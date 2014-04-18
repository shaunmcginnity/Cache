package org.smg.sessionmanager.cache;

public interface NepContext {
    
    public static final String NEP_CONTEXT_KEY="NepContext";

    /*
     * Originating peer's address, to which messages needs to be sent.
     *
     */
    public String getSessionInitiatingPeer();

    /*
     * Originating peer's realm AVP value.
     *
     */
    public String getPeerOriginRealm();

    /*
     * Originating peer's DestinationAddress AVP value
     *
     */
    public String getPeerOriginHost();

}
