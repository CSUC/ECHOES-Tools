/**
 * 
 */
package org.csuc.rest.api.utils.jwt;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.rest.api.typesafe.ApplicationConfig;
import org.csuc.rest.api.typesafe.TypesafeToken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

/**
 * @author amartinez
 *
 */
public class JWTCreate {

	private static Logger logger = LogManager.getLogger(JWTCreate.class);

	private Builder builder = JWT.create();
	private String id;
	private String password;
	private String roles;

	private TypesafeToken tkc = new ApplicationConfig().getTokenConfig();

	private Calendar c = Calendar.getInstance();

	public JWTCreate(String id, String password, String roles) {
		this.id = id;
		this.password = password;
		c.set(Calendar.DATE, tkc.getExpiresAt());
	}

	public String sign() {
		try {
			if (Objects.nonNull(id))
				builder.withIssuer(id);
			builder.withIssuedAt(new Date());
			builder.withExpiresAt(c.getTime());
			builder.withClaim("role", roles);

			builder.withJWTId(tkc.getToken_type());

			return builder.sign(Algorithm.HMAC256(password));
		} catch (IllegalArgumentException | JWTCreationException | UnsupportedEncodingException exception) {
			logger.error(exception);
			return null;
		}
	}
}
