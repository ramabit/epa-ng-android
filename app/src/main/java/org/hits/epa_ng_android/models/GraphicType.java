package org.hits.epa_ng_android.models;

public enum GraphicType {
    horizontal("horizontal"),
    vertical("vertical"),
    circular("circular");

    private String stringValue;

    GraphicType(String value) {
        this.stringValue = value;
    }

    public String getStringValue() {
        return stringValue;
    }

}
