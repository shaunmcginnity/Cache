package org.smg.TwoLevelCache;

import junit.framework.Assert;

import org.junit.Test;
import org.smg.TwoLevelCache.LevelOneCache.EvictionOrder;

public class LevelOneCacheTest {

	@Test
	public void testStoresEvictedEntriesInLevelTwoCache() {
		DefaultSerializingLevelTwoCacheEntryBuilder<String> levelTwoBuilder = new DefaultSerializingLevelTwoCacheEntryBuilder<>();
		LevelTwoCache<String> l2Cache = new InMemoryLevelTwoCache<>(levelTwoBuilder, 100);
		LevelOneCache<String> l1Cache = new LevelOneCache<>(5, l2Cache, EvictionOrder.ACCESS);
		
		for(int i=0; i<5; i++) {
			l1Cache.put(Integer.toString(i), Integer.toString(i*i));
		}
		l1Cache.put("a", "v");
		
		Object object = l2Cache.get("0");
		Assert.assertNotNull(object);
	}

	@Test
	public void testLeastRecentlyUsedEntryIsEvictedFirstToLevelTwoCache() {
		DefaultSerializingLevelTwoCacheEntryBuilder<String> levelTwoBuilder = new DefaultSerializingLevelTwoCacheEntryBuilder<>();
		LevelTwoCache<String> l2Cache = new InMemoryLevelTwoCache<>(levelTwoBuilder,100);
		LevelOneCache<String> l1Cache = new LevelOneCache<>(5, l2Cache, EvictionOrder.ACCESS);
		
		for(int i=0; i<5; i++) {
			l1Cache.put(Integer.toString(i), Integer.toString(i*i));
		}
		l1Cache.put("0", "abcd");
		l1Cache.put("a", "v");
		
		Object object = l2Cache.get("1");
		Assert.assertNotNull(object);
	}

	@Test
	public void testRemovingAnItemIsAlsoRemovedFromLevelTwoCache() {
		DefaultSerializingLevelTwoCacheEntryBuilder<String> levelTwoBuilder = new DefaultSerializingLevelTwoCacheEntryBuilder<>();
		LevelTwoCache<String> l2Cache = new InMemoryLevelTwoCache<>(levelTwoBuilder,100);
		LevelOneCache<String> l1Cache = new LevelOneCache<>(5, l2Cache, EvictionOrder.ACCESS);

		for(int i=0; i<5; i++) {
			l1Cache.put(Integer.toString(i), Integer.toString(i*i));
		}
		l1Cache.put("0", "abcd");
		l1Cache.put("a", "v");
		
		l1Cache.remove("1");
		Object object = l2Cache.get("1");
		Assert.assertNull(object);
	}

	@Test
	public void testUpdatingAnEvictedItemRemovesItFromLevelTwoCache() {
		DefaultSerializingLevelTwoCacheEntryBuilder<String> levelTwoBuilder = new DefaultSerializingLevelTwoCacheEntryBuilder<>();
		LevelTwoCache<String> l2Cache = new InMemoryLevelTwoCache<>(levelTwoBuilder, 100);
		LevelOneCache<String> l1Cache = new LevelOneCache<>(5, l2Cache, EvictionOrder.ACCESS);
		
		for(int i=0; i<5; i++) {
			l1Cache.put(Integer.toString(i), Integer.toString(i*i));
		}
		l1Cache.put("0", "abcd");
		l1Cache.put("a", "v");
		
		l1Cache.put("1", "efgh");
		Object object = l2Cache.get("1");
		Assert.assertNull(object);
	}


}
