package org.csuc.utils;

/**
 * @author amartinez
 */
public enum Status {

    ERROR("ERROR"),
    END("END"),
    QUEUE("QUEUE"),
    PROGRESS("PROGRESS");

    private final String value;

    private Status(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Status convert(String value) {
        for (Status inst : values()) {
            if (inst.value().equals(value)) {
                return inst;
            }
        }
        return null;
    }

    public boolean equalsName(String otherName) {
        //(otherName == null) check is not needed because name.equals(null) returns false
        return value.equals(otherName);
    }

    @Override
    public String toString() {
        return value;
    }
}
