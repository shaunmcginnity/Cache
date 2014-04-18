package com.openwave.sessionmanager.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import com.openwave.policy.Policy;
import com.openwave.sessionmanager.cache.radius.Identity;
import com.openwave.sessionmanager.config.IdentityControlConfiguration;
import com.openwave.spi.DataDictionary;

@SuppressWarnings("serial")
public class SessionImpl extends DataDictionary implements Session {

    private static Set<String> LOCAL_KEY_SET = new TreeSet<>();
    static {
        LOCAL_KEY_SET.addAll(Arrays.asList(new String []{START_TIME,
                                                         STOP_TIME,
                                                         DURATION,
                                                         UPDATE_REQUIRED,
                                                         EXPIRED }));
    }

    private static IdentityControlConfiguration identityControlConfiguration;

    // The client IP address.
    private String ipAddress;

    // SessionId sent by the NEP for this subscriber session
    private String nepSessionId;

    // NEP information from which the subscrier session is initiated
    private NepContext nepContext;

    // The client identity that was sent from Integra.
    private String identity;

    // The hashed id.
    private long id;

    /*
     * This is the IP address of the primary PCCA instance which is
     * obtained by using the using algorithm on the client IP address.
     * This will be used by PCCA replication and re-sync when they restart.
     */
    private String masterPCCAIPAddress;

    private long duration = 0;
    private long startTime = 0;
    private long stopTime = 0;
    private boolean updateRequired = false;
    private boolean expired = false;

    private Policy policy = null;
    
    private boolean policyOnlyAddedForImplicitStop = false;

    private Identity radiusIdentity = null;

    private AuthorisationStatus authorisationStatus = null;

    // The IP address of the Integra instance that initiated the session.
    private String integraIPAddress;

    // The cached identity has been determined by looking at the identity sources
    // in the order that has been configured
    private String sessionIdentity = null;
    private IdentitySource sessionIdentitySource = IdentitySource.NONE;

    // DO NOT REPLICATE!
    private int integraChannelId = -1;
    private String integraSessionId = null;

    private SessionState state = SessionState.NULL;

    private Map<String, String> subscriberAttributes = null;

    private boolean isCached = false;
    private boolean isCacheReplica = false;

    private final long cacheTTLInMillis;
       
    private StartReason startReason;
    
    private StopReason stopReason;

    private boolean waitingForTermination = false;
    
    //private Map<Long, Collection<ServiceData>> gyServiceCollection = null;
    private Map<String, ServiceData> serviceDataCollection;

    private boolean invalidPdpContext;
    
    public SessionImpl() {
    	
    	this.cacheTTLInMillis = 0;
    }
    
    public SessionImpl(final long cacheTTLInMillis) {
        this.cacheTTLInMillis = cacheTTLInMillis;
    }

    /**
     * Create a new session.
     *
     * @param ip The client IP address.
     * @param startTime The time when the session was created.
     * @param stopTime When the session stopped.
     * @param id The hashed id.
     */
    public SessionImpl(final String ip,
                       final long startTime,
                       final long stopTime,
                       final long id,
                       final long cacheTTLInMillis) {
        this.cacheTTLInMillis = cacheTTLInMillis;

        ipAddress = ip;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.id = id;
    }

    /**
     * Initialises the session.
     * 
     * @param ip 		the ip address of the session that is being initialised
     */
    public void initialise(final String ip) {
        initialise(ip, null);
    }

    /**
     * Initialises the session.
     * 
     * @param ip 		the ip address of the session that is being initialised
     * @param msisdn	the identity of the session that is being initialised.
     * 					This should only be the identity that was sent from 
     * 					Integra (e.g. via the Identity Header). Any other types 
     * 					of identity, for example from RADIUS or a PCRF, should
     * 					not be passed in here, but set instead using the 
     * 					appropriate methods on this class. For example, 
     * 					{@link #setRadiusIdentity(Identity)} or
     * 					{@link #setPolicy(Policy)} 
     */
    public void initialise(final String ip, final String msisdn) {
        // Only required when we use object pooling.
        //clear();

        startTime = System.currentTimeMillis();
        stopTime = 0;

        ipAddress = ip;
        identity = msisdn;
        id = startTime + ip.hashCode();

        state = SessionState.NULL;

        isCached = true;
    }

