package org.EDM.Transformations.formats.utils;


/**
 * @author amartinez
 */
public enum PlaceType {

    COUNTRY("Contry"),
    PROVINCE("Province"),
    STATE("State"),
    COUNTY("Country"),
    PLACE("Place"),
    MUNICIPALITY("Municipality"),
    PARTMUNICIPALITY("PartMunicipality");

    private final String value;

    private PlaceType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static PlaceType convert(String value) {
        for (PlaceType inst : values()) {
            if (inst.value().equals(value)) {
                return inst;
            }
        }
        return null;
    }
}
