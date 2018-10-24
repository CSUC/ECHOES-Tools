package org.csuc.utils.authorization;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.util.Objects;

import static org.csuc.typesafe.authoritzation.AuthoritzationConfig.DOMAIN;

/**
 * @author amartinez
 */
public class TokenVerifier {

    private final Algorithm algorithm;
    private final JwkProvider jwkProvider;
    private final String issuer;
    private JWTVerifier verifier;


    public TokenVerifier(JwkProvider jwkProvider) {
        this.algorithm = null;
        this.jwkProvider = jwkProvider;
        this.issuer = toUrl(DOMAIN);
    }

    /**
     *
     * @param idToken
     * @return
     * @throws JwkException
     */
    public DecodedJWT verifyToken(String idToken) throws JwkException {
        if (Objects.nonNull(verifier)) return  verifier.verify(idToken);
        if(Objects.nonNull(algorithm)){
            verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            return verifier.verify(idToken);
        }
        String kid = JWT.decode(idToken).getKeyId();
        PublicKey publicKey = jwkProvider.get(kid).getPublicKey();

        return JWT.require(Algorithm.RSA256((RSAKey) publicKey))
                .withIssuer(issuer)
                .build()
                .verify(idToken);
    }


    /**
     *
     * @param domain
     * @return
     */
    private String toUrl(String domain) {
        String url = domain;
        if (!domain.startsWith("http://") && !domain.startsWith("https://"))    url = "https://" + domain;
        if (!url.endsWith("/")) url = url + "/";
        return url;
    }

}
