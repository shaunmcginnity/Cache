package org.smg.TwoLevelCache;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

public class MemcacheLevelTwoCache<T> implements LevelTwoCache<T> {

	private LevelTwoCacheEntryBuilder<T> builder;
	private MemcachedClient c;
	private int size = 0;
	private final TreeSet<String> index = new TreeSet<> ();
	MemcacheLevelTwoCache(LevelTwoCacheEntryBuilder<T> builder) {
		this.builder = builder;
		try {
			c = new MemcachedClient(
					new BinaryConnectionFactory(),
			        AddrUtil.getAddresses("10.20.50.164:11211"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			c = null;
		}

			c.set("someKey", 3600, "Hello");
			// Retrieve a value (synchronously).
			Object myObject=c.get("someKey");
			System.out.println("Got " + (String)myObject);
		}
	
	@Override
	public T get(String key) {
		if(index.contains(key) == false) {
			return null;
		}
		byte [] stored = (byte[]) c.get(key);
		if(null != stored) {
			try {
				return builder.retrieve(stored);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	@Override
	public void put(String key, T o) throws InvalidObjectException {
		try {
			OperationFuture<Boolean> set = c.set(key, 0, builder.build(o));
			// Make put synchronous
			Boolean status = set.get();
			if(status) {
				index.add(key);
				size++;
			}
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InvalidObjectException(e.getMessage());
		}
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return index.size();
	}

	@Override
	public T remove(String key) {
		if(index.contains(key) == false) {
			return null;
		}
		T object = null;
		try {
			object = get(key);
			// TODO - return removed object
			c.delete(key);
			index.remove(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return object;
	}

	@Override
	public void removeBeforePut(String key) {
		remove(key);
	}

}
