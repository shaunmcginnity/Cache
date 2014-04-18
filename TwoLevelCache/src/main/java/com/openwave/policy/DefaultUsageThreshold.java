package com.openwave.policy;

public class DefaultUsageThreshold implements UsageThreshold {
    private static final int HASH_PRIME = 31;

	private final CollectionType type;
	private long threshold;
	
	public DefaultUsageThreshold(CollectionType type, long threshold) {
		this.type = type;
		this.threshold = threshold;
	}

	@Override
	public CollectionType getType() {
		return type;
	}

	@Override
	public long getThreshold() {
		return threshold;
	}
	
	public long addToThreshold(long value) {
		threshold += value;
		return threshold;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof DefaultUsageThreshold)) {
			return false;
		}
		
		DefaultUsageThreshold other = (DefaultUsageThreshold)obj;
		if (this.type != other.type) {
			return false;
		}
		if (this.threshold != other.threshold) {
			return false;
		}
		
		return true;
	}

    @Override
    public int hashCode() {
        int result = 1;
        result = HASH_PRIME * result + (int) (threshold ^ (threshold >>> 32));
        result = HASH_PRIME * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }
}
