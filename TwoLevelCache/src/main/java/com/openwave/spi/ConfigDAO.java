package com.openwave.spi;

import com.openwave.oam.config.client.ConfigException;
import com.openwave.oam.config.client.ConfigGroup;
import com.openwave.oam.config.client.ConfigListener;


public interface ConfigDAO {
	
    void registerConfigItemListeners( String configGroupKey, String [] configItemKeys, String callerId, ConfigListener configListener ) throws ConfigException;
    int readConfigKeyInt( String key ) throws ConfigException;
    boolean readConfigKeyBoolean( String key ) throws ConfigException;
	String readConfigKeyString( String key ) throws ConfigException;
    String [] readConfigKeyStringArray( String key ) throws ConfigException;
    String getInstanceId();
	String getHost();
	String getServerType();
	ConfigGroup getConfigGroup( String configGroupKey ) throws ConfigException;
	String getRegion();

}
