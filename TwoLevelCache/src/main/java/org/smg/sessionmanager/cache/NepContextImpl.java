package org.smg.sessionmanager.cache;

public class NepContextImpl implements NepContext {

    private final String sessionInitiatingPeer;
    private final String peerOriginRealm;
    private final String peerOriginHost;

    public NepContextImpl (final String peerAddress, final String peerOriginRealm, final String peerOriginHost) {

        this.sessionInitiatingPeer = peerAddress;
        this.peerOriginRealm = peerOriginRealm;
        this.peerOriginHost = peerOriginHost;
    }

    @Override
    public String getSessionInitiatingPeer() {
        return sessionInitiatingPeer;
    }

    @Override
    public String getPeerOriginRealm() {
        return peerOriginRealm;
    }

    @Override
    public String getPeerOriginHost() {
        return peerOriginHost;
    }
}
