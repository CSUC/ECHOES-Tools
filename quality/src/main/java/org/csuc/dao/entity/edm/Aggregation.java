package org.csuc.dao.entity.edm;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.dao.entity.Error;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashSet;
import java.util.Set;

public class Aggregation {

    @Embedded("errorList")
    private Set<Error> errorList = new HashSet<>();

    @Embedded("data")
    private eu.europeana.corelib.definitions.jibx.Aggregation data = new eu.europeana.corelib.definitions.jibx.Aggregation();

    public Aggregation() {
    }

    public Set<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(Set<Error> errorList) {
        this.errorList = errorList;
    }

    public eu.europeana.corelib.definitions.jibx.Aggregation getData() {
        return data;
    }

    public void setData(eu.europeana.corelib.definitions.jibx.Aggregation data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JsonAdapter<Aggregation> jsonAdapter = new Moshi.Builder().build().adapter(Aggregation.class);
        return jsonAdapter.toJson(this);
    }
}
