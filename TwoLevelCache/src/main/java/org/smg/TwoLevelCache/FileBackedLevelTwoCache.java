package org.smg.TwoLevelCache;

import java.io.IOException;
import java.io.InvalidObjectException;

import org.clapper.util.misc.FileHashMap;

public class FileBackedLevelTwoCache<T> implements LevelTwoCache<T> {

	private FileHashMap<String,byte[]> fileHashMap;
	private final LevelTwoCacheEntryBuilder<T> builder;
	
	FileBackedLevelTwoCache(LevelTwoCacheEntryBuilder<T> builder) {
		this.builder = builder;
		try {
			System.out.println(System.getProperties());
			fileHashMap = new FileHashMap<String, byte[]>("test");
			System.out.println(fileHashMap.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fileHashMap = null;
		}
	}
	@Override
	public T get(String key) {
		byte [] byteArray = fileHashMap.get(key);
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
		byte[] byteArray;
		try {
			byteArray = builder.build(o);
			fileHashMap.put(key, byteArray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int size() {
		return fileHashMap.size();
	}

	@Override
	public T remove(String key) {
		byte [] byteArray = fileHashMap.remove(key);
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
		if(fileHashMap.containsKey(key)) {
			remove(key);
		}
	}

}
