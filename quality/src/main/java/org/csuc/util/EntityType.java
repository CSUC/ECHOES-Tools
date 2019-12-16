package org.csuc.util;

public enum EntityType {

    ProvidedCHO("edm:ProvidedCHO"),
    Place("edm:Place"),
    Concept("skos:Concept"),
    TimeSpan("edm:TimeSpan"),
    Agent("edm:Agent"),
    Aggregation("ore:Aggregation"),
    WebResource("edm:WebResource"),
    License("cc:License");

    private final String type;

    EntityType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
