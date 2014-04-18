package org.smg.sessionmanager.cache;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.smg.pcca.selector.CacheReplicaEndpoint;
import org.smg.sessionmanager.events.SessionEvent;

import jsr166e.ConcurrentHashMapV8;

public final class SessionCacheImpl implements SessionCache, SessionEventCompletionListener {

    private static final int MILLISECONDS_PER_MINUTE = 60_000;

    // Reserve enough space for about one and a half million entries.
    private static final int INITIAL_CAPACITY = 1_500_000;

    private final Map<Key, Session> sessionCache;

    private final long cacheTTLInMillis = 30 * MILLISECONDS_PER_MINUTE;

    private final Timer cacheEvictionTimer = new Timer("Cache-Eviction-Timer");

    public SessionCacheImpl() throws UnknownHostException {
        sessionCache = new ConcurrentHashMapV8<>(INITIAL_CAPACITY);

    }

    @Override
    public Session createSessionWithIP(final String ipAddress) {
        return makeNewSession(ipAddress, null);
    }

    @Override
    public Session createSessionWithIPAndMSISDN(final String ipAddress, final String identity) {
        return makeNewSession(ipAddress, identity);
    }

    @Override
    public void removeSession(final String ipAddress, final String msisdn) {
        final Session s = getByIPAddress(ipAddress);
        if (null == s) {
            return;
        }

        removeSession(s);
    }

    @Override
    public void removeSession(final Session session) {
    	final Session removedSession = removeByKey(session.getKey());
        if (removedSession != null) {
            session.setIsNotCached();
        }
    }

    @Override
    public Session getByIPAddress(final String ipAddress) {
        final Key key = Key.createKey().withIPAddress(ipAddress);
        return getSession(key);
    }
    
    @Override
    public Session getByIPAddressEnforceLookup(final String ipAddress) {
        final Key key = Key.createKey().withIPAddress(ipAddress);
        Session session = sessionCache.get(key);
        return session;
    }

    @Override
    public Session getByPCRFSessionId(final String id) {
        final Key key = Key.createKey().withPCRFSessionId(id);
        return getSession(key);
    }

    @Override
    public Session getByNEPSessionId(final String id) {
        if (null == id || id.isEmpty()) {   
            return null;
        }

        for (final Map.Entry<Key, Session> entry : sessionCache.entrySet()) {
            final Session session = entry.getValue();
            if (null != session.getNepSessionId() && id.equals(session.getNepSessionId())) {
                return session;
            }
        }

        return null;
    }

    @Override
    public long getNumCacheElements() {
        return sessionCache.size();
    }

    @Override
    public void close() {
        cacheEvictionTimer.cancel();
    }

    /*
     * Replicates the session to PCCA replica instances.
     */
    @Override
    public void replicateSession(final Session session) {
        try {
            final String ip = session.getIp();

            final Key key = Key.createKey().withIPAddress(ip);
            addSessionToCache(key, session);
            session.setIsCached();
        } catch (final Exception e) {
            // TODO?
        }
    }

    @Override
    public Session replicateSessionRemoval(final Key key) {
        final Session removedSession = removeByKey(key);

        return removedSession;
    }

    @Override
    public void pushSessionToRemotePCCA(final Session session) {
    }

    @Override
    public void removeSessionFromRemotePCCA(final Session session) {
    }

    @Override
    public List<String> getAllClientIPAddresses() {
        final List<String> clientIpAddresses = new ArrayList<>(sessionCache.size());

        for (final Map.Entry<Key, Session> entry : sessionCache.entrySet()) {
            // do not include the IP of a session waiting for CCR-T (NEP Gx)
            if(entry.getValue().isWaitingForTermination()){
                continue;
            }
            final String ip = entry.getValue().getIp();
            if (null != ip && ! ip.isEmpty()) {
                clientIpAddresses.add(ip);
            }
        }

        return clientIpAddresses;
    }

    @Override
    public Session querySessionsByIdentity(final String identity) {
        if (null == identity || identity.isEmpty()) {
            return new SessionListImpl(new ArrayList<Session>(1));
        }

        final List<Session> sessions = new ArrayList<>();
        for (final Map.Entry<Key, Session> entry : sessionCache.entrySet()) {
            final Session s = entry.getValue();
            if (null != s.getIdentity() && identity.equals(s.getIdentity())) {
                sessions.add(s);
            }
        }

        return new SessionListImpl(sessions);
    }

    @Override
    public CacheReplicaEndpoint getMyEndpoint() {
        return null;
    }

    @Override
    public boolean getSessionFromCacheReplicaForEvent(final SessionEvent event,
                                                      final String clientIpAddress,
                                                      final long cacheReplicationTimeout) {
    	return false;
    }

    @Override
    public void put(final String clientIpAddress, final Session session) {
        addSessionToCacheByIPAddressAndIdentity(clientIpAddress, null, (SessionImpl) session);
    }

    @Override
    public void evictExpiredElements() {
    }

    @Override
    public void completed(final boolean status, final SessionEvent sessionEvent, final Session session) {
    }

    /*
     * This is the main method that actually creates and caches new sessions,
     * everything else will eventually call this method.
     */
    private Session makeNewSession(final String ipAddress, final String identity) {

        final SessionImpl newSession = new SessionImpl(cacheTTLInMillis);
        newSession.initialise(ipAddress, identity);

        newSession.setMasterPCCAIPAddress("127.0.0.1");

        addSessionToCacheByIPAddressAndIdentity(ipAddress, identity, newSession);

        return newSession;
    }

    private void addSessionToCacheByIPAddressAndIdentity(final String ipAddress,
                                                         final String identity,
                                                         final SessionImpl newSession) {
        final Key key = Key.createKey().withIPAddress(ipAddress).withIdentity(identity);
        addSessionToCache(key, newSession);
    }

    private void addSessionToCache(final Key key, final Session session) {
        final Session previousSession =  sessionCache.put(key, session);

    }

    private Session getSession(final Key key) {
        Session session = sessionCache.get(key);
        if(session != null
                && session.isWaitingForTermination()){
            session = null;
        }
        return session;
    }

    private Session removeByKey(final Key key) {
        final Session removedSession = sessionCache.remove(key);

        return removedSession;
    }

	@Override
	public int getNumReplicatedSessions() {
		// TODO Auto-generated method stub
		return 0;
	}

}
