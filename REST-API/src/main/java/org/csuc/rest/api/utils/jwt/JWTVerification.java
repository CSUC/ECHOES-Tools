/**
 * 
 */
package org.csuc.rest.api.utils.jwt;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author amartinez
 *
 */
public class JWTVerification {

	private static Logger logger = LogManager.getLogger(JWTVerification.class);

	private JWTVerifier verifier;
	private String token;
	private String secret;

	public JWTVerification(String secret, String token) {

		this.token = token;
		this.secret = secret;
	}

	public DecodedJWT verify() {
		try {
			verifier = JWT.require(Algorithm.HMAC256(secret)).acceptLeeway(1) // 1
					.acceptExpiresAt(5) // 5 secs for exp
					.build();

			return verifier.verify(token);
		} catch (UnsupportedEncodingException | JWTVerificationException exception) {
			// UTF-8 encoding not supported and Invalid signature/claims
			logger.error(exception);
			return null;
		}
	}
}
