package org.smg.TwoLevelCache;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

public class MultithreadedMemcacheLevelTwoCache<T> implements LevelTwoCache<T> {

	private final class Entry {
		public Entry(String key, byte[] bytes) {
			this.key = key;
			this.bytes = bytes;
		}
		public String key;
		public byte[] bytes;
	}
	private final LevelTwoCacheEntryBuilder<T> builder;
	private int size = 0;
	private final TreeSet<String> index = new TreeSet<> ();
	private final BlockingQueue<Entry> queue = new ArrayBlockingQueue<>(1024);
	private final ExecutorService threadPool = Executors.newFixedThreadPool(4);
	private MemcachedClient c;
	
	MultithreadedMemcacheLevelTwoCache(final LevelTwoCacheEntryBuilder<T> l2builder) {
		this.builder = l2builder;
		try {
			c = new MemcachedClient(
					new BinaryConnectionFactory(),
					AddrUtil.getAddresses("127.0.0.1:11211"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			c = null;
		}
		for(int t=0; t<4; t++) {
			threadPool.execute(new Runnable() {

				@Override
				public void run() {
					MemcachedClient c;
					try {
						c = new MemcachedClient(
								new BinaryConnectionFactory(),
								AddrUtil.getAddresses("127.0.0.1:11211"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						c = null;
					}
					while(true) {
						try {
							Entry entry = queue.take();
							c.set(entry.key, 2592000, entry.bytes);
							index.add(entry.key);
							size++;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			});
			
		}

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
		} else {
			System.err.println("MemcacheLevelTwoCache : index error : get of " + key + " returned null");
		}
		return null;
	}

	@Override
	public void put(String key, T o) throws InvalidObjectException {
//		try {
			try {
				queue.put(new Entry(key, builder.build(o)));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//OperationFuture<Boolean> set = c.set(key, 2592000, builder.build(o));
			// Make put synchronous
//			Boolean status = set.get();
//			if(status) {
//			} else {
//				System.out.println("Problem adding session for " + key);
//			}
//		} catch (IOException e) { //| InterruptedException | ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new InvalidObjectException(e.getMessage());
//		}
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
			if(null == object) {
				System.err.println("MemcacheLevelTwoCache : cannot remove " + key);
			}
			Boolean removed = c.delete(key).get();
			if(removed) {
				//System.out.println("Removed : " + key + " " + removed);
				index.remove(key);
			}
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
