package org.csuc.typesafe.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ServerConfigTest {

    private static Logger logger = LogManager.getLogger(ServerConfigTest.class);

    @Test
    public void getConfig() throws IOException {
        Application application = new ServerConfig(null).getConfig();

        assertNotNull(application);

        System.out.println(application.getParserFolder("82fee5e1-7c77-499b-b51a-e6b9ff10e9d1"));
    }
}