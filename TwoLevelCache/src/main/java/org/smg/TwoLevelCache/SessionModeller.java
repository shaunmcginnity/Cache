package org.smg.TwoLevelCache;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.smg.TwoLevelCache.LevelOneCache.EvictionOrder;

public class SessionModeller 
{
	private static final LevelTwoCacheEntryBuilder<Session> l2Builder = new DefaultSerializingLevelTwoCacheEntryBuilder();
	private static final LevelTwoCache<Session> l2Cache = new InMemoryLevelTwoCache<>(l2Builder);
	private static final LevelOneCache<Session> cache = new LevelOneCache<>(10000, l2Cache, EvictionOrder.ACCESS);
	private static final Mean startMean = new Mean();
	private static final Mean stopMean = new Mean();
	private static final Mean sessionAge = new Mean();
	private static final int sAVERAGE_SESSION_DURATION = 120;
	
	private static final class SessionStop implements Runnable {
		private String sessionId;

		public SessionStop(String id) {
			this.sessionId = id;
		}

		@Override
		public void run() {
			//System.out.println("STOP " + session.getId());
			long now = System.currentTimeMillis();
			Session session;
			try {
				session = (Session)SessionModeller.cache.remove(sessionId);
				sessionAge.increment(session.getAge());
				stopMean.increment(System.currentTimeMillis() - now);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static final class DataStart implements Runnable {
		private final String sessionId;
		private final HashMap<String, Object> attributes;

		public DataStart(String sessionId, HashMap<String, Object> attributes) {
			this.sessionId = sessionId;
			this.attributes = attributes;
		}

		@Override
		public void run() {
			//System.out.println("DATA_START " + session.getId());
			Session cachedSession = (Session)SessionModeller.cache.get(sessionId);
			cachedSession.putAll(attributes);
		}
	}
    private static final class SessionInitiator implements Runnable {

		private final ScheduledThreadPoolExecutor e;
		private final Random r;

		private SessionInitiator(ScheduledThreadPoolExecutor e, Random r) {
			this.e = e;
			this.r = r;
		}

		@Override
		public void run() {
			Session s = new Session(r.nextLong(), System.currentTimeMillis());
			addInitialSessionAttributes(s);
			//System.out.println("START " + s.getId());
			long now = System.currentTimeMillis();
			SessionModeller.cache.put(Long.toString(s.getId()), s);
			long delta = System.currentTimeMillis() - now;
			startMean.increment(delta);
			if(r.nextInt(100) < 70) {
				e.schedule(new DataStart(Long.toString(s.getId()), dataStartAttributes()), 5, TimeUnit.MILLISECONDS);
			}
			e.schedule(new SessionStop(Long.toString(s.getId())), sAVERAGE_SESSION_DURATION, TimeUnit.SECONDS);
		}

		private HashMap<String,Object> dataStartAttributes() {
			HashMap<String, Object> attributes = new HashMap<String, Object>();
			for(int i=0; i<10; i++) {
				attributes.put("dataStartAttr" + i, r.nextInt() + "value");
			}
			return attributes;
		}

		private void addInitialSessionAttributes(Session s) {
			for(int i=0; i<10; i++) {
				s.put("initAttr" + i, r.nextInt() + "value");
			}			
		}
	}

	public static void main( String[] args )
    {
        final ScheduledThreadPoolExecutor e = new ScheduledThreadPoolExecutor(1);
        final Random r = new Random();
        e.scheduleAtFixedRate(new SessionInitiator(e, r), 0, 400, TimeUnit.MICROSECONDS);

        e.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out.println("SIZE : " + cache.size() + " " + l2Cache.size() + " " + e.getTaskCount());
				System.out.println(startMean.getResult() + " " + stopMean.getResult() + " " + sessionAge.getResult());
				startMean.clear();
				stopMean.clear();
				sessionAge.clear();
			}
        	
        }, 0, 10000, TimeUnit.MILLISECONDS);
}
}
