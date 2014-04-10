package org.smg.TwoLevelCache;

import java.io.InvalidObjectException;
import java.util.LinkedHashMap;

@SuppressWarnings("serial")
public class LevelOneCache<T> extends LinkedHashMap<String, T> {
	public enum EvictionOrder { ACCESS, INSERTION };
	private final LevelTwoCache<T> l2Cache;
	private int numEntries;
	
	public LevelOneCache(int numEntries, LevelTwoCache<T> l2Cache, EvictionOrder e) {
		super(numEntries, 0.75f, EvictionOrder.ACCESS == e);
		this.l2Cache = l2Cache;
		this.numEntries = numEntries;
	}
	
	@Override
	protected boolean removeEldestEntry(
			java.util.Map.Entry<String, T> eldest) {
		//System.out.println("L1Cache : removeOldestEntry " + eldest.getKey());
		if(super.size() > numEntries) {
			//System.out.println("L1Cache : Evicting " + eldest.getKey());
			try {
				l2Cache.put(eldest.getKey(), eldest.getValue());
			} catch (InvalidObjectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	@Override
	public T remove(Object key) {
		T value = super.remove(key);
		if(null == value) {
			return l2Cache.remove((String)key);
		}
		return value;
	}

	@Override
	public T put(String key, T value) {
		l2Cache.removeBeforePut(key);
		return super.put(key, value);
	}

	@Override
	public T get(Object key) {
		if(this.containsKey(key)) {
			return super.get(key);
		}
		T o = l2Cache.remove((String)key);
		if(null != o) {
			super.put((String)key, o);
			return o;
		}
		return null;
	}
	
	
}
