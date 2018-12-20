package org.csuc.service.injection;

import org.csuc.App;
import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.glassfish.hk2.api.Factory;

import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 * @author amartinez
 */
public class ApplicationBinderFactory implements Factory<Application> {

    private URL file = getClass().getClassLoader().getResource("echoes-gui-server.conf");

    @Override
    public Application provide() {
        return new ServerConfig((Objects.isNull(file)) ? null : new File(file.getFile()).toPath()).getConfig();
    }

    @Override
    public void dispose(Application application) {

    }
}
