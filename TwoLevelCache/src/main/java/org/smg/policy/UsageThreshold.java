package org.smg.policy;

public interface UsageThreshold
{
	public enum CollectionType { INPUT, OUTPUT, INPUT_OUTPUT, UNKNOWN } ;
	public final static String SESSION_KEY = "Session";
	
	public CollectionType getType();
	public long getThreshold();
}
