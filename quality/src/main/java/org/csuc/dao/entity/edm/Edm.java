package org.csuc.dao.entity.edm;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

import java.util.Set;

@Entity("content")
public class Edm {

    @Property("rdf:about")
    private Object about;

    @Property("edm:ProvidedCHO")
    private Object providedCHO;

    @Property("edm:WebResource")
    private Object webResource;

    @Property("edm:Agent")
    private Object agent;

    @Embedded("edm:Place")
    private Set<Place> place;

    @Property("edm:TimeSpan")
    private Object timeSpan;

    @Property("skos:Concept")
    private Object concept;

    @Property("cc:License")
    private Object license;

    public Edm() {
    }

    public Object getAbout() {
        return about;
    }

    public void setAbout(Object about) {
        this.about = about;
    }

    public Object getProvidedCHO() {
        return providedCHO;
    }

    public void setProvidedCHO(Object providedCHO) {
        this.providedCHO = providedCHO;
    }

    public Object getWebResource() {
        return webResource;
    }

    public void setWebResource(Object webResource) {
        this.webResource = webResource;
    }

    public Object getAgent() {
        return agent;
    }

    public void setAgent(Object agent) {
        this.agent = agent;
    }

    public Set<Place> getPlace() {
        return place;
    }

    public void setPlace(Set<Place> place) {
        this.place = place;
    }

    public Object getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(Object timeSpan) {
        this.timeSpan = timeSpan;
    }

    public Object getConcept() {
        return concept;
    }

    public void setConcept(Object concept) {
        this.concept = concept;
    }

    public Object getLicense() {
        return license;
    }

    public void setLicense(Object license) {
        this.license = license;
    }

    @Override
    public String toString() {
        JsonAdapter<Edm> jsonAdapter = new Moshi.Builder().build().adapter(Edm.class);
        return jsonAdapter.toJson(this);
    }
}
