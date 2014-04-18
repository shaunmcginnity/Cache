package org.smg.TwoLevelCache;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.smg.TwoLevelCache.LevelOneCache.EvictionOrder;

final class SessionInitiator implements Runnable {

	private final SessionModeller sessionModeller;
	private final ScheduledThreadPoolExecutor e;
	private final Random r;
	private long started = 0;
	private int sessionAge;
	private final LevelOneCache<Session> levelOneCache;
	
	SessionInitiator(SessionModeller sessionModeller, ScheduledThreadPoolExecutor e, Random r, int sessionAge) {
		this.sessionModeller = sessionModeller;
		this.e = e;
		this.r = r;
		this.sessionAge = sessionAge;
		this.levelOneCache = sessionModeller.getLevelOneCache();
	}

	@Override
	public void run() {
		if(started > 2500000) {
			return;
		}
		final String id = Long.toString(r.nextLong());
		Session s = new Session(id, System.currentTimeMillis(), sessionModeller.getSessionAttributesBuilder().initialSessionAttributes());
		//System.out.println("START " + s.getId());
		long now = System.currentTimeMillis();
		levelOneCache.put(id, s);
		long delta = System.currentTimeMillis() - now;
		sessionModeller.recordStartTime(delta);

		started++;

		if(r.nextInt(100) < 70) {
			e.schedule(new Runnable() {
				@Override
				public void run() {
					DataStart dataStart = new DataStart(levelOneCache, id, sessionModeller.getSessionAttributesBuilder().dataStartAttributes());
					dataStart.execute();
				}
			}, 5, TimeUnit.MILLISECONDS);
		}

		if(started < 500000) {
			e.schedule(new Runnable() {
				@Override
				public void run() {
					SessionStop sessionStop = new SessionStop(levelOneCache, id);
					long now = System.currentTimeMillis();
					Session session = sessionStop.execute();
					if(null != session) {
						sessionModeller.recordSessionAge(session.getAge());
						sessionModeller.recordStopTime(System.currentTimeMillis() - now);
					}
				}
			}, this.sessionAge, TimeUnit.SECONDS);
		}
	}

}

final class SessionStatsReporter implements Runnable {

	/**
	 * 
	 */
	private final SessionModeller sessionModeller;
	private final ScheduledThreadPoolExecutor e;

	SessionStatsReporter(SessionModeller sessionModeller, ScheduledThreadPoolExecutor e) {
		this.sessionModeller = sessionModeller;
		this.e = e;
	}
	@Override
	public void run() {
		sessionModeller.report();
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
    private final Random r = new Random();
    private final int meanSessionAge;
	private final int sessionInitiationPeriod;
	private final SessionAttributesBuilder sessionAttributesBuilder;
	private static final int sInitialL2CacheCapacity = 1500000;

	SessionModeller(SessionAttributesBuilder sessionAttributesBuilder, int l2CacheType, int l1CacheSize, int sessionAge, int sessionInitiationPeriod) {
		this.sessionAttributesBuilder = sessionAttributesBuilder;
		l2Cache = LevelTwoCacheFactory.build(l2CacheType, l2Builder, sInitialL2CacheCapacity );
		cache = new LevelOneCache<>(l1CacheSize, l2Cache, EvictionOrder.ACCESS);
		this.meanSessionAge = sessionAge;
		this.sessionInitiationPeriod = sessionInitiationPeriod;
		System.out.println(String.format("Using \nL1 Cache size : %d\nL2 Cache : %s\nMean session age : %d\nSession initiation period : %d\n", l1CacheSize, l2Cache.getClass().getName(), this.meanSessionAge, this.sessionInitiationPeriod));
	}

	public SessionAttributesBuilder getSessionAttributesBuilder() {
		return sessionAttributesBuilder;
	}

	public void report() {
		System.out.println("SIZE : " + getLevelOneCacheSize() + " " + getLevelTwoCacheSize() + " " + e.getTaskCount());
		System.out.println(startMean.getResult() + " " + stopMean.getResult() + " " + sessionAge.getResult());
		startMean.clear();
		stopMean.clear();
		sessionAge.clear();
	}

	public void recordStopTime(long delta) {
		this.stopMean.increment(delta);
	}

	public void recordSessionAge(long age) {
		this.sessionAge.increment(age);
	}

	public void recordStartTime(long delta) {
		this.startMean.increment(delta);
	}

	public LevelOneCache<Session> getLevelOneCache() {
		return this.cache;
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
		
		SessionAttributesBuilder sessionAttributesBuilder = new SessionAttributesBuilder(sessionKeysStringPool);
		final SessionModeller sessionModeller = new SessionModeller(sessionAttributesBuilder, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		
        sessionModeller.startSessions();
    }

	private void startSessions() {
        e.scheduleAtFixedRate(new SessionInitiator(this, e, r, this.meanSessionAge), 0, sessionInitiationPeriod, TimeUnit.MICROSECONDS);
        e.scheduleAtFixedRate(new SessionStatsReporter(this, e), 10000, 10000, TimeUnit.MILLISECONDS);
	}

	public int getLevelOneCacheSize() {
		return this.cache.size();
	}

	public int getLevelTwoCacheSize() {
		return this.l2Cache.size();
	}
	
}
