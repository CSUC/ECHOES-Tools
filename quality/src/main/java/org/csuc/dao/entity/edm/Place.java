package org.csuc.dao.entity.edm;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.dao.entity.Error;
import org.csuc.util.DataType;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import java.util.HashSet;
import java.util.Set;

/**
 * @author amartinez
 */
public class Place {

    @Property("rdf:about")
    private String about;

    @Property("wgs84_pos:lat")
    private String wgs84_lat;

    @Property("wgs84_pos:long")
    private String wgs84_long;

    @Property("wgs84_pos:alt")
    private String wgs84_alt;

    @Embedded("skos:prefLabel")
    private Set<prefLabel> skos_prefLabel;

    @Embedded("skos:altLabel")
    private Set<altLabel> skos_altLabel;

    @Embedded("skos:note")
    private Set<skosNote> skos_note;

    @Embedded("dcterms:hasPart")
    private Set<hasPart> dcterms_hasPart;

    @Embedded("dcterms:isPartOf")
    private Set<isPartOf> dcterms_isPartOf;

    @Embedded("QualityDetails:isNextInSequence")
    private isNexInSequence edm_isNextInSequence;

    @Embedded("owl:sameAs")
    private Set<sameAs> owl_sameAs;

    @Embedded("errorList")
    private Set<Error> errorList = new HashSet<>();

    public Place() {
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWgs84_lat() {
        return wgs84_lat;
    }

    public void setWgs84_lat(String wgs84_lat) {
        this.wgs84_lat = wgs84_lat;
    }

    public String getWgs84_long() {
        return wgs84_long;
    }

    public void setWgs84_long(String wgs84_long) {
        this.wgs84_long = wgs84_long;
    }

    public String getWgs84_alt() {
        return wgs84_alt;
    }

    public void setWgs84_alt(String wgs84_alt) {
        this.wgs84_alt = wgs84_alt;
    }

    public Set<prefLabel> getSkos_prefLabel() {
        return skos_prefLabel;
    }

    public void setSkos_prefLabel(Set<prefLabel> skos_prefLabel) {
        this.skos_prefLabel = skos_prefLabel;
    }

    public Set<altLabel> getSkos_altLabel() {
        return skos_altLabel;
    }

    public void setSkos_altLabel(Set<altLabel> skos_altLabel) {
        this.skos_altLabel = skos_altLabel;
    }

    public Set<skosNote> getSkos_note() {
        return skos_note;
    }

    public void setSkos_note(Set<skosNote> skos_note) {
        this.skos_note = skos_note;
    }

    public Set<hasPart> getDcterms_hasPart() {
        return dcterms_hasPart;
    }

    public void setDcterms_hasPart(Set<hasPart> dcterms_hasPart) {
        this.dcterms_hasPart = dcterms_hasPart;
    }

    public Set<isPartOf> getDcterms_isPartOf() {
        return dcterms_isPartOf;
    }

    public void setDcterms_isPartOf(Set<isPartOf> dcterms_isPartOf) {
        this.dcterms_isPartOf = dcterms_isPartOf;
    }

    public isNexInSequence getEdm_isNextInSequence() {
        return edm_isNextInSequence;
    }

    public void setEdm_isNextInSequence(isNexInSequence edm_isNextInSequence) {
        this.edm_isNextInSequence = edm_isNextInSequence;
    }

    public Set<sameAs> getOwl_sameAs() {
        return owl_sameAs;
    }

    public void setOwl_sameAs(Set<sameAs> owl_sameAs) {
        this.owl_sameAs = owl_sameAs;
    }

    public Set<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(Set<Error> errorList) {
        this.errorList = errorList;
    }

    @Override
    public String toString() {
        JsonAdapter<Place> jsonAdapter = new Moshi.Builder().build().adapter(Place.class);
        return jsonAdapter.toJson(this);
    }

    @Embedded
    public static class prefLabel extends DataType {

        @Property("value")
        private String value;

        @Property("lang")
        private String lang;

        public prefLabel(String value, String lang) {
            this.value = value;
            this.lang = lang;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }
    }

    @Embedded
    public static class altLabel {

        @Property("value")
        private String value;

        @Property("lang")
        private String lang;

        public altLabel(String value, String lang) {
            this.value = value;
            this.lang = lang;
        }

        public altLabel() {
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }


    }

    @Embedded
    public static class skosNote {

        @Property("value")
        private String value;

        @Property("lang")
        private String lang;

        public skosNote(String value, String lang) {
            this.value = value;
            this.lang = lang;
        }

        public skosNote() {
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }
    }

    @Embedded
    public static class hasPart {

        @Property("resource")
        private String resource;

        @Property("value")
        private String value;

        @Property("lang")
        private String lang;

        public hasPart(String resource, String value, String lang) {
            this.resource = resource;
            this.value = value;
            this.lang = lang;
        }

        public hasPart() {
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }
    }

    @Embedded
    public static class isPartOf {

        @Property("resource")
        private String resource;

        @Property("value")
        private String value;

        @Property("lang")
        private String lang;

        public isPartOf(String resource, String value, String lang) {
            this.resource = resource;
            this.value = value;
            this.lang = lang;
        }

        public isPartOf() {
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }
    }

    @Embedded
    public static class isNexInSequence {

        @Property("resource")
        private String resource;

        public isNexInSequence(String resource) {
            this.resource = resource;
        }

        public isNexInSequence() {
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }
    }

    @Embedded
    public static class sameAs {

        @Property("resource")
        private String resource;

        public sameAs(String resource) {
            this.resource = resource;
        }

        public sameAs() {
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }
    }
}
