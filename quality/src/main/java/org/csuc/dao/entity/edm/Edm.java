package org.csuc.dao.entity.edm;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.adapter.BigIntegerAdapter;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity("content")
public class Edm {

    @Embedded("edm:ProvidedCHO")
    private List<ProvidedCHO> providedCHO = new ArrayList<>();

    @Embedded("edm:Place")
    private List<Place> place = new ArrayList<>();

    @Embedded("skos:Concept")
    private List<Concept> concept = new ArrayList<>();

    @Embedded("edm:WebResource")
    private List<WebResource> webResource = new ArrayList<>();

    @Embedded("edm:Agent")
    private List<Agent> agent = new ArrayList<>();

    @Embedded("ore:Aggregation")
    private List<Aggregation> aggregation = new ArrayList<>();

    @Embedded("edm:TimeSpan")
    private List<TimeSpan> timeSpan = new ArrayList<>();

//    @Embedded("cc:License")
//    private List license;

//    @Embedded("svcs:Service")
//    private List service;

    public Edm() {
    }

    public List<ProvidedCHO> getProvidedCHO() {
        return providedCHO;
    }

    public void setProvidedCHO(List<ProvidedCHO> providedCHO) {
        this.providedCHO = providedCHO;
    }

    public List<Place> getPlace() {
        return place;
    }

    public void setPlace(List<Place> place) {
        this.place = place;
    }

    public List<Concept> getConcept() {
        return concept;
    }

    public void setConcept(List<Concept> concept) {
        this.concept = concept;
    }

    public List<Agent> getAgent() {
        return agent;
    }

    public void setAgent(List<Agent> agent) {
        this.agent = agent;
    }

    public List<Aggregation> getAggregation() {
        return aggregation;
    }

    public void setAggregation(List<Aggregation> aggregation) {
        this.aggregation = aggregation;
    }

    public List<TimeSpan> getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(List<TimeSpan> timeSpan) {
        this.timeSpan = timeSpan;
    }

    public List<WebResource> getWebResource() {
        return webResource;
    }

    public void setWebResource(List<WebResource> webResource) {
        this.webResource = webResource;
    }

    @Override
    public String toString() {
        JsonAdapter<Edm> jsonAdapter =
                new Moshi.Builder()
                        .add(BigInteger.class, new BigIntegerAdapter().nullSafe())
                        .build().adapter(Edm.class);

        return jsonAdapter.toJson(this);
    }
}
