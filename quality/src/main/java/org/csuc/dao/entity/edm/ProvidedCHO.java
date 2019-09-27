package org.csuc.dao.entity.edm;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import eu.europeana.corelib.definitions.jibx.ProvidedCHOType;
import org.csuc.dao.entity.Error;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashSet;
import java.util.Set;

public class ProvidedCHO {

    @Embedded("errorList")
    private Set<Error> errorList = new HashSet<>();

    @Embedded("data")
    private ProvidedCHOType data = new ProvidedCHOType();

    public ProvidedCHO() {
    }

    public Set<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(Set<Error> errorList) {
        this.errorList = errorList;
    }

    public ProvidedCHOType getData() {
        return data;
    }

    public void setData(ProvidedCHOType data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JsonAdapter<ProvidedCHO> jsonAdapter = new Moshi.Builder().build().adapter(ProvidedCHO.class);
        return jsonAdapter.toJson(this);
    }
}
