package org.csuc.utils.authorization;


import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.UserInfo;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.net.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

import static org.csuc.typesafe.authoritzation.AuthoritzationConfig.CLIENT_ID;
import static org.csuc.typesafe.authoritzation.AuthoritzationConfig.CLIENT_SECRET;
import static org.csuc.typesafe.authoritzation.AuthoritzationConfig.DOMAIN;

/**
 * @author amartinez
 */
public class Authoritzation {

    private static Logger logger = LogManager.getLogger(Authoritzation.class);

    private JwkProvider jwkProvider;

    private String user;
    private String token;

    private DecodedJWT jwt;

    public Authoritzation(String user, String token) {
        jwkProvider = new UrlJwkProvider(DOMAIN);
        this.user = user;
        this.token = token;
    }

    public void execute() throws JwkException {
        TokenVerifier TokenVerifier = new TokenVerifier(jwkProvider);
        DecodedJWT jwt = TokenVerifier.verifyToken(token);
        if(!Objects.equals(user, jwt.getClaim("sub").asString()))   throw new JwkException("invalid user by token");
        logger.debug(jwt.getClaims());
    }

}
