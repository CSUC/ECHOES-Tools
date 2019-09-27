package org.csuc.dao.entity.edm;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import eu.europeana.corelib.definitions.jibx.WebResourceType;
import org.csuc.adapter.BigIntegerAdapter;
import org.csuc.dao.entity.Error;
import org.mongodb.morphia.annotations.Embedded;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class WebResource {

    @Embedded("errorList")
    private Set<Error> errorList = new HashSet<>();

    @Embedded("data")
    private WebResourceType data = new WebResourceType();

    public WebResource() {
    }

    public Set<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(Set<Error> errorList) {
        this.errorList = errorList;
    }

    public WebResourceType getData() {
        return data;
    }

    public void setData(WebResourceType data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JsonAdapter<WebResource> jsonAdapter =
                new Moshi.Builder()
                        .add(BigInteger.class, new BigIntegerAdapter().nullSafe())
                        .add(Object.class)
                        .build().adapter(WebResource.class);

        return jsonAdapter.toJson(this);
    }
}
