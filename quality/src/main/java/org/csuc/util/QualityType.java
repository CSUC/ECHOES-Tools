package org.csuc.util;

public enum QualityType {

    ResourceOrLiteralType("ResourceOrLiteralType"),
    LiteralType("LiteralType"),
    ResourceType("ResourceType"),
    LanguageType("LanguageType"),
    AboutType("AboutType"),
    UGCType("UGCType");

    private final String type;

    QualityType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
