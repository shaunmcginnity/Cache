package org.smg.sessionmanager.cache;

public final class GxServiceDataImpl extends GxServiceData {

    private long usedCCTotalOctets = 0;

    private long usedInputOctets = 0;

    private long usedOutputOctets = 0;

    private long grantedCCTotalOctets = 0;

    private long grantedInputOctets = 0;

    private long grantedOutputOctets = 0;

    private long resultCode;

    private UsageMonitoringLevel umLevel = UsageMonitoringLevel.INVALID_LEVEL;

    private String monitoringKey = null;

    private String chargingRule = null;

    public GxServiceDataImpl() {
    }

    @Override
    public long getUsedCCTotalOctets() {
        return usedCCTotalOctets;
    }

    @Override
    public void setUsedCCTotalOctets(long totalOctets) {
        this.usedCCTotalOctets = totalOctets;
    }

    @Override
    public long getGrantedCCTotalOctets() {
        return grantedCCTotalOctets;
    }

    @Override
    public void setGrantedCCTotalOctets(long ccTotalOctets) {
        this.grantedCCTotalOctets = ccTotalOctets;
    }

    @Override
    public long getGrantedCCInputOctets() {
        return grantedInputOctets;
    }

    @Override
    public void setGrantedCCInputOctets(long ccInputOctets) {
        this.grantedInputOctets = ccInputOctets;
    }

    @Override
    public long getUsedCCInputOctets() {
        return usedInputOctets;
    }

    @Override
    public void setUsedCCInputOctets(long inputOctets) {
        this.usedInputOctets = inputOctets;
    }

    @Override
    public long getGrantedCCOutputOctets() {
        return grantedOutputOctets;
    }

    @Override
    public void setGrantedCCOutputOctets(long ccOutputOctets) {
        this.grantedOutputOctets = ccOutputOctets;
    }

    @Override
    public long getUsedCCOutputOctets() {
        return usedOutputOctets;
    }

    @Override
    public void setUsedCCOutputOctets(long outputOctets) {
        this.usedOutputOctets = outputOctets;
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
    public UsageMonitoringLevel getUsageMonitoringLevel() {
        return umLevel;
    }

    @Override
    public void setUsageMonitoringLevel(UsageMonitoringLevel umLevel) {
        this.umLevel = umLevel;

    }

    @Override
    public String getMonitoringKey() {
        return monitoringKey;
    }

    @Override
    public void setMonitoringKey(String monitoringKey) {
        this.monitoringKey = monitoringKey;
    }

    @Override
    public String getChargingRule() {
        return chargingRule;
    }

    @Override
    public void setChargingRule(String chargingRule) {
        this.chargingRule = chargingRule;
    }
}
