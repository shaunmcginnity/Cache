package com.openwave.session;

import java.util.Map;

import com.openwave.policy.Policy;
import com.openwave.sessionmanager.cache.AuthorisationStatus;
import com.openwave.sessionmanager.cache.IdentitySource;
import com.openwave.sessionmanager.cache.Session;
import com.openwave.sessionmanager.cache.radius.Identity;
import com.openwave.spi.GenericRequest;

public interface SessionHelper {

    /**
     * Just return the session and cut out the middle man.
     * @return The session.
     */
    Session getSession(GenericRequest request);

    /**
     * Get the session client IP address.
     *
     * @param request The request
     */
    String getClientIP( GenericRequest request );

    /**
     * Get the accummulated duration of this session.
     *
     * @param request The request
     */
    long getDuration( GenericRequest request );

    /**
     * Flag that the session requires a policy update.
     *
     * @param request The request
     */
    void setSessionUpdateRequired( GenericRequest request );

    /**
     * Flag that the session has expired.
     *
     * @param request The request
     *
     */
    void setSessionExpired(GenericRequest request);

    /**
     * Sets the policy to the session.
     *
     * @param request The request
     * @param policy The policy rules received from the PCRF
     */
    void setPolicy(GenericRequest request, Policy policy);

    /**
     * Gets the policy from the session.
     *
     * @param request The request
     * @return the Policy object for this session
     */
    Policy getPolicy(GenericRequest request);

    /**
     * Clears the policy from the session.
     *
     * @param request The request
     */
    void clearPolicy(GenericRequest request);

    int getIntegraChannelId(GenericRequest request);
    String getIntegraSessionId(GenericRequest request);

    /**
     * This returns the IP Address of the Integra instance that initiated the session.
     *
     * @param request The request.
     * @return The IP address of the Integra instance that initiated the session.
     */
    String getIntegraIPAddress(GenericRequest request);

    /**
     * Gets the NIM details from the session.
     *
     * @param request The request.
     * @return {@link Identity} containing the NIM details relevant to the session. Can be {@code null} if no
     * NIM details exist for the session.
     */
    Identity getNIMIdentity(GenericRequest request);

    Map<String, String> getSubscriberAttributes(GenericRequest request);
    void setSubscriberAttributes(GenericRequest request, Map<String, String> attributes);

    String getIdentity(GenericRequest request);
    IdentitySource getIdentitySource(GenericRequest request);

    /**
     * Sets the authorisation status for the session.
     *
     * @param request The request
     * @param authorisationStatus The authorisation status to set for this session.
     */
    void setAuthorisationStatus(GenericRequest request, AuthorisationStatus authorisationStatus);
    /**
     * Gets the authorisation status from the session.
     *
     * @param request The request
     * @return the authorisation status for this session
     */
    AuthorisationStatus getAuthorisationStatus(GenericRequest request);
}
