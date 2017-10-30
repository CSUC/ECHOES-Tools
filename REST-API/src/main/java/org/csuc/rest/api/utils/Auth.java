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

/**
 * 
 * 
 * @author amartinez
 *
 */
public class Auth {

	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");
	
	private String username;
	private String password;
	
	private String token;

	private UserDAO userDAO = new UserDAOImpl(User.class, echoes.getDatastore());
	
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
		User user = userDAO.findById(username);
		
		if(Objects.nonNull(user)) {
			Password psswd = new Password(user.getDigest(), password);
        	if(!(user.getId().equals(username) && user.getPassword().equals(psswd.getSecurePassword())))	throw new Exception();
        }else throw new Exception(); 
	}

	/**
	 * 
	 * @return
	 */
	public TokenResponse issueToken() {		
		return new TokenResponse(userDAO.findById(username).getToken());	
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
		User user = userDAO.findByToken(token);
		if (Objects.isNull(user) || !allowedRoles.contains(user.getRole()))	throw new Exception();
			
		return user;
	}
	
}
