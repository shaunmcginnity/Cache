package org.smg.TwoLevelCache;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Session implements Serializable {

	private long id;
	private long startTime;
	private HashMap<String, Object> attributes = new HashMap<> ();
	
	public Session() {
		id = 0;
		startTime = 0;
	}
	
	public Session(long id, long startTime) {
		this.id = id;
		this.startTime = startTime;
	}

	public long getId() {
		return id;
	}
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setId(long id) {
		System.out.println("setId()");
		this.id = id;
	}

	public long getAge() {
		return System.currentTimeMillis() - startTime;
	}

	public void putAll(HashMap<String, Object> attributes) {
		this.attributes.putAll(attributes);
	}

	public void put(String key, String value) {
		attributes.put(key, value);
	}

	public String get(String key) {
		return (String)attributes.get(key);
	}

}
