package com.openwave.sessionmanager.cache;

import java.util.HashMap;
import java.util.Map;

/*
 * The source of subscriber identity
 */
public enum IdentitySource {
    
    /*
     * No identity has been found
     */
    NONE("None"),

    /*
     * Sourced from Integra
     */
    INTEGRA("Integra"),
    
    /*
     * Sourced from the RADIUS device id field
     */
    RADIUS_DEVICE_ID("RADIUS Device ID"),
    
    /*
     * Sourced from the RADIUS username field
     */
    RADIUS_USERNAME("RADIUS Username"),
    
    /*
     * Sourced from the PCRF
     */
    PCRF("PCRF");
    
    
    private IdentitySource(String name) {
        this.name = name;
    }
    
    public static IdentitySource getValueFromConfig(String name) {
        return map.get(name);
    }
    
    private String name;
    private static Map<String, IdentitySource> map = new HashMap<String, IdentitySource>(5);
    
    static {
        for (IdentitySource identitySource: IdentitySource.values()) {
            map.put(identitySource.name, identitySource);
        }
    }
    
    public String getName() {
    	return name;
    }
}
