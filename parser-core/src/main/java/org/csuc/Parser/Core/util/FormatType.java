package org.csuc.Parser.Core.util;

/**
 * @author amartinez
 */
public enum FormatType {

    XML("xml"), JSON("json");

    private final String value;

    private FormatType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static FormatType convert(String value) {
        for (FormatType inst : values()) {
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
