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
import org.glassfish.jersey.internal.util.Base64;

/**
 * @author amartinez
 *
 */
public class Auth {

	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");
	
	private String username;
	private String password;
	
	private String token;

	public Auth(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public Auth(String token) {
		this.token = token;		
	}

	public void authenticate() throws Exception {
		UserDAO userDAO = new UserDAOImpl(User.class, echoes.getDatastore());
		User user = userDAO.findById(username);
		
		if(Objects.nonNull(user)) {
			Password psswd = new Password(user.getDigest(), password);
        	if(!(user.getId().equals(username) && user.getPassword().equals(psswd.getSecurePassword())))	throw new Exception();
        }else throw new Exception(); 
	}

	public String issueToken() {
		String authString = username + ":" + password;
        String authStringEnc = Base64.encodeAsString(authString.getBytes());
        
		return String.format("Authorization: Bearer %s", authStringEnc);
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
		String decoded = Base64.decodeAsString(token);
		String[] split = decoded.split(":");

		UserDAO userDAO = new UserDAOImpl(User.class, echoes.getDatastore());
		User user = userDAO.findById(split[0]);

		Password psswd = new Password(user.getDigest(), split[1]);
		if (user == null || !user.getPassword().equals(psswd.getSecurePassword())
				|| !allowedRoles.contains(user.getRole()))
			throw new Exception();
		
		return user;
	}
}
