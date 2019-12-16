package org.csuc.dao.entity.edm;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.adapter.BigIntegerAdapter;
import org.csuc.dao.entity.Error;
import org.mongodb.morphia.annotations.Embedded;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class License {

    @Embedded("errorList")
    private Set<Error> errorList = new HashSet<>();

    @Embedded("data")
    private eu.europeana.corelib.definitions.jibx.License data = new eu.europeana.corelib.definitions.jibx.License();

    public License() {
    }

    public Set<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(Set<Error> errorList) {
        this.errorList = errorList;
    }

    public eu.europeana.corelib.definitions.jibx.License getData() {
        return data;
    }

    public void setData(eu.europeana.corelib.definitions.jibx.License data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JsonAdapter<License> jsonAdapter =
                new Moshi.Builder()
                        .add(BigInteger.class, new BigIntegerAdapter().nullSafe())
                        .add(Object.class)
                        .build().adapter(License.class);

        return jsonAdapter.toJson(this);
    }
}
