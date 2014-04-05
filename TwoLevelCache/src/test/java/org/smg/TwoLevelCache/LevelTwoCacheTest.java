package org.smg.TwoLevelCache;

import java.io.InvalidObjectException;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;

public class LevelTwoCacheTest {
	@Test
	public void addingAnObjectWriteSerializedObjectToMap() throws InvalidObjectException {
		LevelTwoCache l2cache = new LevelTwoCache();
		
		HashMap<String,String> map = new HashMap<> ();
		map.put("a", "v");
		
		l2cache.put("1", map);
		
		Object cachedObject = l2cache.get("1");
		@SuppressWarnings("unchecked")
		HashMap<String,String> cachedMap = (HashMap<String,String>)cachedObject;
		Assert.assertEquals("v", cachedMap.get("a"));
	}
}
