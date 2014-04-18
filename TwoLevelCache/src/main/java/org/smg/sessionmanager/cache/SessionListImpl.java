package org.smg.sessionmanager.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smg.policy.Policy;
import org.smg.sessionmanager.cache.radius.Identity;

public class SessionListImpl implements Session {

	private List<Session> sessionList = new ArrayList<>();
	
	public SessionListImpl(List<Session> sessionList){
		this.sessionList = sessionList;
	}

	@Override
	public List<Session> getSessionsList() {
		return sessionList;
	}

	@Override
	public void start() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void stop() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getIp() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SessionState getState() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getLength() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getDuration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getStartTime() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStartTime(final long startTime) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getStopTime() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void incrementDuration(long duration) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSessionUpdateRequired(boolean enabled) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSessionUpdateRequired() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSessionExpired(boolean expired) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSessionExpired() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Policy getPolicy() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPolicy(Policy policy) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearPolicy() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Identity getRadiusIdentity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRadiusIdentity(Identity radiusIdentity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearRadiusIdentity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIdentity(String identity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getIdentity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public AuthorisationStatus getAuthorisationStatus() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAuthorisationStatus(AuthorisationStatus authorisationStatus) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCached() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIsNotCached() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIsCached() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCacheReplica() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIsCacheReplica(boolean isCacheReplica) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getIntegraChannelId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIntegraChannelId(int channelId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getIntegraIPAddress() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIntegraIPAddress(String ipAddress) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getIntegraSessionId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIntegraSessionId(String integraSessionId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, String> getSubscriberAttributes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSubscriberAttributes(Map<String, String> attributes) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Key getKey() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IdentitySource getIdentitySource() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMasterPCCAIPAddress(String masterPCCAIPAddress) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getMasterPCCAIPAddress() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getCacheTTLInMS() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStartReason(StartReason startReason) {
		throw new UnsupportedOperationException();
	}

	@Override
	public StartReason getStartReason() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStopReason(StopReason stopReason) {
		throw new UnsupportedOperationException();
	}

	@Override
	public StopReason getStopReason() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPolicyOnlyAddedForImplicitStop() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPolicyOnlyAddedForImplicitStop(
			boolean policyOnlyAddedForImplicitStop) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<IdentitySource> getIdentitySources() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addServiceData(ServiceData serviceData) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public ServiceData removeServiceData(String resourceName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServiceData getServiceData(String resourceName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServiceData getServiceData(long ratingGroup, long serviceIdentifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getServiceDataResourceNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setNepSessionId(String nepSessionId) {
		throw new UnsupportedOperationException();	
	}

	@Override
	public String getNepSessionId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setNepContext(NepContext nepContext) {
		throw new UnsupportedOperationException();	
	}

	@Override
	public NepContext getNepContext() {
		throw new UnsupportedOperationException();
	}

    @Override
    public void setInvalidPdpContext(boolean invalidPdpContext) {
        throw new UnsupportedOperationException();   
    }

    @Override
    public boolean isInvalidPdpContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWaitingForTermination(boolean waitForTermination) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWaitingForTermination() {
        throw new UnsupportedOperationException();
    }
}
