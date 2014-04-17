package org.smg.TwoLevelCache;

import java.util.HashMap;

final class DataStart {
	private final String sessionId;
	private final HashMap<String, Object> attributes;
	private final LevelOneCache<Session> cache;

	public DataStart(LevelOneCache<Session> cache, String sessionId, HashMap<String, Object> attributes) {
		this.sessionId = sessionId;
		this.cache = cache;
		this.attributes = attributes;
	}

	public void execute() {
		//System.out.println("DATA_START " + session.getId());
		Session cachedSession = (Session)cache.get(sessionId);
		cachedSession.putAll(attributes);
	}
}