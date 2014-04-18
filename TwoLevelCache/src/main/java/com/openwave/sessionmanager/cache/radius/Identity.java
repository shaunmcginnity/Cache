package com.openwave.sessionmanager.cache.radius;

import java.util.List;

import com.openwave.spi.DataDictionary;

/**
 * The RADIUS session data.
 *
 */
public interface Identity {

    /**
     * @return The client IP address.
     */
    String getIp();

    /**
     * @return The device id.
     */
    String getDeviceId();

    /**
     * @return The RADIUS username.
     */
    String getUserName();

    /**
     * @return The time when the RADIUS request was made.
     */
    int getCreateTime();

    /**
     * @return The RADIUS client id.
     */
    String getRadClientId();

    int getTtl();

    List<CustomAttribute> getCustomAttributes();

    String getAccountSessionId();

    String getCorrelationId();

    int getEventTimestamp();

    DataDictionary toDataDictionary();
}