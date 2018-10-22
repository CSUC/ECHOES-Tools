package org.csuc.typesafe.authoritzation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthoritzationConfigTest {

    private static Logger logger = LogManager.getLogger(AuthoritzationConfigTest.class);

    @Test
    public void test(){
        assertNotNull(AuthoritzationConfig.DOMAIN);
        assertNotNull(AuthoritzationConfig.CLIENT_ID);
        assertNotNull(AuthoritzationConfig.CLIENT_SECRET);
    }

}