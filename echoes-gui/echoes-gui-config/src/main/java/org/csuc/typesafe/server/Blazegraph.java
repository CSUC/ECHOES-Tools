package org.csuc.typesafe.server;

import com.typesafe.config.Config;

/**
 * @author amartinez
 */
public class Blazegraph {

    private String url;
    private String host;
    private int port;
    private String namespace;

    private Config config;

    public Blazegraph(Config config) {
        this.config = config;
    }

    public String getUrl() {
        return config.getString("url");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNamespace() {
        return config.getString("namespace");
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getHost() {
        return config.getString("host");
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return config.getInt("port");
    }

    public void setPort(int port) {
        this.port = port;
    }
}
