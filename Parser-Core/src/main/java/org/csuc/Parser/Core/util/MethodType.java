package org.csuc.Parser.Core.util;

/**
 * @author amartinez
 */
public enum MethodType {

    SAX("sax"),
    DOM4J("dom4j"),
    DOM("dom"),
    XSLT("xslt");

    private final String value;

    private MethodType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static MethodType convert(String value) {
        for (MethodType inst : values()) {
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
