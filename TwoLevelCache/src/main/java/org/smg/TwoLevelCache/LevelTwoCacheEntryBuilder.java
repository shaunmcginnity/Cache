package org.smg.TwoLevelCache;

import java.io.IOException;

public interface LevelTwoCacheEntryBuilder<T> {

	public abstract byte[] build(T o) throws IOException;

	public abstract T retrieve(byte[] byteArray) throws IOException,
			ClassNotFoundException;

}