package org.smg.sessionmanager.cache;

/**
 * Enumeration containing each possible authorisation status applicable to a {@link Session}.
 * <p>
 * The possible values are {@link AuthorisationStatus#ANONYMOUS}, {@link AuthorisationStatus#UNREGISTERED},
 * {@link AuthorisationStatus#PCRF}, and {@link AuthorisationStatus#REGISTERED}. The table below describes how
 * the authorisation status is determined.
 * <p>
 * <table>
 * <th>NIM Identity</th><th>PCRF Identity</th><th>Integra Identity</th><th>LDAP Attributes</th><th>Auth Status</th>
 * <tr>
 * <td>N</td><td>N</td><td>N</td><td>N</td><td>ANONYMOUS</td>
 * </tr>
 * <tr>
 * <td>Y</td><td>N</td><td>N</td><td>N</td><td>UNREGISTERED</td>
 * </tr>
 * <tr>
 * <td>N</td><td>N</td><td>Y</td><td>N</td><td>UNREGISTERED</td>
 * </tr>
 * <tr>
 * <td>Y</td><td>N</td><td>N</td><td>Y</td><td>REGISTERED</td>
 * </tr>
 * <tr>
 * <td>N</td><td>Y</td><td>N</td><td>N</td><td>PCRF</td>
 * </tr>
 * <tr>
 * <td>N</td><td>Y</td><td>N</td><td>Y</td><td>REGISTERED</td>
 * </tr>
 * <tr>
 * <td>N</td><td>N</td><td>Y</td><td>Y</td><td>REGISTERED</td>
 * </tr>
 * </table>
 * <p>
 * @author rtorney
 * @since 4.0
 */
public enum AuthorisationStatus {

	/**
	 * A {@link Session} has an authorisation status of ANONYMOUS if no AAA(NIM) IP address/identity mapping is
	 * available, no PCRF identity is available, and no identity is imported from a HTTP header.
	 */
	ANONYMOUS,

	/**
	 * A {@link Session} has an authorisation status of UNREGISTERED if an identity is available (from either AAA,
	 * PCRF, or Integra via the identity header), but no LDAP attributes are found.
	 */
	UNREGISTERED,

	/**
	 * A {@link Session} has an authorisation status of PCRF if no AAA(NIM) IP address/identity mapping is
	 * available, but a PCRF identity is available, and no LDAP attributes are found.
	 */
	PCRF,

	/**
	 * A {@link Session} has an authorisation status of REGISTERED if the authentication status was previously set to
	 * UNREGISTERED or PCRF and the LDAP lookup succeeds and returns an entry for this subscriber.
	 */
	REGISTERED,
}
