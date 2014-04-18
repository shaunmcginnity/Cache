package org.smg.policy;


public interface UsageMonitoring
{
	public enum UsageMonitoringLevel { SESSION, RULE };
	
	public String getMonitoringKey();
	public ServiceUnit getGrantedServiceUnit();
	public ServiceUnit getUsedServiceUnit();
	public UsageMonitoringLevel getUsageMonitoringLevel();
	public boolean isUsageMonitoringReportRequired();
	public boolean isUsageMonitoringSupportDisabled();

}
