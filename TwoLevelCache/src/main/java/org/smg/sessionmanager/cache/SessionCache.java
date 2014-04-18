package org.smg.sessionmanager.cache;

import java.util.List;

import org.smg.pcca.selector.CacheReplicaEndpoint;
import org.smg.sessionmanager.events.SessionEvent;

public interface SessionCache extends AutoCloseable {

    /**
     * Creates a new session matching the IP Address from the cache. The caller must release the session
     * back to the cache when finished.
     *
     * @param ipAddress
     * @return The new session.
     */
    Session createSessionWithIP(String ipAddress);

    /**
     * Creates a new session using the client IP address and msisdn as the key.
     *
     * @param ipAddress The client IP address.
     * @param msisdn The client MSISDN.
     *
     * @return The new session.
     */
    Session createSessionWithIPAndMSISDN(String ipAddress, String msisdn);

    /**
     * Removes the session matching the given IP address and MSISDN from the cache.
     *
     * @param ipAddress The client IP address.
     * @param msisdn The client MSISDN.
     */
    void removeSession(String ipAddress, String msisdn);

    /**
     * Removes the session matching the given session instance if it is in the cache.
     *
     * @param session The session to remove
     */
    void removeSession(Session session);

    /**
     * Get the session matching the IP Address from the cache. Caller must release the session
     * back to the cache.
     *
     * @param ipAddress The IP address used to locate the session.
     * @return The session or null if no session exists.
     */
    Session getByIPAddress(String ipAddress);

    /**
     * @see #getByIPAddress(String)
     * This method will return the session even if the waitingForTermination flag has been set 
     * for the session.
     * Note: This method should only be used for a RadiusStartEvent, RadiusStopEvent and
     * GxSessionStopEvent (NEP CCR-T) events.
     *
     * 
     * @param ipAddress The IP address used to locate the session.
     * @return The session or null if no session exists.
     */
    Session getByIPAddressEnforceLookup(final String ipAddress);

    /**
     * Get the session matching the PCRF session id. Caller must release the session
     * back to the cache.
     *
     * @param sessionId The session id used to key the session.
     * @return The session or null if no session exists.
     */
    Session getByPCRFSessionId(final String sessionId);

    /**
     * Get the session matching the NEP session id. Caller must release the session
     * back to the cache.
     *
     * @param nepSessionId The session id used to key the session.
     * @return The session or null if no session exists.
     */
    Session getByNEPSessionId(final String nepSessionId);
    /**
     * Get the number of elements in the cache.
     *
     * @return Type integer - number of elements in the cache.
     */
    long getNumCacheElements();
    
    /**
     * Get the number of replicated sessions in the cache.
     *
     * @return Type integer - number of replicated sessions in the cache.
     */
    int getNumReplicatedSessions();

    /**
     * Insert a new session into the cache. This method will be invoked
     * when a session is pushed to the cache replication client.
     *
     * @param session The session object to be cached.
     */
    void replicateSession(Session session);

    /**
     * Manually remove a session from the cache using the key provided.
     *
     * @param key The key used to index the session.
     *
     * @return the session that was removed, or null if no session was removed.
     */
    Session replicateSessionRemoval(Key key);

    void pushSessionToRemotePCCA(Session session);

    /**
     * Sends a message to the replica node instructing it to remove the session.
     * @param session The session to be removed from the replica PCCA instance.
     */
    void removeSessionFromRemotePCCA(Session session);

    /**
     * Returns a list of all the client IP addresses currently held within the session cache.
     * @return The list of client IP addresses.
     */
    List<String> getAllClientIPAddresses();

    /**
     * This will query the session cache for sessions that match the identity.
     *
     * @param identity The identity to query with.
     *
     * @return 	A list of sessions that all have the same identity (the list should only
     * 			contain at most one session), or null if the query fails.
     */
    Session querySessionsByIdentity(String ipAddress);

    /**
     * The endpoint in which the cache replication server is listening. This details the IP address and port.
     * @return The listening endpoint.
     */
    CacheReplicaEndpoint getMyEndpoint();

    /**
     * This method will query the replica cache server for the clients session.
     * A null session can be returned if the lookup on the replica server fails,
     * or if there are no replica servers available.
     *
     * @param event The event associated with this query.
     *
     * @param clientIpAddress The client IP address.
     *
     * @param cacheReplicationTimeout The timeout in milli seconds on the cache query.
     */
    boolean getSessionFromCacheReplicaForEvent(SessionEvent event, String clientIpAddress, long cacheReplicationTimeout);

    /**
     * Stores a new session in the cached, keyed by the client IP address.
     * 
     * @param clientIpAddress The client IP address.
     * 
     * @param session The Session object being cached.
     * 
     * @see Session
     */
    void put(String clientIpAddress, Session session);

    /**
     * This will remove any Session objects from the cache that are deemed to be expired,
     * based on the TTL associated with each session. A <it>SessionExpired</it> event will
     * be triggered for each session that is expired.
     */
    void evictExpiredElements();
}
