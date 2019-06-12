package org.csuc.entities.quality.edm;

import org.mongodb.morphia.annotations.*;

import java.util.List;

@Entity(value = "places", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
@Validation("{$and:[{wgs84_pos:lat:{$ne:null}},{wgs84_pos:long:{$ne:null}}]}")
public class Places {

    @Id
    private String _id;

    @Property("wgs84_pos:lat")
    private String lat;

    @Property("wgs84_pos:long")
    private String lon;

    @Property("wgs84_pos:alt")
    private String alt;

    //Literal
    @Property("skos:prefLabel")
    private List<String> prefLabel;

    //Literal
    @Property("skos:altLabel")
    private List<String> altLabel;

    //Literal
    @Property("skos:note")
    private List<String> note;

    //Literal or Reference
    @Property("dcterms:hasPart")
    private String hasPart;

    //Literal or Reference
    @Property("dcterms:isPartOf")
    private String isPartOf;

    //Reference
    @Property("edm:isNextInSequence")
    private String isNextInSequence;

    //Reference
    @Property("owl:sameAs")
    private String sameAs;

    public Places() {
    }

    public Places(String uuid) {
        this._id = uuid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public List<String> getPrefLabel() {
        return prefLabel;
    }

    public void setPrefLabel(List<String> prefLabel) {
        this.prefLabel = prefLabel;
    }

    public List<String> getAltLabel() {
        return altLabel;
    }

    public void setAltLabel(List<String> altLabel) {
        this.altLabel = altLabel;
    }

    public List<String> getNote() {
        return note;
    }

    public void setNote(List<String> note) {
        this.note = note;
    }

    public String getHasPart() {
        return hasPart;
    }

    public void setHasPart(String hasPart) {
        this.hasPart = hasPart;
    }

    public String getIsPartOf() {
        return isPartOf;
    }

    public void setIsPartOf(String isPartOf) {
        this.isPartOf = isPartOf;
    }

    public String getIsNextInSequence() {
        return isNextInSequence;
    }

    public void setIsNextInSequence(String isNextInSequence) {
        this.isNextInSequence = isNextInSequence;
    }

    public String getSameAs() {
        return sameAs;
    }

    public void setSameAs(String sameAs) {
        this.sameAs = sameAs;
    }
}
