package com.openwave.sessionmanager.cache;

public abstract class ServiceData{

    public enum Type { DATA, TIME, UNKNOWN };

    public static long NOTEXISTS = -1L;
    
    private String resourceName;
    
    public final void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    public final String getResourceName() {
        if (resourceName == null) {
            return createResourceName(getRatingGroup(), getServiceIdentifier());
        } else {
            return resourceName;
        }
    }
    
    public static String createResourceName(long ratingGroup, long serviceIdentifier) {
        return (ratingGroup + "/" + serviceIdentifier);
    }
    
    public Type getType() {
        return Type.UNKNOWN;
    }

    public abstract long getRatingGroup();
    public abstract long getServiceIdentifier();

    public abstract long getGrantedCCTotalOctets();
    public abstract void setGrantedCCTotalOctets(long ccTotalOctets);
    public abstract long getUsedCCTotalOctets();
    public abstract void setUsedCCTotalOctets(long totalOctets);

    public abstract long getGrantedCCTime();
    public abstract void setGrantedCCTime(long ccTime);
    public abstract long getUsedCCTime(); 
    public abstract void setUsedCCTime(long time);

    public abstract int getValidityTime();
    public abstract void setValidityTime(int validityTime);

    public abstract int getVolumeQuotaThreshold();
    public abstract void setVolumeQuotaThreshold(int volumeQuotaThreshold);

    public abstract void setQuotaHoldingTime(int quotaHoldingTime);
    public abstract int getQuotaHoldingTime();

    public abstract int getTimeQuotaThreshold();
    public abstract void setTimeQuotaThreshold(int timeQuotaThreshold);

    public abstract long getResultCode();
    public abstract void setResultCode(long resultCode);

    /*
     * get/set functions for the Gx interface specific data.
     * 
     * GxServiceData is applicable for deployments with NEPs which send usage data
     * on the Gx interface.
     */
    public abstract GxServiceData getGxServiceData();
    public abstract void setGxServiceData(GxServiceData gxServiceData);
    
    @Override
    public int hashCode() {
        return getResourceName().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (!(obj instanceof ServiceData))) {
            return false;
        }
        ServiceData other = (ServiceData)obj;
        if (other.getType() != this.getType()) {
            return false;
        }
        
        String otherName = other.getResourceName();
        String thisName = this.getResourceName();
        if ((otherName == null) && (thisName == null)) {
            return true;
        }
        if ((otherName == null) || (thisName == null)) {
            return false;
        }
        return otherName.equals(thisName);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Type [" + getType() + "] ");
        sb.append("Name [" + resourceName + "] ");
        sb.append("Rating Group [" + getRatingGroup() + "] ");
        sb.append("Service ID [" + getServiceIdentifier() + "] ");
        sb.append("Granted Octets [" + getGrantedCCTotalOctets() + "] ");
        sb.append("Used Octets [" + getUsedCCTotalOctets() + "] ");
        sb.append("Granted Time [" + getGrantedCCTime() + "] ");
        sb.append("Used Time [" + getUsedCCTime() + "] ");
        sb.append("Validity Time [" + getValidityTime() + "] ");
        sb.append("Volume Quota Threshold [" + getVolumeQuotaThreshold() + "] ");
        sb.append("Quota Holding Time [" + getQuotaHoldingTime() + "] ");
        sb.append("Time Quota Threshold [" + getTimeQuotaThreshold() + "] ");
        sb.append("Result Code [" + getResultCode() + "]");
        
        return sb.toString();
    }
}
