package org.csuc.typesafe.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author amartinez
 */
public class Application {

    private String folder;

    public Application() {
    }

    public String getFolder() throws IOException {
        Path path = Paths.get(folder);
        if(!Files.exists(path))
            Files.createDirectories(path);
        return path.toString();
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getParserFolder(String uuid) throws IOException {
        Path path = Paths.get(folder + File.separator + "parser" + File.separator + uuid);
        if(!Files.exists(path))
            Files.createDirectories(path);
        return path.toString();
    }

    public String getRecollectFolder(String uuid) throws IOException {
        Path path = Paths.get(folder + File.separator + "recollect" + File.separator + uuid);
        if(!Files.exists(path))
            Files.createDirectories(path);
        return path.toString();
    }

    public String getValidationFolder(String uuid) throws IOException {
        Path path = Paths.get(folder + File.separator + "validation" + File.separator + uuid);
        if(!Files.exists(path))
            Files.createDirectories(path);
        return path.toString();
    }

    @Override
    public String toString() {
        JsonAdapter<Application> jsonAdapter = new Moshi.Builder().build().adapter(Application.class);
        return jsonAdapter.toJson(this);
    }
}
