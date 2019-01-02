package org.csuc.typesafe.consumer;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.typesafe.config.Config;

/**
 * @author amartinez
 */
public class RabbitMQConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private int portManagement;
    private Queues queues;

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

    public Queues getQueues() {
        return queues;
    }

    public void setQueues(Queues queues) {
        this.queues = queues;
    }

    @Override
    public String toString() {
        JsonAdapter<RabbitMQConfig> jsonAdapter = new Moshi.Builder().build().adapter(RabbitMQConfig.class);
        return jsonAdapter.toJson(this);
    }
}
