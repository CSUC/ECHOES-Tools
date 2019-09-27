package org.csuc.poi;

public enum STEP1 {

    INPUT("input"),
    MESSAGE("message");

    private final String value;

    STEP1(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
