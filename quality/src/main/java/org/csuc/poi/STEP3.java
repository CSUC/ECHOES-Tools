package org.csuc.poi;

public enum STEP3 {

    INPUT("input"),
    TYPE("type"),
    METADATA("metadata"),
    VALIDATION_TYPE("validation-type"),
    MESSAGE("message"),
    LEVEL("level");

    private final String value;

    STEP3(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
