package org.smg.policy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Policy {

    private final String gxSessionId;

    private Map<String, Rule> chargingRules = null;

    private Map<String, UsageMonitoring> usageMonitoring;
    
    private List<AttributeValuePair> capturedAttributes;

    private boolean isTornDown = false;

    private String identity;
    
    /**
     * Create a new policy.
     *
     * @param gxSessionId the gxSessionId to set
     */
    public Policy(final String gxSessionId) {
        this.gxSessionId = gxSessionId;
    }

    /**
     * Update this policy will rule pushes etc
     * @param newPolicy
     * @return
     */
    public boolean updatePolicy(final Policy newPolicy) {
    	boolean anyChanges = false;

    	final Map<String, Rule> newRules = newPolicy.getChargingRules();
    	final int numberOfRules = newRules.values().size();
    	
    	if (null == chargingRules) {
    	    chargingRules = new LinkedHashMap<>(numberOfRules);
    	}

    	for (final Rule newRule: newRules.values()) {
    		switch (newRule.getType()) {
	            case INSTALL:
	                chargingRules.put(newRule.getName(), newRule);
	                anyChanges = true;
	                break;

	            case REMOVE:
	                chargingRules.remove(newRule.getName());
	                anyChanges = true;
	                break;
    		}
    	}

        final List<AttributeValuePair> additionalAvps = newPolicy.getCapturedAttributes();
        if ((null != additionalAvps) && (additionalAvps.isEmpty() != true)) {
            // set the captured AVPs only if we have received some in the policy push
            updateCapturedAttributes(newPolicy.getCapturedAttributes());            
        }

    	return anyChanges;
    }

    public void setChargingRules(final Map<String, Rule> rules) {
        chargingRules = rules;
    }

    public Map<String, Rule> getChargingRules() {
    	return chargingRules;
    }

    public void setUsageMonitoringInformation(final Map<String, UsageMonitoring> usageMonitoring) {
    	this.usageMonitoring = usageMonitoring;
    }

    public Map<String, UsageMonitoring> getUsageMonitoringInformation() {
    	return usageMonitoring;
    }

    /**
     * @return the gxSessionId
     */
    public String getGxSessionId() {
        return gxSessionId;
    }


    /**
     * @return the chargingRuleNames
     */
    public Set<String> getChargingRuleNames() {
    	if (chargingRules != null) {
    		return chargingRules.keySet();
    	}
    	return null;
    }

    public void setIdentity(final String identity) {
    	this.identity = identity;
    }

    public String getIdentity() {
    	return identity;
    }

    public void setTornDown(final boolean isTornDown) {
        this.isTornDown = isTornDown;
    }

    public boolean isTornDown() {
        return isTornDown;
    }


    @Override
    public String toString() {
        final StringBuilder text = new StringBuilder();
        text.append("GxSessionID=");
        text.append(gxSessionId);

        text.append(" Identity=");
        text.append(identity);

        if (getChargingRuleNames() != null) {
	        text.append(" Rules=");
	        for (final String rule : getChargingRuleNames()) {
	            text.append("\t"+ rule);
	        }
        }

        if (getCapturedAttributes() != null) {
            text.append(" Attributes=");
	        for (final AttributeValuePair avp : getCapturedAttributes()) {
	            text.append("\t"+ avp);
	        }
        }

        return text.toString();
    }

    public String usageMonitoringString() {
		final StringBuilder text = new StringBuilder();
    	if (usageMonitoring != null) {

    		for (final String key: usageMonitoring.keySet()) {
    			final UsageMonitoring um = usageMonitoring.get(key);
    			text.append("UM Key: [" + key + "] Level: [" + um.getUsageMonitoringLevel() + "]");
    			ServiceUnit su = um.getGrantedServiceUnit();
    			if (su != null) {
    				text.append(" [GSU: In[" + su.getInputOctets() + "] Out[" + su.getOutputOctets() + "] Total[" + su.getTotalOctets() + "]]");
    			}
    			su = um.getUsedServiceUnit();
    			if (su != null) {
    				text.append(" [USU: In[" + su.getInputOctets() + "] Out[" + su.getOutputOctets() + "] Total[" + su.getTotalOctets() + "]]");
    			}
    			text.append(" | ");
    		}
    	}
    	return text.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (! (o instanceof Policy)) {
            return false;
        }

        final Policy other = (Policy) o;

        return new EqualsBuilder().append(gxSessionId, other.gxSessionId)
                                  .append(getChargingRuleNames(), other.getChargingRuleNames())
                                  .append(identity, other.identity)
                                  .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(gxSessionId)
                                    .append(getChargingRuleNames())
                                    .append(identity)
                                    .toHashCode();
    }

	public void setCapturedAttributes(List<AttributeValuePair> capturedAttriburtes) {
		this.capturedAttributes = capturedAttriburtes;		
	}
	
    private void updateCapturedAttributes(List<AttributeValuePair> capturedAvps) {
        for (AttributeValuePair capturedAvp : capturedAvps) {
            for(AttributeValuePair policyAvp : this.capturedAttributes) {
                //If the AVP already exists, replace it
                if (policyAvp.getAttributeName().equals(capturedAvp.getAttributeName())) {
                    this.capturedAttributes.remove(policyAvp);
                    break;
                }
            }
            
        }

        this.capturedAttributes.addAll(capturedAvps);

	}
	
	public List<AttributeValuePair> getCapturedAttributes() {
		return capturedAttributes;
	}

}
