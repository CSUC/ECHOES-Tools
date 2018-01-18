package org.Validation.Core.edm;

public enum PlaceTypes {
    CONTINENT("continent"),
    COUNTRY("country"),
    STATE("state"),
    REGION("region"),
    MUNICIPALITY("municipality"),
    CITY("city"),
    TOWN("town"),
    OTHER("other");

    private final String value;

    private PlaceTypes(String value) {
        this.value = value;
    }

    public String xmlValue() {
        return value;
    }

    public static PlaceTypes convert(String value) {
        for (PlaceTypes inst : values()) {
            if (inst.xmlValue().equals(value)) {
                return inst;
            }
        }
        return null;
    }
}
