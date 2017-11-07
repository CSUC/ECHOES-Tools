/**
 * 
 */
package org.csuc.rest.api.utils;

import java.util.List;
import java.util.Objects;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.dao.UserDAO;
import org.Morphia.Core.dao.impl.UserDAOImpl;
import org.Morphia.Core.entities.User;
import org.Morphia.Core.utils.Password;
import org.Morphia.Core.utils.Role;
import org.csuc.rest.api.typesafe.ApplicationConfig;
import org.csuc.rest.api.typesafe.TypesafeMongoDB;
import org.csuc.rest.api.utils.jwt.JWTCreate;
import org.csuc.rest.api.utils.jwt.JWTDecode;
import org.csuc.rest.api.utils.jwt.JWTVerification;
import org.csuc.rest.api.utils.jwt.Token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * 
 * 
 * @author amartinez
 *
 */
public class Auth {

	private TypesafeMongoDB mongodbConf = new ApplicationConfig().getMongodbConfig();

	private MorphiaEchoes echoes = new MorphiaEchoes(mongodbConf.getHost(), mongodbConf.getPort(),
			mongodbConf.getDatabase());

	private String username;
	private String password;

	private String token;

	private UserDAO dao = new UserDAOImpl(User.class, echoes.getDatastore());
	private User user;

	/**
	 * 
	 * @param username
	 * @param password
	 */
	public Auth(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * 
	 * @param token
	 */
	public Auth(String token) {
		this.token = token;
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void authenticate() throws Exception {
		user = dao.findById(username);
		if (Objects.nonNull(user)) {
			Password psswd = new Password(user.getDigest(), password);
			if (!(user.getId().equals(username) && user.getPassword().equals(psswd.getSecurePassword())))
				throw new Exception();
		} else
			throw new Exception();
	}

	/**
	 * 
	 * @return
	 */
	public Token issueToken() throws Exception {
		String token = new JWTCreate(user.getId(), Password.getSecurePassword(password, user.getDigest()),
				user.getRole().name()).sign();

		DecodedJWT jwtd = JWT.decode(token);

		return new Token(jwtd.getToken(), jwtd.getExpiresAt().getTime(), jwtd.getIssuedAt().getTime(), jwtd.getId());
	}

	/**
	 * Check if the user contains one of the allowed roles
	 * 
	 * Throw an Exception if the user has not permission to execute the method
	 * 
	 * 
	 * @param allowedRoles
	 * @param token
	 * @throws Exception
	 */
	public User validateToken(List<Role> allowedRoles) throws Exception {		
		DecodedJWT jwtd = new JWTDecode(token).decode();
		
		if(Objects.isNull(jwtd))	throw new Exception();
		else {
			User user = dao.findById(jwtd.getIssuer());
			if(Objects.isNull(user))	throw new Exception();
			else {
				DecodedJWT jwtvd = new JWTVerification(user.getPassword(), jwtd.getToken()).verify();
				if(Objects.isNull(jwtvd) || !allowedRoles.contains(user.getRole()))	throw new Exception();				
				return user;
			}
		}		
	}

}
