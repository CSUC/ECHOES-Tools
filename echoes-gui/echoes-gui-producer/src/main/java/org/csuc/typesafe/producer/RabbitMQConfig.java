package org.csuc.typesafe.producer;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * @author amartinez
 */
public class RabbitMQConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private int portManagement;
    private String parserQueue;
    private String recollectQueue;
    private String validationQueue;
    private String zipQueue;

    public RabbitMQConfig() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPortManagement() {
        return portManagement;
    }

    public void setPortManagement(int portManagement) {
        this.portManagement = portManagement;
    }

    public String getParserQueue() {
        return parserQueue;
    }

    public void setParserQueue(String parserQueue) {
        this.parserQueue = parserQueue;
    }

    public String getRecollectQueue() {
        return recollectQueue;
    }

    public void setRecollectQueue(String recollectQueue) {
        this.recollectQueue = recollectQueue;
    }

    public String getValidationQueue() {
        return validationQueue;
    }

    public void setValidationQueue(String validationQueue) {
        this.validationQueue = validationQueue;
    }

    public String getZipQueue() {
        return zipQueue;
    }

    public void setZipQueue(String zipQueue) {
        this.zipQueue = zipQueue;
    }

    @Override
    public String toString() {
        JsonAdapter<RabbitMQConfig> jsonAdapter = new Moshi.Builder().build().adapter(RabbitMQConfig.class);
        return jsonAdapter.toJson(this);
    }
}
