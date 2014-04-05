package org.smg.TwoLevelCache;

import junit.framework.Assert;

import org.junit.Test;
import org.smg.TwoLevelCache.LevelOneCache.EvictionOrder;

public class LevelOneCacheTest {

	@Test
	public void testStoresEvictedEntriesInLevelTwoCache() {
		LevelTwoCache l2Cache = new LevelTwoCache();
		LevelOneCache l1Cache = new LevelOneCache(5, l2Cache, EvictionOrder.ACCESS);
		
		for(int i=0; i<5; i++) {
			l1Cache.put(Integer.toString(i), Integer.toString(i*i));
		}
		l1Cache.put("a", "v");
		
		Object object = l2Cache.get("0");
		Assert.assertNotNull(object);
	}

	@Test
	public void testLEastRecentlyUsedEntryIsEvictedFirstToLevelTwoCache() {
		LevelTwoCache l2Cache = new LevelTwoCache();
		LevelOneCache l1Cache = new LevelOneCache(5, l2Cache, EvictionOrder.ACCESS);
		
		for(int i=0; i<5; i++) {
			l1Cache.put(Integer.toString(i), Integer.toString(i*i));
		}
		l1Cache.put("0", "abcd");
		l1Cache.put("a", "v");
		
		Object object = l2Cache.get("1");
		Assert.assertNotNull(object);
	}

}
