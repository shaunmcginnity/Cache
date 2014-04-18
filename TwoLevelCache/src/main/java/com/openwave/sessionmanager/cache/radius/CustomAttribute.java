package com.openwave.sessionmanager.cache.radius;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CustomAttribute {

    private final String attributeName;
    private final int attributeType;
    private final CustomDataType dataType;
    private final Object dataValue;
    private final int vendorId;
    private final int vendorType;

    // This is an immutable object, so we need only calculate the hash code once.
    private int cachedHashCode = 0;

    public CustomAttribute(final String attributeName,
                           final int attributeType,
                           final CustomDataType dataType,
                           final Object dataValue,
                           final int vendorId,
                           final int vendorType) {
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.dataType = dataType;
        this.dataValue = dataValue;
        this.vendorId = vendorId;
        this.vendorType = vendorType;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public int getAttributeType() {
        return attributeType;
    }

    public CustomDataType getDataType() {
        return dataType;
    }

    public Object getDataValue() {
        return dataValue;
    }

    public int getVendorId() {
        return vendorId;
    }

    public int getVendorType() {
        return vendorType;
    }

    @Override
    public boolean equals(final Object o) {
        if (null == o) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (! (o instanceof CustomAttribute)) {
            return false;
        }

        final CustomAttribute other = (CustomAttribute) o;

        return new EqualsBuilder()
        .append(attributeName, other.getAttributeName())
        .append(attributeType, other.getAttributeType())
        .append(dataType, other.getDataType())
        .append(dataValue, other.getDataValue())
        .append(vendorId, other.getVendorId())
        .append(vendorType, other.getVendorType())
        .isEquals();
    }

    @Override
    public int hashCode() {
        if (0 == cachedHashCode) {
            cachedHashCode = new HashCodeBuilder()
        .append(attributeName)
        .append(attributeType)
        .append(dataValue)
        .append(vendorId)
        .append(vendorType)
        .toHashCode();
    }

        return cachedHashCode;
    }

}
