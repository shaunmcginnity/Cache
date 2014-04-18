package com.openwave.sessionmanager.config;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicReference;

import com.openwave.oam.config.client.ConfigException;
import com.openwave.oam.config.client.ConfigGroup;
import com.openwave.oam.config.client.ConfigListener;
import com.openwave.sessionmanager.cache.IdentitySource;
import com.openwave.sessionmanager.cache.SessionImpl;
import com.openwave.sessionmanager.log.Log;
import com.openwave.spi.ConfigDAO;


public class IdentityControlConfiguration implements ConfigListener {

    
    private AtomicReference<List<IdentitySource>> identitySourcesRef = new AtomicReference<List<IdentitySource>>();
    
    private final static String GROUP_IDENTITY_CONTROL = "IdentityControl";
    private final static String GROUP_IDENTITY_SOURCES = "IdentitySources";
    private final static String CFG_IDENTITY_SOURCE = "IdentitySource";
    
    private final ConfigDAO configDao;
    
    public IdentityControlConfiguration(ConfigDAO configDao) throws ConfigException {
        this.configDao = configDao;
        initialise();
        configDao.registerConfigItemListeners(GROUP_IDENTITY_CONTROL, new String[] {GROUP_IDENTITY_SOURCES}, this.getClass().getCanonicalName(), this);
        
        SessionImpl.setIdentityControlConfiguration(this);
    }
    
    public List<IdentitySource> getIdentitySources() {
        return identitySourcesRef.get();
    }
    
    private void initialise() throws ConfigException {

        List<IdentitySource> identitySources = new LinkedList<IdentitySource>();
        try {
            
            ConfigGroup topGroup = configDao.getConfigGroup(GROUP_IDENTITY_CONTROL);
            
            ConfigGroup sourcesGroup = topGroup.getConfigGroup(GROUP_IDENTITY_SOURCES);
            ListIterator<ConfigGroup> entryIter = sourcesGroup.getGroupIterator();
            
            if (entryIter == null) {
            	throw new ConfigException
            		("No client identity sources defined in the [PCCA Agent/Client Identity Control] config group");
            }
            
            while(entryIter.hasNext()) {
                ConfigGroup entry = entryIter.next();
                String source = entry.getConfigString(CFG_IDENTITY_SOURCE);
                IdentitySource identitySource = IdentitySource.getValueFromConfig(source);
                identitySources.add(identitySource);
            }
            
        } catch (ConfigException e) {
            Log.ConfigError("IdentityControlConfiguration: " + e.getMessage());
            if (identitySources.isEmpty()) {
                identitySources.add(IdentitySource.INTEGRA);
            }
        } 

        identitySourcesRef.set(identitySources);
    }
    
    
    @Override
    public void notify(String arg0) {
        try {
            initialise();
        } catch (ConfigException e) {
            Log.ConfigError("IdentityControlConfiguration: " + e.getMessage());
        }
    }
    
}
