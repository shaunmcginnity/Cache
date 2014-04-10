package org.smg.TwoLevelCache;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.HashMap;

public class InMemoryLevelTwoCache implements LevelTwoCache {
	private final HashMap<String, byte[]> map = new HashMap<> ();
	private final LevelTwoCacheEntryBuilder builder = new LevelTwoCacheEntryBuilder();
	/* (non-Javadoc)
	 * @see org.smg.TwoLevelCache.LevelTwoCache#get(java.lang.String)
	 */
	@Override
	public Object get(String key) {
		byte [] byteArray = map.get(key);
		if(null != byteArray) {
			return extract(byteArray);
		}
		return null;
	}

	private Object extract(byte[] byteArray) {
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
	public void put(String key, Object o) throws InvalidObjectException {
		 try {
			//System.out.println("L2Cache : Adding " + key);
			byte [] byteArray = builder.build(o);
			map.put(key, byteArray);
		} catch (IOException e) {
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
	public Object remove(Object key) {
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