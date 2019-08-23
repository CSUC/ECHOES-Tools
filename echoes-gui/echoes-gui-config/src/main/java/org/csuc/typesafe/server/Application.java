package org.csuc.typesafe.server;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigRenderOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author amartinez
 */
public class Application {

    private Config config;

    private String folder;
    private String host;
    private int port;

    private MongoDB mongoDB;

    public Application(Config config) {
        this.config = config;
        this.mongoDB = new MongoDB(config.getConfig("mongodb"));
    }

    public String getFolder() throws IOException {
        Path path = Paths.get(config.getString("folder"));
        if(!Files.exists(path))
            Files.createDirectories(path);
        return path.toString();
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getParserFolder(String uuid) throws IOException {
        Path path = Paths.get(getFolder() + File.separator + "analyse" + File.separator + uuid);
        if(!Files.exists(path))
            Files.createDirectories(path);
        return path.toString();
    }

    public String getRecollectFolder(String uuid) throws IOException {
        Path path = Paths.get(getFolder() + File.separator + "transformation" + File.separator + uuid);
        if(!Files.exists(path))
            Files.createDirectories(path);
        return path.toString();
    }

    public String getQualityFolder(String uuid) throws IOException {
        Path path = Paths.get(getFolder() + File.separator + "quality" + File.separator + uuid);
        if(!Files.exists(path))
            Files.createDirectories(path);
        return path.toString();
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

    public MongoDB getMongoDB() {
        return mongoDB;
    }

    public void setMongoDB(MongoDB mongoDB) {
        this.mongoDB = mongoDB;
    }

    @Override
    public String toString() {
        return config.root().render(ConfigRenderOptions.concise());
    }
}
