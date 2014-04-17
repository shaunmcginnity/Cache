package org.smg.TwoLevelCache;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.smg.TwoLevelCache.LevelOneCache.EvictionOrder;

class LevelTwoCacheFactory {
	static LevelTwoCache<Session> build(int id, LevelTwoCacheEntryBuilder<Session> builder) {
		switch(id) {
		case 1:
			return new InMemoryLevelTwoCache<>(builder);
		case 2:
			return new FileBackedLevelTwoCache<>(builder);
		case 3:
			return new MemcacheLevelTwoCache<>(builder);
		case 4:
			return new MapDBLevelTwoCache<>(builder);
		default:
			return new NotCompressingLevelTwoCache<>();
		}
	}
}

public class SessionModeller 
{
	private final LevelTwoCacheEntryBuilder<Session> l2Builder = new KryoSerializingSessionEntryBuilder2();
	private final LevelTwoCache<Session> l2Cache;
	private final LevelOneCache<Session> cache;
	private final Mean startMean = new Mean();
	private final Mean stopMean = new Mean();
	private final Mean sessionAge = new Mean();
    private final ScheduledThreadPoolExecutor e = new ScheduledThreadPoolExecutor(1);
    final Random r = new Random();
	private final int meanSessionAge;
	private final int sessionInitiationPeriod;
	private StringPool sessionKeysStringPool;

	SessionModeller(StringPool sessionKeysStringPool, int l2CacheType, int l1CacheSize, int sessionAge, int sessionInitiationPeriod) {
		this.sessionKeysStringPool = sessionKeysStringPool;
		l2Cache = LevelTwoCacheFactory.build(l2CacheType, l2Builder);
		cache = new LevelOneCache<>(l1CacheSize, l2Cache, EvictionOrder.ACCESS);
		this.meanSessionAge = sessionAge;
		this.sessionInitiationPeriod = sessionInitiationPeriod;
		System.out.println(String.format("Using \nL1 Cache size : %d\nL2 Cache : %s\nMean session age : %d\nSession initiation period : %d\n", l1CacheSize, l2Cache.getClass().getName(), this.meanSessionAge, this.sessionInitiationPeriod));
	}

	private StringPool getSessionKeysStringPool() {
		return sessionKeysStringPool;
	}
	
	private final class SessionStop implements Runnable {
		private final String sessionId;
		private final SessionModeller sessionModeller;

		public SessionStop(SessionModeller sessionModeller, String id) {
			this.sessionId = id;
			this.sessionModeller = sessionModeller;
		}

		@Override
		public void run() {
			//System.out.println("STOP " + session.getId());
			long now = System.currentTimeMillis();
			Session session;
			try {
				session = (Session)sessionModeller.cache.remove(sessionId);
				if(null != session) {
					sessionAge.increment(session.getAge());
					stopMean.increment(System.currentTimeMillis() - now);
				} else {
					System.err.println("STOP error getting " + sessionId);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private final class DataStart implements Runnable {
		private final String sessionId;
		private final SessionModeller sessionModeller;
		private final HashMap<String, Object> attributes;

		public DataStart(SessionModeller sessionModeller, String sessionId, HashMap<String, Object> attributes) {
			this.sessionId = sessionId;
			this.sessionModeller = sessionModeller;
			this.attributes = attributes;
		}

		@Override
		public void run() {
			//System.out.println("DATA_START " + session.getId());
			Session cachedSession = (Session)sessionModeller.cache.get(sessionId);
			cachedSession.putAll(attributes);
		}
	}

	private final class SessionStatsReporter implements Runnable {

		private final ScheduledThreadPoolExecutor e;
		private final SessionModeller sessionModeller;

		private SessionStatsReporter(SessionModeller sessionModeller, ScheduledThreadPoolExecutor e) {
			this.e = e;
			this.sessionModeller = sessionModeller;
		}
		@Override
		public void run() {
			System.out.println("SIZE : " + cache.size() + " " + l2Cache.size() + " " + e.getTaskCount());
			System.out.println(sessionModeller.startMean.getResult() + " " + sessionModeller.stopMean.getResult() + " " + sessionModeller.sessionAge.getResult());
			sessionModeller.startMean.clear();
			sessionModeller.stopMean.clear();
			sessionModeller.sessionAge.clear();
		}
    	
    }
	
	private final class SessionInitiator implements Runnable {

		private final ScheduledThreadPoolExecutor e;
		private final SessionModeller sessionModeller;
		private final Random r;
		private long started = 0;
		
		private SessionInitiator(SessionModeller sessionModeller, ScheduledThreadPoolExecutor e, Random r) {
			this.e = e;
			this.sessionModeller = sessionModeller;
			this.r = r;
		}

		@Override
		public void run() {
			if(started > 1000000) {
				return;
			}
			String id = Long.toString(r.nextLong());
			Session s = new Session(id, System.currentTimeMillis());
			addInitialSessionAttributes(s);
			//System.out.println("START " + s.getId());
			long now = System.currentTimeMillis();
			sessionModeller.cache.put(id, s);
			long delta = System.currentTimeMillis() - now;
			startMean.increment(delta);

			started++;

			if(r.nextInt(100) < 70) {
				e.schedule(new DataStart(sessionModeller,
						id,
						dataStartAttributes()),
						5,
						TimeUnit.MILLISECONDS);
			}

			if(started < 100000) {
				e.schedule(new SessionStop(sessionModeller, id),
						meanSessionAge,
						TimeUnit.SECONDS);
			}
		}

		private HashMap<String,Object> dataStartAttributes() {
			HashMap<String, Object> attributes = new HashMap<String, Object>();
			StringPool sessionKeys = sessionModeller.getSessionKeysStringPool();
			for(int i=0; i<10; i++) {
				attributes.put(sessionKeys.getStringFor(r.nextInt(sessionKeys.getSize())),
						       r.nextInt() + "value");
			}
			return attributes;
		}

		private void addInitialSessionAttributes(Session s) {
			for(int i=0; i<10; i++) {
				StringPool sessionKeys = sessionModeller.getSessionKeysStringPool();
				int keyIndex = r.nextInt(sessionKeys.getSize());
				s.put(sessionKeys.getStringFor(keyIndex), r.nextInt() + "value");
			}			
		}
	}

	public static void main(String[] args) {
		System.out.println("Starting...");
		
		StringPool sessionKeysStringPool = null;
		int stringPoolType = Integer.parseInt(args[4]);
		if(stringPoolType == 0) {
			sessionKeysStringPool = new ArrayBackedStringPool("attribute", 400, false);
		} else if(stringPoolType == 1){
			sessionKeysStringPool = new ArrayBackedStringPool("attribute", 400, true);
		} else if(stringPoolType == 2){
			sessionKeysStringPool = new NonPoolingStringPool("attribute", 400, false);
		} else if(stringPoolType == 3){
			sessionKeysStringPool = new NonPoolingStringPool("attribute", 400, true);
		}
		final SessionModeller sessionModeller = new SessionModeller(sessionKeysStringPool, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		
        sessionModeller.startSessions();
    }

	private void startSessions() {
        e.scheduleAtFixedRate(new SessionInitiator(this, e, r), 0, sessionInitiationPeriod, TimeUnit.MICROSECONDS);
        e.scheduleAtFixedRate(new SessionStatsReporter(this, e), 10000, 10000, TimeUnit.MILLISECONDS);
	}
	
}
