package org.csuc.typesafe.server;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigRenderOptions;

import java.util.Map;

/**
 * @author amartinez
 */
public class MongoDB {

    private Config config;

    private String host;
    private int port;
    private String database;

    private Map<String, Object> options;

    public MongoDB(Config config) {
        this.config = config;
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

    public String getDatabase() {
        return config.getString("database");
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public ConfigObject getOptions() {
        return config.getObject("options");
    }

    public void setOptions(Map options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return config.root().render(ConfigRenderOptions.concise());
    }
}
