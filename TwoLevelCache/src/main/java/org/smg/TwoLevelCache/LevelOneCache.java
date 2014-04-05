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
		// TODO Auto-generated method stub
		System.out.println("L1Cache : removeOldestEntry " + eldest.getKey());
		if(super.size() > numEntries) {
			System.out.println("L1Cache : Evicting " + eldest.getKey());
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

}
