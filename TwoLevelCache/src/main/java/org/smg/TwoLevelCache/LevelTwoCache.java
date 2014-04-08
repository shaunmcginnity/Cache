package org.smg.TwoLevelCache;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashMap;

public class LevelTwoCache {
	private final HashMap<String, byte[]> map = new HashMap<> ();
	private final LevelTwoCacheEntryBuilder builder = new LevelTwoCacheEntryBuilder();
	public Object get(String key) {
		byte [] byteArray = map.get(key);
		if(null != byteArray) {
			return extract(byteArray);
		}
		return null;
	}

	private Object extract(byte[] byteArray) {
		try {
			return builder.retrieve(byteArray);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void put(String key, Object o) throws InvalidObjectException {
		 try {
			System.out.println("L2Cache : Adding " + key);
			byte [] byteArray = builder.build(o);
			map.put(key, byteArray);
		} catch (IOException e) {
			throw new InvalidObjectException(e.getMessage()); // TODO
		}
		
	}
	
	public int size() {
		return map.size();
	}

	public Object remove(Object key) {
		System.out.println("L2Cache : Removing " + key);
		byte[] byteArray = map.remove(key);
		if(null != byteArray) {
			return extract(byteArray);
		}
		return null;
	}
} 