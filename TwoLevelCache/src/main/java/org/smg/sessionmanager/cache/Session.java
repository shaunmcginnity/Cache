package org.smg.sessionmanager.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smg.policy.Policy;
import org.smg.sessionmanager.cache.radius.Identity;

/**
 * Session interface.
 *
 * @author David McCauley
 */
public interface Session {

    /**
     * The reason this Session was started
     */
    static enum StartReason { 
        NETWORK_START, 
        DATA_START, 
        TXN, 
        INTEGRA_DATA_START, 
        RADIUS_START, 
        RADIUS_INTERIM_UPDATE 
    }

    /**
     * The reason this Session was stopped
     */
    static enum StopReason { 
        NETWORK_STOP, 
        DATA_STOP, 
        EXPIRY, 
        NEW_SESSION, 
        RADIUS_STOP, 
        INVALID_PDP_CONTEXT 
    }

    public static final String DURATION = "duration";
    public static final String START_TIME = "startTime";
    public static final String STOP_TIME = "stopTime";
    public static final String UPDATE_REQUIRED = "update";
    public static final String EXPIRED = "expired";

    void start();
    void stop();

    /**
     * @return The client IP address.
     */
    String getIp();

    /**
     * @return The unique session id.
     */
    long getId();

    /**
     * @return The current session state.
     * 
     * @see SessionState
     */
    SessionState getState();

    long getLength();
    long getDuration();
    
    long getStartTime();
    /**
     * Set function for the start time
     *
     * This is needed for the RadiusInterimUpdates, with same Identity/IP.  
     *
     * @param startTime session start time, which needs to be updated
     * 
     */
    void setStartTime(long startTime);

    long getStopTime();
    void incrementDuration(long duration);

    void setSessionUpdateRequired(boolean enabled);
    boolean isSessionUpdateRequired();
    void setSessionExpired(boolean expired);
    boolean isSessionExpired();

    /**
     * Returns the PCRF policy for this particular client.
     *
     * @return The PCRF policy, or null if there is no policy available.
     * 
     * @see Policy
     */
    Policy getPolicy();
    
    /**
     * Attachs a PCRF policy to the users session.
     *
     * @param policy The PCRF policy.
     * 
     * @see Policy
     */
    void setPolicy(Policy policy);
    
    /**
     * Removes the existing PCRF policy from this session.
     */
    void clearPolicy();

    /**
     * @return The RADIUS identity.
     * 
     * @see Identity
     */
    Identity getRadiusIdentity();
    
    /**
     * Attachs the RADIUS identity to the existing Session.
     *
     * @param radiusIdentity The RADIUS identity to be attached to this session.
     *
     * @see Identity
     */
    void setRadiusIdentity(Identity radiusIdentity);
    
    /**
     * Removes any RADIUS <tt>Identity</tt> from the session.
     */
    void clearRadiusIdentity();

    /**
     * Sets the <b><i>Integra</i></b> identity (i.e. the identity that was sent
     * from Integra, e.g. via the Identity Header) for this session.
     * 
     * @param identity	the identity that was sent from Integra for this session
     */
    void setIdentity(String identity);

    /**
     * Returns the final determined client identity for this session.
     * 
     * <p>The client identity can come from multiple sources, such as Integra 
     * (via the Identity Header), a PCRF server, or Radius (either the Radius 
     * Device ID or Radius Username can be used as a form of client 
     * identification). OAM configuration is used to determine which of these 
     * client identity sources will be used by the PCC-A for each session, and 
     * in what priority these identity sources will be used (in the case where
     * an identity has been provided by more than one source).
     * 
     * <p>This method uses this OAM configuration and the presence or absence of 
     * multiple identities (e.g. it could be possible that the session contains
     * an identity from Integra as well as an identity from RADIUS or a PCRF)
     * to determine which one of these identities will ultimately be used for
     * the session.
     * 
     * @return	the client identity that will ultimately be used for this 
     * 			session
     */
    String getIdentity();

    /**
     * @return The current authorisation status for this session.
     * @see AuthorisationStatus
     */
    AuthorisationStatus getAuthorisationStatus();
    
    /**
     * Attachs an AuthorisationStatus to the existing session.
     * 
     * @param authorisationStatus The AuthorisationStatus being attached to this session.
     */
    void setAuthorisationStatus(AuthorisationStatus authorisationStatus);

    /**
     * @return True if this session is being cached.
     */
    boolean isCached();

    /**
     * We use this to indicate that the session is no longer being cached.
     */
    void setIsNotCached();

    /**
     * Used to indicate that the session is being cached.
     */
    void setIsCached();

    /**
     * @return True if this session has been replicated from another cache.
     */
    boolean isCacheReplica();

    /**
     * Indicates whether the session is a copy replicated from a remote cache.
     *
     * @param isCacheReplica
     */
    void setIsCacheReplica(boolean isCacheReplica);

    /**
     * @return The channel id for the integra instance.
     */
    int getIntegraChannelId();
    void setIntegraChannelId(int channelId);

    /**
     * @return The IP address of the Integra instance that initiated the session.
     */
    String getIntegraIPAddress();
    
    /**
     * Sets the IP address of the Integra instance that initiated the session.
     *
     * @param ipAddress The IP address of the Integra instance that intiated the session.
     */
    void setIntegraIPAddress(String ipAddress);

    /**
     * @return The Integra session id.
     */
    String getIntegraSessionId();
    
