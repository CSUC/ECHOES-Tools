package org.csuc.util;

public enum QualityType {

    ResourceOrLiteralType("ResourceOrLiteralType"),
    LiteralType("LiteralType"),
    ResourceType("ResourceType"),
    LanguageType("LanguageType");

    private final String type;

    QualityType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
