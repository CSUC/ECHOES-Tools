/**
 * 
 */
package org.csuc.rest.api;

import javax.ws.rs.Priorities;

import org.csuc.rest.api.filters.AuthenticationFilter;
import org.csuc.rest.api.service.Authentication;
import org.csuc.rest.api.service.User;
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

	}
}
