package org.csuc;

import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.eclipse.jetty.nosql.mongodb.MongoSessionIdManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

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
                        .register(JacksonFeature.class);

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
