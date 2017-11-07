/**
 * 
 */
package org.csuc.rest.api;

import java.util.Objects;

import javax.ws.rs.Priorities;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.dao.UserDAO;
import org.Morphia.Core.dao.impl.UserDAOImpl;
import org.Morphia.Core.utils.Password;
import org.Morphia.Core.utils.Role;
import org.csuc.rest.api.filters.AuthenticationFilter;
import org.csuc.rest.api.service.Authentication;
import org.csuc.rest.api.service.User;
import org.csuc.rest.api.typesafe.ApplicationConfig;
import org.csuc.rest.api.typesafe.TypesafeMongoDB;
import org.csuc.rest.api.typesafe.TypesafeUser;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * 
 * Best practice for REST token-based authentication with JAX-RS and Jersey
 * 
 * https://github.com/Posya/wiki/blob/master/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey.adoc
 * 
 * @author amartinez
 *
 */
@SuppressWarnings("deprecation")
public class CustomApplication extends ResourceConfig {

	public CustomApplication() {
		packages("org.csuc.rest.api");

		register(LoggingFilter.class);
		register(Authentication.class);
		register(User.class);
		register(Priorities.AUTHENTICATION);

		// Register Auth Filter here
		register(AuthenticationFilter.class);

		// Create Admin if not exists
		createAdministrator();		
	}

	/**
	 * Insert default user if not exists
	 * 
	 * 
	 */
	private void createAdministrator() {
		ApplicationConfig conf = new ApplicationConfig();

		TypesafeUser userConf = conf.getUserConfig();
		TypesafeMongoDB mongodbConf = conf.getMongodbConfig();
		
		UserDAO dao = new UserDAOImpl(org.Morphia.Core.entities.User.class,
				new MorphiaEchoes(mongodbConf.getDatabase()).getDatastore());
		
		if (Objects.isNull(dao.findById(userConf.getUsername()))) {
			org.Morphia.Core.entities.User user = new org.Morphia.Core.entities.User(userConf.getUsername());
			user.setPassword(Password.getSecurePassword(userConf.getUsername(), userConf.getDigest()));
			user.setDigest(userConf.getDigest());
			
			user.setRole(Role.Admin);

			dao.save(user);
		}		
	}

}
