package org.csuc.utils.authorization;


import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

import static org.csuc.utils.authorization.AuthoritzationConfig.DOMAIN;

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
