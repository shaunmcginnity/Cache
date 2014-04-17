package org.smg.TwoLevelCache;

final class SessionStop {
	/**
	 * 
	 */
	private final LevelOneCache<Session> cache;
	private final String sessionId;
	// private final SessionModeller sessionModeller;

	public SessionStop(LevelOneCache<Session> cache, String id) {
		this.cache = cache;
		this.sessionId = id;
	}

	public Session execute() {
		//System.out.println("STOP " + session.getId());
		Session session = null;
		try {
			session = cache.remove(sessionId);
			if(null == session) {
				System.err.println("STOP error getting " + sessionId);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return session;
	}
}