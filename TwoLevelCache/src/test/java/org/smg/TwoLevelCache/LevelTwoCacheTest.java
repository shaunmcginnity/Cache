package org.smg.TwoLevelCache;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;

public class LevelTwoCacheTest {
	@Test
	public void addingAnObjectWriteSerializedObjectToMap() throws InvalidObjectException {
		DefaultSerializingLevelTwoCacheEntryBuilder<HashMap<String,String>> levelTwoBuilder = new DefaultSerializingLevelTwoCacheEntryBuilder<>();
		LevelTwoCache<HashMap<String,String>> l2Cache = new InMemoryLevelTwoCache<>(levelTwoBuilder);
		
		HashMap<String,String> map = new HashMap<> ();
		map.put("a", "v");
		
		l2Cache.put("1", map);
		
		Object cachedObject = l2Cache.get("1");
		@SuppressWarnings("unchecked")
		HashMap<String,String> cachedMap = (HashMap<String,String>)cachedObject;
		Assert.assertEquals("v", cachedMap.get("a"));
	}
//	@Test
//	public void kryoSerializerTest() throws InvalidObjectException {
//		KryoSerializingLevelTwoCacheEntryBuilder<HashMap<String,String>> levelTwoBuilder = new KryoSerializingLevelTwoCacheEntryBuilder<>();
//		LevelTwoCache<HashMap<String,String>> l2Cache = new InMemoryLevelTwoCache<>(levelTwoBuilder);
//		
//		HashMap<String,String> map = new HashMap<> ();
//		map.put("a", "v");
//		
//		l2Cache.put("1", map);
//		
//		Object cachedObject = l2Cache.get("1");
//		@SuppressWarnings("unchecked")
//		HashMap<String,String> cachedMap = (HashMap<String,String>)cachedObject;
//		Assert.assertEquals("v", cachedMap.get("a"));
//	}
	
	@Test
	public void testSessionSerialization() throws IOException, ClassNotFoundException {
		KryoSerializingLevelTwoCacheEntryBuilder levelTwoBuilder = new KryoSerializingLevelTwoCacheEntryBuilder();
		
		Session session = new Session(123456l, 1000l);
		session.put("a", "b");
		
		byte[] serialized = levelTwoBuilder.build(session);
		
		System.out.println("Serialized to " + serialized.length);
		
		Session retrieved = levelTwoBuilder.retrieve(serialized);
		
		Assert.assertEquals("b", session.get("a"));
		Assert.assertEquals(123456, retrieved.getId());
	}

}
