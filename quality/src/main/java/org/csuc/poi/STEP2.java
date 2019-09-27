package org.csuc.poi;

public enum STEP2 {

    INPUT("input"),
    TEST("test"),
    MESSAGE("message");

    private final String value;

    STEP2(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
