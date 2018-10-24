package org.csuc.utils.parser;

/**
 * @author amartinez
 */
public enum ParserFormat {

    XML("xml"),
    JSON("json");

    private final String value;

    ParserFormat(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static ParserFormat convert(String value) {
        for (ParserFormat inst : values()) {
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
