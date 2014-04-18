package com.openwave.sessionmanager.cache;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The cache key used to index the session cache.
 * @author bnesbitt
 *
 */
public class Key {
    private static final int PRIME = 31;

    /**
     * The client IP address.
     */
    private String clientIP;
    
    /**
     * The PCRF Gx Session Id.
     */
    private String pcrfSessionId;
    
    /**
     * The PTSP Session Id - Note that this probably isn't used anymore.
     */
    private String ptspSessionId;
    
    /**
     * The clients identity.
     */
    private String identity;

    /**
     * Static builder method to create a new empty <tt>Key</tt>.
     * 
     * @return A new <tt>Key</tt> object.
     */
    public static Key createKey() {
        return new Key();
    }

    private Key() {
    }

    /**
     * Used to determine if Key is empty (null).
     * 
     * @return True if the Key object components are null.
     */
    public boolean isNull() {
        return clientIP == null && pcrfSessionId == null && ptspSessionId == null && identity == null;
    }

    /*
     * Builders.
     */
    
    /**
     * Builder method that can be chained with other builder methods.
     * <p>
     * Assigns the client IP address to the Key.
     * 
     * @param ip The client IP address.
     * 
     * @return The updated <tt>Key</tt> object with the client IP address set.
     */
    public Key withIPAddress(final String ip) {
        setClientId(ip);
        return this;
    }

    /**
     * Builder method that can be chained with other builder methods.
     * <p>
     * Assigns the PCRF Gx session id to the Key.
     * 
     * @param id The PCRF Gx session id.
     * 
     * @return The updated <tt>Key</tt> object with the PCRF Gx session id set.
     */
    public Key withPCRFSessionId(final String id) {
        setPCRFSessionId(id);
        return this;
    }

    /**
     * Builder method that can be chained with other builder methods.
     * <p>
     * Assigns the PTSP session id to the Key.
     * 
     * @param id The PTSP session id.
     * 
     * @return The updated <tt>Key</tt> object with the PTSP session id set.
     */
    public Key withPTSPSessionId(final String id) {
        setPTSPSessionId(id);
        return this;
    }

    /**
     * Builder method that can be chained with other builder methods.
     * <p>
     * Assigns the client identity to the Key.
     * 
     * @param id The client identity.
     * 
     * @return The updated <tt>Key</tt> object with the client identity set.
     */
    public Key withIdentity(final String id) {
        setIdentity(id);
        return this;
    }

    /*
     * Accessors
     */
    
    /**
     * @return The client IP address.
     */
    public String getClientIP() {
        return clientIP;
    }

    /**
     * @return The PCRF Gx session id.
     */
    public String getPCRFSessionId() {
        return pcrfSessionId;
    }

    /**
     * @return The PTSP session id.
     */
    public String getPTSPSessionId() {
        return ptspSessionId;
    }

    /**
     * @return The clients identity.
     */
    public String getIdentity() {
        return identity;
    }

    /*
     * Mutators.
     */
    
    /**
     * Sets the client IP address.
     *
     * @param clientIP The client IP address.
     */
    public void setClientId(final String clientIP) {
        // Reset the cached hash code to indicate that it needs to be re-calculated.
        this.clientIP = clientIP;
    }

    /**
     * Sets the PCRF Gx session id.
     * 
     * @param pcrfSessionId The PCRF Gx session id.
     */
    public void setPCRFSessionId(final String pcrfSessionId) {
        this.pcrfSessionId = pcrfSessionId;
    }

    /**
     * Sets the PTSP session id.
     *
     * @param ptspSessionId The PTSP session id.
     */
    public void setPTSPSessionId(final String ptspSessionId) {
        this.ptspSessionId = ptspSessionId;
    }

    /**
     * Sets the clients identity.
     *
     * @param identity The clients identity.
     */
    public void setIdentity(final String identity) {
        this.identity = identity;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (isNull()) {
            return false;
        }

        if (! (o instanceof Key)) {
            return false;
        }

        final Key other = (Key) o;

        boolean isEqual = true;

        // clientIP
        if (clientIP != null && other.clientIP != null) {
            isEqual = clientIP.equals(other.clientIP);
        }

        if (! isEqual) {
            return false;
        }

        // pcrfSessionId
        if (pcrfSessionId != null && other.pcrfSessionId != null) {
            isEqual = pcrfSessionId.equals(other.pcrfSessionId);
        }

        if (! isEqual) {
            return false;
        }

        // ptspSessionId
        if (ptspSessionId != null && other.ptspSessionId != null) {
            isEqual = ptspSessionId.equals(other.ptspSessionId);
        }

        if (! isEqual) {
            return false;
        }

        // Identity
        if (null != identity && null != other.identity) {
            isEqual = identity.equals(other.identity);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        final int result = 1;
        return PRIME * result + ((clientIP == null) ? 0 : clientIP.hashCode());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
