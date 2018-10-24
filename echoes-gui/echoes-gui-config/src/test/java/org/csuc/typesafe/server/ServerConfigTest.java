package org.csuc.typesafe.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServerConfigTest {

    private static Logger logger = LogManager.getLogger(ServerConfigTest.class);

    @Test
    public void getConfig() {
        Application application = new ServerConfig(null).getConfig();

        assertNotNull(application);

        logger.info(application.toString());
    }
}