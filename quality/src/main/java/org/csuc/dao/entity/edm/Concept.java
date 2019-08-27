package org.csuc.dao.entity.edm;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.dao.entity.Error;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashSet;
import java.util.Set;

public class Concept {

    @Embedded("errorList")
    private Set<Error> errorList = new HashSet<>();

    @Embedded("data")
    private eu.europeana.corelib.definitions.jibx.Concept data = new eu.europeana.corelib.definitions.jibx.Concept();

    public Concept() {
    }

    public Set<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(Set<Error> errorList) {
        this.errorList = errorList;
    }

    public eu.europeana.corelib.definitions.jibx.Concept getData() {
        return data;
    }

    public void setData(eu.europeana.corelib.definitions.jibx.Concept data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JsonAdapter<Concept> jsonAdapter = new Moshi.Builder().build().adapter(Concept.class);
        return jsonAdapter.toJson(this);
    }
}