    @Override
    public String getIp() {
        return ipAddress;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public SessionState getState() {
        return state;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(final long startTime) {
        if (this.startTime != startTime) {
            this.startTime = startTime;
        }
    }

    @Override
    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(final long stopTime) {
        if (this.stopTime != stopTime) {
            this.stopTime = stopTime;
        }
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public void setDuration(final long duration) {
        if (this.duration != duration) {
            this.duration = duration;
        }
    }

    @Override
    public void incrementDuration(final long incrementDuration) {
        if (incrementDuration != 0) {
            duration += incrementDuration;
        }
    }

    @Override
    public long getLength() {
        long length = 0;
        if (stopTime >= startTime) {
            length = stopTime - startTime;
        }
        return length;
    }

    @Override
    public synchronized void start() {
        if (state == SessionState.NULL) {
            state = SessionState.STARTED;
        }
    }

    @Override
    public synchronized void stop() {
        if (state != SessionState.STOPPED) {
            setStopTime(System.currentTimeMillis());
            state = SessionState.STOPPED;
        }
    }

    @Override
    public void clear() {
        super.clear();

        duration = 0;
        updateRequired = false;
        expired = false;

        ipAddress = null;
        identity = null;
        id = 0;
        masterPCCAIPAddress = null;

        authorisationStatus = null;
        integraIPAddress = null;
        integraChannelId = -1;

        sessionIdentity = null;
        sessionIdentitySource = IdentitySource.NONE;

        state = SessionState.NULL;

        policy = null;
        radiusIdentity = null;

        if (null != subscriberAttributes) {
            subscriberAttributes.clear();
        }

        isCached = false;
        isCacheReplica = false;

        if (null != serviceDataCollection) {
            serviceDataCollection.clear();
    }
    }

    @Override
    public int size() {
        return LOCAL_KEY_SET.size() + super.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Set<String> keySet() {
        final TreeSet<String> keys = new TreeSet<String>(LOCAL_KEY_SET);
        keys.addAll(super.keySet());
        return keys;
    }

    @Override
    public boolean containsKey(final Object key) {
        if (LOCAL_KEY_SET.contains(key)) {
            return true;
        }
        return super.containsKey(key);
    }

    @Override
    public Object get(final Object key) {
        if (key.equals(STOP_TIME)) {
            return stopTime;
        }
        if (key.equals(START_TIME)) {
            return startTime;
        }
        if (key.equals(DURATION)) {
            return duration;
        }
        if (key.equals(UPDATE_REQUIRED)) {
            return updateRequired;
        }
        if (key.equals(EXPIRED)) {
            return expired;
        }
        return super.get(key);
    }

    @Override
    public Object put(final String key, final Object value) {
        if (LOCAL_KEY_SET.contains(key)) {
            throw new IllegalArgumentException("Not allowed");
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends Object> map) {
        if (map.containsKey(START_TIME) ||
                map.containsKey(STOP_TIME) ||
                map.containsKey(DURATION) ||
                map.containsKey(UPDATE_REQUIRED) ||
                map.containsKey(EXPIRED)) {
            throw new IllegalArgumentException("Not allowed");
        }
        super.putAll(map);
    }

    @Override
    public Object remove(final Object key) {
        if (key.equals(START_TIME) ||
                key.equals(STOP_TIME) ||
                key.equals(DURATION) ||
                key.equals(UPDATE_REQUIRED) ||
                key.equals(EXPIRED)) {
            throw new IllegalArgumentException("Not allowed");
        }
        return super.remove(key);
    }

    @Override
    public boolean isSessionUpdateRequired() {
        return updateRequired;
    }

    @Override
    public void setSessionUpdateRequired(final boolean enabled) {
        updateRequired = enabled;
    }

    @Override
    public void setSessionExpired(final boolean expired) {
        this.expired = expired;
    }

    @Override
    public boolean isSessionExpired() {
        return expired;
    }

    @Override
    public void setPolicy(final Policy policy) {
        this.policy = policy;
    }

    @Override
    public void clearPolicy() {
        policy = null;
    }

    @Override
    public Policy getPolicy() {
        return policy;
    }
    
    @Override
    public boolean isPolicyOnlyAddedForImplicitStop() {
        return policyOnlyAddedForImplicitStop;
    }
    
    @Override
    public void setPolicyOnlyAddedForImplicitStop(
            final boolean policyOnlyAddedForImplicitStop) {
        this.policyOnlyAddedForImplicitStop 
            = policyOnlyAddedForImplicitStop;
    }

    @Override
    public Identity getRadiusIdentity() {
        return radiusIdentity;
    }

    @Override
    public void setRadiusIdentity(final Identity radiusIdentity) {
        this.radiusIdentity = radiusIdentity;
    }

    @Override
    public void clearRadiusIdentity() {
        radiusIdentity = null;
    }

    @Override
	public AuthorisationStatus getAuthorisationStatus() {
		return authorisationStatus;
	}

	@Override
	public void setAuthorisationStatus(final AuthorisationStatus authorisationStatus) {
		this.authorisationStatus = authorisationStatus;

	}

    @Override
    public Collection<Object> values() {
        throw new UnsupportedOperationException("Method not allowed");
    }

    @Override
    public boolean isCached() {
        return isCached;
    }

    @Override
    public void setIsNotCached() {
        isCached = false;
    }

    @Override
    public void setIsCached() {
        isCached = true;
    }

    @Override
    public boolean isCacheReplica() {
        return isCacheReplica;
    }

    @Override
    public void setIsCacheReplica(final boolean isCacheReplica) {
        this.isCacheReplica = isCacheReplica;
    }

    @Override
    public int getIntegraChannelId() {
        return integraChannelId;
    }

    @Override
    public void setIntegraChannelId(final int integraChannelId) {
        this.integraChannelId = integraChannelId;
    }

    @Override
    public String getIntegraSessionId() {
        return integraSessionId;
    }

    @Override
    public String getIntegraIPAddress() {
        return integraIPAddress;
    }

    @Override
    public void setIntegraIPAddress(final String ipAddress) {
        integraIPAddress = ipAddress;
    }

    @Override
    public void setIntegraSessionId(final String integraSessionId) {
        this.integraSessionId = integraSessionId;
    }

    @Override
    public Key getKey() {
        return Key.createKey().withIPAddress(ipAddress);
    }

    @Override
    public Map<String, String> getSubscriberAttributes() {
        return subscriberAttributes;
    }

    @Override
    public void setSubscriberAttributes(final Map<String, String> attributes) {
        subscriberAttributes = attributes;
    }

    @Override
    public String getIdentity() {
        determineIdentity();
        return sessionIdentity;
    }

    private void determineIdentity() {

        if (identityControlConfiguration != null) {
            final List<IdentitySource> identitySources = identityControlConfiguration.getIdentitySources();
            if (identitySources != null) {

                for (final IdentitySource identitySource: identitySources) {

                    switch(identitySource) {
                    case INTEGRA:
                        if ( (identity != null) && (! identity.isEmpty()) ) {
                            sessionIdentity = identity;
                            sessionIdentitySource = IdentitySource.INTEGRA;
                        }
                        break;

                    case PCRF:
                        if (policy != null) {
                            final String pcrfIdentity = policy.getIdentity();
                            if (pcrfIdentity != null) {
                                sessionIdentity = pcrfIdentity;
                                sessionIdentitySource = IdentitySource.PCRF;
                            }
                        }
                        break;

                    case RADIUS_DEVICE_ID:
                        if (radiusIdentity != null) {
                            final String deviceId = radiusIdentity.getDeviceId();
                            if (deviceId != null) {
                                sessionIdentity = deviceId;
                                sessionIdentitySource = IdentitySource.RADIUS_DEVICE_ID;
                            }
                        }
                        break;

                    case RADIUS_USERNAME:
                        if (radiusIdentity != null) {
                            final String username = radiusIdentity.getUserName();
                            if (username != null) {
                                sessionIdentity = username;
                                sessionIdentitySource = IdentitySource.RADIUS_USERNAME;
                            }
                        }
                        break;

                    case NONE:
                    default:
                        break;

                    }

                    if (sessionIdentity != null) {
                        return;
                    }

                }
            }
        }
    }

    @Override
    public IdentitySource getIdentitySource() {

        if (sessionIdentitySource == IdentitySource.NONE) {
            determineIdentity();
        }

        return sessionIdentitySource;
    }
    
    @Override
    public List<IdentitySource> getIdentitySources() {
        if (identityControlConfiguration != null) {
            return identityControlConfiguration.getIdentitySources();
        }
        else {
            return null;
        }
    }

    @Override
    public void setIdentity(final String identity) {
        this.identity = identity;
    }

    public static void setIdentityControlConfiguration(final IdentityControlConfiguration identityControlConfiguration) {
        SessionImpl.identityControlConfiguration = identityControlConfiguration;
    }

    @Override
    public void setMasterPCCAIPAddress(final String masterPCCAIPAddress) {
        this.masterPCCAIPAddress = masterPCCAIPAddress;
    }

    @Override
    public String getMasterPCCAIPAddress() {
        return masterPCCAIPAddress;
    }

    @Override
    public long getCacheTTLInMS() {
        return cacheTTLInMillis;
    }

    @Override
    public void addServiceData(ServiceData serviceData) {
        if (serviceDataCollection == null) {
            serviceDataCollection = new HashMap<>();
        }
        
        serviceDataCollection.put(serviceData.getResourceName(), serviceData);
    }
    
    @Override
    public ServiceData getServiceData(long ratingGroup, long serviceIdentifier) {
        return getServiceData(ServiceData.createResourceName(ratingGroup, serviceIdentifier));
    }
    
    @Override
    public ServiceData getServiceData(String resourceName) {
        if (serviceDataCollection == null) {
            return null;
        }
        
        return serviceDataCollection.get(resourceName);
    }
    
    @Override
    public ServiceData removeServiceData(String resourceName) {
        if (serviceDataCollection == null) {
            return null;
        }

        return serviceDataCollection.remove(resourceName);
    }
    
    @Override
    public Set<String> getServiceDataResourceNames() {
        if (serviceDataCollection == null) {
            return null;
        }

        return serviceDataCollection.keySet();
    }
    


    @Override
    public void setStartReason(final StartReason startReason) {
    	this.startReason = startReason;
    }
    
    @Override
	public StartReason getStartReason() {
		return startReason;
	}

    @Override
	public void setStopReason(final StopReason stopReason) {
    	this.stopReason = stopReason;
    }
	
    @Override
	public StopReason getStopReason() {
		return stopReason;
	}

	@Override
	public List<Session> getSessionsList() {
		final List<Session> sessionList = new ArrayList<>(1);
		sessionList.add(this);
		return sessionList;
	}

    @Override
    public void setNepSessionId(final String nepSessionId) {
        this.nepSessionId = nepSessionId;
    }

    @Override
    public String getNepSessionId() {
        return nepSessionId;
	}

    @Override
    public void setNepContext(final NepContext nepContext) {
        if (nepContext != null) {
            this.nepContext = nepContext;
        }
    }

    @Override
    public NepContext getNepContext() {
        return this.nepContext;
    }

    @Override
    public void setInvalidPdpContext(final boolean invalidPdpContext) {
        this.invalidPdpContext = invalidPdpContext;
    }

    @Override
    public boolean isInvalidPdpContext() {
        return invalidPdpContext;
    }

    @Override
    public void setWaitingForTermination(final boolean waitForTermination){
        this.waitingForTermination = waitForTermination;
    }

    @Override
    public boolean isWaitingForTermination(){
        return waitingForTermination;
    }
}
