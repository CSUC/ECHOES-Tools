package org.csuc.utils.recollect;


/**
 * @author amartinez
 */
public enum StatusLink {

    EXPIRED("expired"),
    GENERATE("generate"),
    PROGRESS("progress"),
    NULL("null");

    private final String value;

    StatusLink(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static StatusLink convert(String value) {
        for (StatusLink inst : values()) {
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
