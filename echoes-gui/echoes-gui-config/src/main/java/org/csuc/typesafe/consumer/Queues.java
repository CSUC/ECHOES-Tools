package org.csuc.typesafe.consumer;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * @author amartinez
 */
public class Queues {

    private String analyse;
    private String recollect;
    private String validation;
    private String zip;
    private String loader;

    public Queues() {
    }

    public String getAnalyse() {
        return analyse;
    }

    public void setAnalyse(String analyse) {
        this.analyse = analyse;
    }

    public String getRecollect() {
        return recollect;
    }

    public void setRecollect(String recollect) {
        this.recollect = recollect;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getLoader() {
        return loader;
    }

    public void setLoader(String loader) {
        this.loader = loader;
    }

    @Override
    public String toString() {
        JsonAdapter<Queues> jsonAdapter = new Moshi.Builder().build().adapter(Queues.class);
        return jsonAdapter.toJson(this);
    }
}
