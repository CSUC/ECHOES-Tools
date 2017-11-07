/**
 * 
 */
package org.csuc.rest.api.utils.jwt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * 
 * @author amartinez
 *
 */
public class JWTDecode {

	private static Logger logger = LogManager.getLogger(JWTDecode.class);

	private String token;

	public JWTDecode(String token) {
		this.token = token;
	}

	public DecodedJWT decode() {
		try {
			return JWT.decode(token);
		} catch (JWTDecodeException exception) {
			logger.error(exception);
			return null;
		}
	}
}
