package org.csuc;

import org.csuc.client.Client;
import org.csuc.service.injection.ApplicationBinderFactory;
import org.csuc.service.injection.ClientBinderFactory;
import org.csuc.service.injection.RabbitMQBinderFactory;
import org.csuc.typesafe.consumer.RabbitMQConfig;
import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.eclipse.jetty.nosql.mongodb.MongoSessionIdManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Objects;

/**
 *
 *
 */
public class App {

    public static void main(String[] args) throws Exception {
        Log.setLog(new Slf4jLog());

        URL file = App.class.getClassLoader().getResource("echoes-gui-server.conf");
        Application config = new ServerConfig((Objects.isNull(file)) ? null : new File(file.getFile()).toPath()).getConfig();

        final ResourceConfig application =
                new ResourceConfig()
                        .packages("org.csuc.service")
                        .register(JacksonFeature.class)
                        .register(new AbstractBinder() {
                            @Override
                            protected void configure() {
                                bindFactory(ClientBinderFactory.class).to(Client.class).in(Singleton.class);
                            }
                        })
                        .register(new AbstractBinder() {
                            @Override
                            protected void configure() {
                                bindFactory(ApplicationBinderFactory.class).to(Application.class).in(Singleton.class);
                            }
                        })
                        .register(new AbstractBinder() {
                            @Override
                            protected void configure() {
                                bindFactory(RabbitMQBinderFactory.class).to(RabbitMQConfig.class).in(Singleton.class);
                            }
                        });

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        InetSocketAddress inetSocketAddress = new InetSocketAddress(config.getHost(), config.getPort());
        Server jettyServer = new Server(inetSocketAddress);
        jettyServer.setHandler(context);

        MongoSessionIdManager idMgr = new MongoSessionIdManager(jettyServer);
        idMgr.setWorkerName(config.getMongoDB().getOptions().get("workerName").render());
        idMgr.setScavengePeriod(Long.parseLong(config.getMongoDB().getOptions().get("scavengePeriod").render()));

        jettyServer.setSessionIdManager(idMgr);

        ServletHolder jerseyServlet =
                new ServletHolder(new org.glassfish.jersey.servlet.ServletContainer(application));
        jerseyServlet.setInitOrder(0);

        context.addServlet(jerseyServlet, "/rest/*");

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }
}
