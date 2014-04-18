package org.smg.sessionmanager.cache;

public abstract class GxServiceData{

    // pccRule will be set SESSION_LEVEL for session level monitoring information
    public static final String SESSION_LEVEL = "SessionLevelUsageMonitoring";

    public enum UsageMonitoringLevel {SESSION_LEVEL, PCC_RULE_LEVEL, INVALID_LEVEL};
   
    /*
     * Total Octets granted to the NEP Session initiated on the Gx interface.
     * 
     */ 
    public abstract long getGrantedCCTotalOctets();
    public abstract void setGrantedCCTotalOctets(long ccTotalOctets);

    /*
     * Total Octets used by the subscriber.
     * This gets updated after PCCA recieves usage report from the NEP.
     * 
     */ 
    public abstract long getUsedCCTotalOctets();
    public abstract void setUsedCCTotalOctets(long totalOctets);

    /*
     * Input Octets granted to the NEP Session initiated on the Gx interface.
     * 
     */ 
    public abstract long getGrantedCCInputOctets();
    public abstract void setGrantedCCInputOctets(long ccTotalOctets);

    /*
     * Input Octets used by the subscriber.
     * This gets updated after PCCA recieves usage report from the NEP.
     * 
     */ 
    public abstract long getUsedCCInputOctets();
    public abstract void setUsedCCInputOctets(long totalOctets);

    /*
     * Output Octets granted to the NEP Session initiated on the Gx interface.
     * 
     */ 
    public abstract long getGrantedCCOutputOctets();
    public abstract void setGrantedCCOutputOctets(long ccTotalOctets);

    /*
     * Output Octets used by the subscriber.
     * This gets updated after PCCA recieves usage report from the NEP.
     * 
     */ 
    public abstract long getUsedCCOutputOctets();
    public abstract void setUsedCCOutputOctets(long totalOctets);

    /*
     * This will be set to DIAMETER_SUCCESS if the session data is updated with the usage.
     * Appropirate Diameter error other wise.
     * 
     */
    public abstract long getResultCode();
    public abstract void setResultCode(long resultCode);

    /*
     * MonitoringKey for which the usage report is sent for this subscriber.
     *
     */
    public abstract String getMonitoringKey();
    public abstract void setMonitoringKey(String monitoringKey);

    /*
     * MonitoringLevel at which the usage report is sent for this subscriber.
     * The Monitoring Level can be either SESSION_LEVEL or PCC_RULE_LEVEL.
     *
     */
    public abstract UsageMonitoringLevel getUsageMonitoringLevel();
    public abstract void setUsageMonitoringLevel(UsageMonitoringLevel umLevel);

    /*
     * Charging rule for which the usage report is sent for this subscriber.
     *
     */
    public abstract String getChargingRule();
    public abstract void setChargingRule(String chargingRule);

    public String getUsageMonitoringLevelToString(UsageMonitoringLevel umLevel) {
    
        if (umLevel == null) {
            return null;
        }

        return UsageMonitoringLevel.valueOf(umLevel.name()).toString();
       
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Charging Rule Name [" + getChargingRule() + "]");
        sb.append("MonitoringKey [" + getMonitoringKey() + "]");
        sb.append("UsageMonitoringLevel [" + getUsageMonitoringLevelToString(getUsageMonitoringLevel()) + "]");
        sb.append("Granted Total Octets [" + getGrantedCCTotalOctets() + "] ");
        sb.append("Granted Input Octets [" + getGrantedCCInputOctets() + "] ");
        sb.append("Granted Output Octets [" + getGrantedCCOutputOctets() + "] ");
        sb.append("Used Total Octets [" + getUsedCCTotalOctets() + "] ");
        sb.append("Used Input Octets [" + getUsedCCInputOctets() + "] ");
        sb.append("Used Output Octets [" + getUsedCCOutputOctets() + "] ");
        sb.append("Result Code [" + getResultCode() + "]");
        
        return sb.toString();
    }
}