    /**
     * Attachs the Integra session id to the session.
     * @param integraSessionId The Integra session id.
     */
    void setIntegraSessionId(String integraSessionId);

    /**
     * @return LDAP subscriber data as key-value-pairs stored in a <tt>Map</tt>.
     */
    Map<String, String> getSubscriberAttributes();
    
    /**
     * @param attributes Adds the LDAP subscriber data to the existing subscriber session.
     */
    void setSubscriberAttributes(Map<String, String> attributes);

    /**
     * @return The cache <tt>Key</tt> for this particular session.
     * 
     * @see Key
     */
    Key getKey();

    /**
     * @return The source of the subscriber identity.
     * 
     * @see IdentitySource
     */
    IdentitySource getIdentitySource();

    void setMasterPCCAIPAddress(final String masterPCCAIPAddress);

    String getMasterPCCAIPAddress();

    /**
     * This returns the session TTL in milli seconds.
     * @return The TTL.
     */
    long getCacheTTLInMS();

    /**
     * @param startReason  the reason for starting this season, may be null
     */
    public void setStartReason(final StartReason startReason);

    /**
     * @return the reason for starting this session, or null if this has not 
     * been set
     */
    public StartReason getStartReason();

    /**
     * @param stopReason  the reason for stopping this session, may be null
     */
    public void setStopReason(final StopReason stopReason);

    /**
     * @return the reason for stopping this session, or null if this has not 
     * been set
     */
    public StopReason getStopReason();

    /**
     * @return true if the only reason a policy was added to this session was to 
     * facilitate an implicit stop being sent to a PCRF server, false otherwise
     */
    boolean isPolicyOnlyAddedForImplicitStop();

    /**
     * @param policyOnlyAddedForImplicitStop should be true if the 
     * only reason a policy was added to this session was to facilitate an 
     * implicit stop being sent to a PCRF server, false otherwise
     */
    void setPolicyOnlyAddedForImplicitStop(
            final boolean policyOnlyAddedForImplicitStop);
    
    /**
     * @return a collection of valid possible sources of identity for this 
     * session
     */
    List<IdentitySource> getIdentitySources();
    
    /**
     * @return a collection of sessions. This method will be implemented only 
     * if the implementation calls has a list of sessions rather than a singel
     * entry
     */
    List<Session> getSessionsList();

     	/** Adds/removes/gets service data information 
     	*/
    	void addServiceData(ServiceData serviceData);
    	ServiceData removeServiceData(String resourceName);
    	ServiceData getServiceData(String resourceName);
    	ServiceData getServiceData(long ratingGroup, long serviceIdentifier);
    	Set<String> getServiceDataResourceNames();

    /**
     * @param nepSessionId  session id sent by the NEP for this subscriber session
     *
     */
    public void setNepSessionId(String nepSessionId);

    /**
     * @return returns the session id sent by the NEP for this subscriber session
     *  
     */
    public String getNepSessionId();

    /**
     * @param nepContext nepContext from which the subscriber session is initiated 
     *
     */
    public void setNepContext(final NepContext nepContext);

    /**
     * @return returns the nep context for the subscriber session 
     *  
     */
    public NepContext getNepContext();
    
    /**
     * Set whether spurious traffic from a mobile device after the device
     * has detached has caused a Diameter error due to there being no 
     * open PDP context.  
     * 
     * @param invalidPdpContext should be set to true if the PCRF responded 
     * to a CCR-I with a CCA-I containing a non-successful (i.e. anything 
     * other than a code in the range 2xxx) result code, false otherwise.
     * <p>
     * If true, this indicates that the event workflow should be stopped (i.e. 
     * no more SEs invoked) after the PCRF SE completes, and that the session 
     * should be removed from the PCC-A session cache.
     * <p>
     * In addition, in the case that a non-successful result code was received
     * during an {@link IntegraDataStartEvent}, then Integra should also be
     * informed that a session was not created. 
     * <p>
     */
    public void setInvalidPdpContext(final boolean invalidPdpContext);

    /**
     * Returns whether spurious traffic from a mobile device after the device
     * has detached has caused a Diameter error due to there being no 
     * open PDP context.  
     * 
     * @return true if the PCRF responded to a CCR-I with a CCA-I containing a 
     * non-successful (i.e. anything other than a code in the range 2xxx) 
     * result code, false otherwise.
     * <p>
     * If true, this indicates that the event workflow should be stopped (i.e. 
     * no more SEs invoked) after the PCRF SE completes, and that the session 
     * should be removed from the PCC-A session cache.
     * <p>
     * In addition, in the case that a non-successful result code was received
     * during an {@link IntegraDataStartEvent}, then Integra should also be
     * informed that a session was not created. 
     * <p>
     */
    public boolean isInvalidPdpContext();

    /**
     * Set whether the session entry is waiting for termination.
     * When a RAR-T is sent to the NEP (F5 11.4) it may take more than a few 
     * seconds to generate a CCR-T. We need to keep this session so we can 
     * report any usage sent in CCR-T message. If the flag is true the session
     * will not be queryable.
     * 
     * @param waitForTermination true if an RAR-T has been sent to the NEP for 
     * this session, false otherwise.
     */
    public void setWaitingForTermination(final boolean waitForTermination);

    /**
     * @see #setWaitingForTermination(boolean)
     * 
     * @return true if an RAR-T has been sent to the NEP for this session, 
     * false otherwise.
     */
    public boolean isWaitingForTermination();
}
