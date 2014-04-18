package org.smg.TwoLevelCache;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashMap;

public class InMemoryLevelTwoCache<T> implements LevelTwoCache<T> {
	private final HashMap<String, byte[]> map;
	private final LevelTwoCacheEntryBuilder<T> builder;
	
	InMemoryLevelTwoCache(LevelTwoCacheEntryBuilder<T> builder, int initialCapacity) {
		this.builder = builder;
		map = new HashMap<> (initialCapacity);
	}
	
	/* (non-Javadoc)
	 * @see org.smg.TwoLevelCache.LevelTwoCache#get(java.lang.String)
	 */
	@Override
	public T get(String key) {
		byte [] byteArray = map.get(key);
		if(null != byteArray) {
			return extract(byteArray);
		}
		return null;
	}

	private T extract(byte[] byteArray) {
		try {
			return builder.retrieve(byteArray);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.smg.TwoLevelCache.LevelTwoCache#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public void put(String key, T o) throws InvalidObjectException {
		 try {
			byte [] byteArray = builder.build(o);
			//System.out.println("L2Cache : Adding " + key + " " + byteArray.length);
			map.put(key, byteArray);
		} catch (IOException e) {
			System.out.println("L2Cache : Error " + e.getMessage());
			throw new InvalidObjectException(e.getMessage()); // TODO
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.smg.TwoLevelCache.LevelTwoCache#size()
	 */
	@Override
	public int size() {
		return map.size();
	}

	/* (non-Javadoc)
	 * @see org.smg.TwoLevelCache.LevelTwoCache#remove(java.lang.Object)
	 */
	@Override
	public T remove(String key) {
		//System.out.println("L2Cache : Removing " + key);
		byte[] byteArray = map.remove(key);
		if(null != byteArray) {
			return extract(byteArray);
		}
		System.out.println("L2Cache : Can't find " + key);
		return null;
	}

	/* (non-Javadoc)
	 * @see org.smg.TwoLevelCache.LevelTwoCache#removeBeforePut(java.lang.String)
	 */
	@Override
	public void removeBeforePut(String key) {
		if(map.containsKey(key)) {
			remove(key);
		}
	}
} 