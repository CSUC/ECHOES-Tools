package org.csuc.typesafe.consumer;

import com.typesafe.config.Config;

/**
 * @author amartinez
 */
public class Queues {

    private Config analyse;
    private Config recollect;
    private Config validation;
    private Config zip;
    private Config loader;

    public Queues(Config config) {
        analyse = config.getConfig("analyse");
        recollect = config.getConfig("recollect");
        validation = config.getConfig("validation");
        zip = config.getConfig("zip");
        loader = config.getConfig("loader");
    }

    public Config getAnalyse() {
        return analyse;
    }

    public void setAnalyse(Config analyse) {
        this.analyse = analyse;
    }

    public Config getRecollect() {
        return recollect;
    }

    public void setRecollect(Config recollect) {
        this.recollect = recollect;
    }

    public Config getValidation() {
        return validation;
    }

    public void setValidation(Config validation) {
        this.validation = validation;
    }

    public Config getZip() {
        return zip;
    }

    public void setZip(Config zip) {
        this.zip = zip;
    }

    public Config getLoader() {
        return loader;
    }

    public void setLoader(Config loader) {
        this.loader = loader;
    }
}
