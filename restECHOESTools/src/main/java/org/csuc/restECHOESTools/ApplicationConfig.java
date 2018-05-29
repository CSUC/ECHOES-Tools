package org.csuc.restECHOESTools;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * @author amartinez
 */
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        packages("org.csuc.rest.api");

        register(LoggingFilter.class);
        register(MyResource.class);

    }

}