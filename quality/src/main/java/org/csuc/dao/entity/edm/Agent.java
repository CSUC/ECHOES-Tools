package org.csuc.dao.entity.edm;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import eu.europeana.corelib.definitions.jibx.AgentType;
import org.csuc.dao.entity.Error;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashSet;
import java.util.Set;

public class Agent {

    @Embedded("errorList")
    private Set<Error> errorList = new HashSet<>();

    @Embedded("data")
    private AgentType data = new AgentType();

    public Agent() {
    }

    public Set<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(Set<Error> errorList) {
        this.errorList = errorList;
    }

    public AgentType getData() {
        return data;
    }

    public void setData(AgentType data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JsonAdapter<Agent> jsonAdapter = new Moshi.Builder().build().adapter(Agent.class);
        return jsonAdapter.toJson(this);
    }
}
