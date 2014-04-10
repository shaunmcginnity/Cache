package org.smg.TwoLevelCache;

import java.io.InvalidObjectException;

public interface LevelTwoCache {

	public abstract Object get(String key);

	public abstract void put(String key, Object o)
			throws InvalidObjectException;

	public abstract int size();

	public abstract Object remove(Object key);

	public abstract void removeBeforePut(String key);

}