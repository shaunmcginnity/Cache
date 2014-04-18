package com.openwave.spi;

public interface GenericRequest {

    String getRequestName();

    GenericRequest createRequest();
    
    DataDictionary getDataDictionary( String name );
    DataDictionary createDataDictionary( String name );
    DataDictionary removeDataDictionary(String name);
    
}
