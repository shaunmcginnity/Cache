package org.smg.policy;

public class AttributeValuePair {
	
	/**
	 * Type represents the type of data 
	 * stored by this AVP.  
	 * Typically an AVP may be String, Int, date, Address etc
	 * However as we will only use this data for logging, 
	 * and some simple pattern matching, we will convert any
	 * non-integer values to there string representation.
	 * 
	 * In the future it may be necessary to extend the 
	 * available types.
	 * 
	 */
    public enum Type {
        STRING,
        INTEGER,
        LONG,
        BYTES,
        ENUMERATED,
        OCTET_STRING,
        UTF8_STRING
    };
    
    /**
     * Name of the Attribute i.e.
     * 3GPP-IMEI
     * 
     */
    private String name;
    
    /**
     * The type of the data stored
     * from this attribute, i.e. is the
     * data integer or string. 
     */
    private Type type;
    
    /**
     * The data stored by this attribute.
     */
    private Object data;
    
    public AttributeValuePair(String name, Type type, Object data) {
    	this.name = name;
    	this.type = type;
    	this.data = data;
    }
    
    /**
     * Returns the name of the attribute
     * @return
     */
    public String getAttributeName() {
    	return name;
    }
     
    /**
     * Returns the type of the data stored
     * @return
     */
    public Type getDataType() {
    	return type;
    }
        
    /**
     * Returns the data as an Object.
     * It's up to the caller to determine the
     * type and treat it accordingly.
     * 
     * @return
     */
    public Object getDataValue() {
    	return data;
    }
    
    public String toString() {
    	return String.format("Name [%s] Type [%s] Data [%s]", 
    			getAttributeName(), 
    			getDataType().name(),
    			(getDataValue() != null ? getDataValue() : "<empty>"));
    }

}
