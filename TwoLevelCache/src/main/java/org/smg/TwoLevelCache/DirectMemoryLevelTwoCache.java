package org.smg.TwoLevelCache;

import java.io.IOException;
import java.io.InvalidObjectException;

import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;

public class DirectMemoryLevelTwoCache<T> implements LevelTwoCache<T> {

	private CacheService<String, byte[]> cacheService;
	private LevelTwoCacheEntryBuilder<T> builder;

	DirectMemoryLevelTwoCache(LevelTwoCacheEntryBuilder<T> builder) {
		this.builder = builder;
		  this.cacheService = new DirectMemory<String, byte[]>()
				    .setNumberOfBuffers( 10 )
				    .setSize( 1000 )
				    .setDisposalTime(10000000)
				    .setInitialCapacity( 100000 )
				    .setConcurrencyLevel( 4 )
				    .newCacheService();
	}
	
	@Override
	public T get(String key) {
		byte [] byteArray = cacheService.retrieveByteArray(key);
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
			if(null == cacheService.putByteArray(key, byteArray)) {
				System.err.println("ERROR");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int size() {
		return (int)cacheService.entries();
	}

	@Override
	public T remove(String key) {
		T o = get(key);
		cacheService.free(key);
		return o;
	}

	@Override
	public void removeBeforePut(String key) {
		remove(key);
	}

}

