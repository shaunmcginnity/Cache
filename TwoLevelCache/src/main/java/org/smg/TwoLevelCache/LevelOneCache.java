package org.smg.TwoLevelCache;

import java.io.InvalidObjectException;
import java.util.LinkedHashMap;

@SuppressWarnings("serial")
public class LevelOneCache extends LinkedHashMap<String, Object> {
	public enum EvictionOrder { ACCESS, INSERTION };
	private final LevelTwoCache l2Cache;
	private int numEntries;
	
	public LevelOneCache(int numEntries, LevelTwoCache l2Cache, EvictionOrder e) {
		super(numEntries, 0.75f, EvictionOrder.ACCESS == e);
		this.l2Cache = l2Cache;
		this.numEntries = numEntries;
	}
	
	@Override
	protected boolean removeEldestEntry(
			java.util.Map.Entry<String, Object> eldest) {
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
	public Object remove(Object key) {
		Object value = super.remove(key);
		if(null == value) {
			return l2Cache.remove(key);
		}
		return value;
	}

	@Override
	public Object put(String key, Object value) {
		l2Cache.remove(key);
		return super.put(key, value);
	}

}
