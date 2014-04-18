package com.openwave.policy;

public class Rule {

    public enum RuleType {
        INSTALL,
        REMOVE;
    }

    public enum MeteringMethod {
        DURATION,
        VOLUME,
        DURATION_VOLUME;

        public static MeteringMethod fromInt(int value) {
            switch (value) {
                case 0:
                return MeteringMethod.DURATION;
                
                case 1:
                return MeteringMethod.VOLUME;

                case 2:
                return MeteringMethod.DURATION_VOLUME;

            }
           
            return null;
        }

        public static int toInt(MeteringMethod method) {

            switch (method) {
            
            case DURATION:
                return 0;
            
            case VOLUME:
                return 1;
            
            case DURATION_VOLUME:
                return 2;
            }

            return -1;
        }
    }

    private final String name;

    private final RuleType type;

    private String monitoringKey;

    // Cache the generated hash code so we only calculate it when we need to.
    private int cachedHashCode = 0;

    /*
     * The field is to store the value of Precedecne AVP of the charging rule in the PCCA cache for the specicied subscriber.
     *
     * The Precedence AVP (AVP code 1010) is of type Unsigned32.
     * Within the Charging Rule Definition AVP, the Precedence AVP determines the order, in which the service data 
     * flow templates are applied at service data flow detection at the PCEF.
     *
     * See 3GPP TS 29.212 V11.7.0 (2012-12) for more information.
     *
     */
    private long precedence;

    /*
     * The field is to storethe MeteringMethod AVP value in the PCCA cache for the specified subscriber session.
     *
     * The Metering-Method AVP (AVP code 1007) is of type Enumerated, and it defines what parameters shall be metered for offline charging.
     *
     * The PCEF may use the AVP for online charging in case of decentralized unit determination, refer to 3GPP TS 32.299 [19]. 
     *
     * DURATION (0)
     *     This value shall be used to indicate that the duration of the service data flow shall be metered.
     * VOLUME (1)
     *     This value shall be used to indicate that volume of the service data flow traffic shall be metered.
     * DURATION_VOLUME (2)
     *     This value shall be used to indicate that the duration and the volume of the service data flow traffic shall be metered.
     *
     * See 3GPP TS 29.212 V11.7.0 (2012-12) for more information.
     *
     */
    private MeteringMethod meteringMethod;

    /*
     * The field is for stroring the Rating-Group AVP value in the PCCA Cache for the specified subsriber session.
     *
     * The Raring-Group AVP (AVP code 432) is of type Unsigned32.
     *
     * The charging key for the PCC rule used for rating purposes.
     *
     */
    private long ratingGroup;

    /*
     * The field is for stroring the Service-Identifier AVP value in the PCCA Cache for the specified subsriber session.
     *
     * The Service-Identifier AVP (AVP code 461) is of type Unsigned32.
     *
     * The identity of the service or service component the service data flow in a PCC rule relates to. 
     *
     */
    private long serviceIdentifier;

    /*
     * The field is set to TRUE if the charging rule is a ChargingRuleBase name
     *
     */
    private boolean isRuleBaseNameFlag;

    public Rule(final String name, final RuleType type, final boolean isRuleBaseNameFlag) {
        this.name = name;
        this.type = type;
        this.isRuleBaseNameFlag = isRuleBaseNameFlag;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public RuleType getType() {
        return type;
    }

    /**
     * @return isChargingRuleBaseName flag
     *
     */
    public boolean isRuleBaseName() {
        return isRuleBaseNameFlag;
    }

    public void setMonitoringKey(final String monitoringKey) {
        // Reset the cachedHashCode since the monitoringKey is being updated.
        cachedHashCode = 0;
    	this.monitoringKey = monitoringKey;
    }

    public String getMonitoringKey() {
    	return monitoringKey;
    }

    public void setPrecedence(final long precedence) {
        this.precedence = precedence;
    }

    public long getPrecedence() {
        return precedence;
    }

    @Override
    public String toString() {
        return (name + ": " + type.toString());
    }

    public void setRuleMeteringMethod(final MeteringMethod method) {
        this.meteringMethod = method;
    }

    public MeteringMethod getRuleMeteringMethod() {
        return meteringMethod;
    }

    public boolean isMonitoringEnabled() {

        if ((monitoringKey != null) || (meteringMethod != null) || (precedence > 0)) {
            return true;
        }
    
        return false;
    }

    public void setRatingGroup(long ratingGroup) {
        this.ratingGroup = ratingGroup;
    }
    
    public long getRatingGroup() {
        return ratingGroup;
    }
  
    public void setServiceIdentifier(long serviceIdentifier) {
        this.serviceIdentifier = serviceIdentifier;
    }
    
    public long getServiceIdentifier() {
        return serviceIdentifier;
    }

    @Override
    public int hashCode() {
        // Only compute the hash code when we need to, otherwise return the cached value.
        if (0 == cachedHashCode) {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((monitoringKey == null) ? 0 : monitoringKey.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            cachedHashCode = result;
        }

        return cachedHashCode;
    }

    /**
     * We only check equality on the <tt>monitoringKey</tt> and <tt>name</tt>. 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Rule other = (Rule) obj;

        if (isRuleBaseNameFlag != other.isRuleBaseName()) {
            return false;
        }

        if (monitoringKey == null) {
            if (other.monitoringKey != null) {
                return false;
            }
        } else if (!monitoringKey.equals(other.monitoringKey)) {
            return false;
        }

        if (name == null) {
            if (other.name != null) {
                return false; 
            }
        } else if (!name.equals(other.name)) {
            return false;
    }

        return true;
    }

}
