package org.smg.TwoLevelCache;

import java.util.HashMap;

@SuppressWarnings("serial")
public class Session extends HashMap<String, Object> {

	private final long id;
	private long startTime;
	
	public Session(long id) {
		this.id = id;
		this.startTime = System.currentTimeMillis();
	}

	public long getId() {
		return id;
	}
	
	public long getAge() {
		return System.currentTimeMillis() - startTime;
	}

}
