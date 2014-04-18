package com.openwave.policy;

public class PolicyDDKeys
{
	// The key for the accounting data dictionary 
	public final static String AccountingDD = "AccountingDD";
	
	// Key for the usage monitoring groups, sent from the PCRF
	public final static String UsageMonitoring = "UsageMonitoring";
	
	// Key for the usage thresholds map, which is sent south to the PTSP
	public final static String UsageThresholds = "UsageThresholds";
	
	// Key for the reported threshold usage map, which is sent north from the PTSP
	public final static String ReportedUsage = "ReportedUsage";
	
	// Key for the mid-session requested usage list, sent south from the PCRF
	public final static String RequestedUsage = "RequestedUsage";
	
    // Key for the non-shared policies
    public final static String NonSharedPolicies = "NonSharedPolicies";

}
