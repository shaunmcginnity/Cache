package org.smg.sessionmanager.cache;

public class ServiceDataImpl extends ServiceData {
    
    private long ratingGroup;
    private long serviceIdentifier;
    private long grantedTotalOctets;
    private long usedTotalOctets;
    private long grantedTime;
    private long usedTime;
    private int validityTime;
    private int volumeQuotaThreshold;
    private int quotaHoldingTime;
    private int timeQuotaThreshold;
    private long resultCode;
    private GxServiceData gxServiceData = null;

    private final Type type;
    
    public ServiceDataImpl(final String resourceName, final Type type) {
        this.type = type;
        setResourceName(resourceName);
    }
    
    @Override 
    public Type getType() {
        return type;
    }

    @Override
    public long getRatingGroup() {
        return ratingGroup;
    }
    
    public void setRatingGroup(long ratingGroup) {
        this.ratingGroup = ratingGroup;
    }

    @Override
    public long getServiceIdentifier() {
        return serviceIdentifier;
    }
    
    public void setServiceIdentifier(long serviceIdentifier) {
        this.serviceIdentifier = serviceIdentifier;
    }

    @Override
    public long getGrantedCCTotalOctets() {
        return grantedTotalOctets;
    }

    @Override
    public void setGrantedCCTotalOctets(long ccTotalOctets) {
        this.grantedTotalOctets = ccTotalOctets;
    }

    @Override
    public long getUsedCCTotalOctets() {
        return usedTotalOctets;
    }

    @Override
    public void setUsedCCTotalOctets(long totalOctets) {
        this.usedTotalOctets = totalOctets;
    }

    @Override
    public long getGrantedCCTime() {
        return grantedTime;
    }

    @Override
    public void setGrantedCCTime(long ccTime) {
        this.grantedTime = ccTime;
    }

    @Override
    public long getUsedCCTime() {
        return usedTime;
    }

    @Override
    public void setUsedCCTime(long time) {
        this.usedTime = time;
    }

    @Override
    public int getValidityTime() {
        return validityTime;
    }

    @Override
    public void setValidityTime(int validityTime) {
        this.validityTime = validityTime;
    }

    @Override
    public int getVolumeQuotaThreshold() {
        return volumeQuotaThreshold;
    }

    @Override
    public void setVolumeQuotaThreshold(int volumeQuotaThreshold) {
        this.volumeQuotaThreshold = volumeQuotaThreshold;
    }

    @Override
    public void setQuotaHoldingTime(int quotaHoldingTime) {
        this.quotaHoldingTime = quotaHoldingTime;
    }

    @Override
    public int getQuotaHoldingTime() {
        return quotaHoldingTime;
    }

    @Override
    public int getTimeQuotaThreshold() {
        return timeQuotaThreshold;
    }

    @Override
    public void setTimeQuotaThreshold(int timeQuotaThreshold) {
        this.timeQuotaThreshold = timeQuotaThreshold;
    }

    @Override
    public long getResultCode() {
        return resultCode;
    }

    @Override
    public void setResultCode(long resultCode) {
        this.resultCode = resultCode;

    }

    @Override
    public GxServiceData getGxServiceData() {
        return gxServiceData;
    }

    @Override
    public void setGxServiceData(GxServiceData gxServiceData) {
        this.gxServiceData = gxServiceData;
    }

}
