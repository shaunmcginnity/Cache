package org.smg.sessionmanager.cache;

public class SessionImplFactory {

    public SessionImpl newInstance(final long ttl) {
        return new SessionImpl(ttl);
    }

}
