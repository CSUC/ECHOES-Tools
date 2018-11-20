package org.csuc;

import org.eclipse.jetty.nosql.mongodb.MongoSessionIdManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 *
 */
public class App {

    public static void main(String[] args) throws Exception {
        Log.setLog(new Slf4jLog());

        final ResourceConfig application =
                new ResourceConfig()
                        .packages("org.csuc.service")
                        .register(JacksonFeature.class);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        MongoSessionIdManager idMgr = new MongoSessionIdManager(jettyServer);
        idMgr.setWorkerName("node0");
        idMgr.setScavengePeriod(60);

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
