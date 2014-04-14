package org.smg.TwoLevelCache;

import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.Map;

public class SimpleLevelTwoCache<T> implements LevelTwoCache<T>{

	private Map<String, T> map = new HashMap<> ();
	
	@Override
	public T get(String key) {
		return map.get(key);
	}

	@Override
	public void put(String key, T o) throws InvalidObjectException {
		map.put(key, o);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public T remove(String key) {
		return map.remove(key);
	}

	@Override
	public void removeBeforePut(String key) {
		remove(key);
	}

}
