package org.smg.TwoLevelCache;

import java.util.HashMap;

@SuppressWarnings("serial")
public class Session extends HashMap<String, Object> {

	private final long id;
	
	public Session(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

}
