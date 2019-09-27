package org.csuc.dao.entity.edm;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import eu.europeana.corelib.definitions.jibx.TimeSpanType;
import org.csuc.dao.entity.Error;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashSet;
import java.util.Set;

public class TimeSpan {

    @Embedded("errorList")
    private Set<Error> errorList = new HashSet<>();

    @Embedded("data")
    private TimeSpanType data = new TimeSpanType();

    public TimeSpan() {
    }

    public Set<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(Set<Error> errorList) {
        this.errorList = errorList;
    }

    public TimeSpanType getData() {
        return data;
    }

    public void setData(TimeSpanType data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JsonAdapter<TimeSpan> jsonAdapter = new Moshi.Builder().build().adapter(TimeSpan.class);
        return jsonAdapter.toJson(this);
    }
}
