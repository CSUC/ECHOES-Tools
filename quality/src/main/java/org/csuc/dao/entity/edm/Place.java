package org.csuc.dao.entity.edm;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import eu.europeana.corelib.definitions.jibx.PlaceType;
import org.csuc.dao.entity.Error;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashSet;
import java.util.Set;

/**
 * @author amartinez
 */
public class Place {

    @Embedded("errorList")
    private Set<Error> errorList = new HashSet<>();

    @Embedded("data")
    private PlaceType data = new PlaceType();

    public Place() {
    }

    public Set<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(Set<Error> errorList) {
        this.errorList = errorList;
    }

    public PlaceType getData() {
        return data;
    }

    public void setData(PlaceType data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JsonAdapter<Place> jsonAdapter = new Moshi.Builder().build().adapter(Place.class);
        return jsonAdapter.toJson(this);
    }
}
