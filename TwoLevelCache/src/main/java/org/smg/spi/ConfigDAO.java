package org.smg.spi;

import org.smg.oam.config.client.ConfigException;
import org.smg.oam.config.client.ConfigGroup;
import org.smg.oam.config.client.ConfigListener;


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
