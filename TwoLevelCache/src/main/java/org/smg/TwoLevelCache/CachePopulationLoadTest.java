package org.smg.TwoLevelCache;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.smg.TwoLevelCache.LevelOneCache.EvictionOrder;

public class CachePopulationLoadTest 
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
	private StringPool sessionKeysStringPool;

	CachePopulationLoadTest(StringPool sessionKeysStringPool, int l2CacheType, int l1CacheSize, int sessionAge, int sessionInitiationPeriod) {
		this.sessionKeysStringPool = sessionKeysStringPool;
		l2Cache = LevelTwoCacheFactory.build(l2CacheType, l2Builder);
		cache = new LevelOneCache<>(l1CacheSize, l2Cache, EvictionOrder.ACCESS);
		this.meanSessionAge = sessionAge;
		this.sessionInitiationPeriod = sessionInitiationPeriod;
		System.out.println(String.format("Using \nL1 Cache size : %d\nL2 Cache : %s\nMean session age : %d\nSession initiation period : %d\n", l1CacheSize, l2Cache.getClass().getName(), this.meanSessionAge, this.sessionInitiationPeriod));
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

	StringPool getSessionKeysStringPool() {
		return sessionKeysStringPool;
	}
	
	public static void main(String[] args) throws InterruptedException {
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
		final CachePopulationLoadTest cachePopulationLoadTest = new CachePopulationLoadTest(sessionKeysStringPool, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		
		cachePopulationLoadTest.run(sessionAttributesBuilder);
    }

	private void run(SessionAttributesBuilder sessionAttributesBuilder) throws InterruptedException {
		long start = System.currentTimeMillis();
		for(int i=0; i<1000000; i++) {
			String id = "session" + i;
			Session s = new Session(id, 0, sessionAttributesBuilder.dataStartAttributes());
			cache.put(id, s);
		}
		System.out.println(System.currentTimeMillis() - start);
		Thread.sleep(10000);
	}

	public int getLevelOneCacheSize() {
		return this.cache.size();
	}

	public int getLevelTwoCacheSize() {
		return this.l2Cache.size();
	}
	
}
