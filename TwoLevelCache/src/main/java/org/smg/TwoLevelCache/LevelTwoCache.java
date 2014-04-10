package org.smg.TwoLevelCache;

import java.io.InvalidObjectException;

public interface LevelTwoCache<T> {

	public abstract T get(String key);

	public abstract void put(String key, T o)
			throws InvalidObjectException;

	public abstract int size();

	public abstract T remove(String key);

	public abstract void removeBeforePut(String key);

}