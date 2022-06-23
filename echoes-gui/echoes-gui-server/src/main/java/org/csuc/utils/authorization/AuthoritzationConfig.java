package org.csuc.utils.authorization;

import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author amartinez
 */
public class AuthoritzationConfig extends ConfigBeanFactory{

    private static Logger logger = LogManager.getLogger(AuthoritzationConfig.class);

    public static String DOMAIN = ConfigFactory.parseResources("auth0.env").getString("DOMAIN");
    public static String CLIENT_ID = ConfigFactory.parseResources("auth0.env").getString("CLIENT_ID");
    public static String CLIENT_SECRET = ConfigFactory.parseResources("auth0.env").getString("CLIENT_SECRET");



}
