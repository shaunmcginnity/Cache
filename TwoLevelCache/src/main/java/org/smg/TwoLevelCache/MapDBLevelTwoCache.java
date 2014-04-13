package org.smg.TwoLevelCache;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Map;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;

public class MapDBLevelTwoCache<T> implements LevelTwoCache<T> {

	private BTreeMap<Object, Object> map;
	private LevelTwoCacheEntryBuilder<T> builder;

	MapDBLevelTwoCache(LevelTwoCacheEntryBuilder<T> builder) {
		this.builder = builder;
		DB db = DBMaker
			     .newMemoryDirectDB()
			     .transactionDisable()
			     .asyncWriteFlushDelay(100)
			     .cacheDisable()
			     .make();

		map = db.getTreeMap("test"); 
	}
	
	@Override
	public T get(String key) {
		byte [] byteArray = (byte [])map.get(key);
		if(null != byteArray) {
			try {
				return builder.retrieve(byteArray);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void put(String key, T o) throws InvalidObjectException {
		try {
			map.put(key, builder.build(o));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new InvalidObjectException(e.getMessage()); // TODO
		}
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public T remove(String key) {
		byte [] byteArray = (byte[]) map.remove(key);
		if(null != byteArray) {
			try {
				return builder.retrieve(byteArray);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void removeBeforePut(String key) {
		remove(key);
	}

}
