package org.csuc.typesafe;

import org.junit.Test;

import static org.junit.Assert.*;

public class HDFSConfigTest {

    @Test
    public void config() {

        HDFSConfig config = HDFSConfig.config(null);

        assertEquals("hdfs://localhost:54310", config.getUri());
        assertEquals("hdfs", config.getUser());
        assertEquals("/user/hdfs/echoes", config.getHome());
    }
}