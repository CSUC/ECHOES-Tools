package org.csuc.rest.api.filters;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.Morphia.Core.entities.User;
import org.Morphia.Core.utils.Role;
import org.csuc.rest.api.context.BasicSecurityContext;
import org.csuc.rest.api.utils.Auth;
import org.csuc.rest.api.utils.ResponseStatusCode;
import org.csuc.rest.api.utils.Secured;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	
	@Context
	private ResourceInfo resourceInfo;

	private static final String AUTHENTICATION_SCHEME = "Bearer";
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Get the resource class which matches with the requested URL
		// Extract the roles declared by it
		Class<?> resourceClass = resourceInfo.getResourceClass();
		List<Role> classRoles = extractRoles(resourceClass);

		// Get the resource method which matches with the requested URL
		// Extract the roles declared by it
		Method resourceMethod = resourceInfo.getResourceMethod();
		List<Role> methodRoles = extractRoles(resourceMethod);

		// Get the Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		// Validate the Authorization header
		if (!isTokenBasedAuthentication(authorizationHeader)) {
			abortWithUnauthorized(requestContext);
			return;
		}

		// Extract the token from the Authorization header
		String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
		try {
			Auth auth = new Auth(token);
			User user;
			
			// The method annotations override the class annotations and Validate token
			if (methodRoles.isEmpty())	user = auth.validateToken(classRoles);
			else	user = auth.validateToken(methodRoles);

			requestContext.setSecurityContext(new BasicSecurityContext(user, true));
		} catch (Exception e) {
			abortWithUnauthorized(requestContext);
		}
	}

	/**
	 * Check if the Authorization header is valid
	 * 
	 * It must not be null and must be prefixed with "Bearer" plus a whitespace
	 * 
	 * Authentication scheme comparison must be case-insensitive
	 * 
	 * @param authorizationHeader
	 * @return
	 */
	private boolean isTokenBasedAuthentication(String authorizationHeader) {		
		return authorizationHeader != null
				&& authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
	}

	/**
	 * Abort the filter chain with a 401 status code
	 * 
	 * The "WWW-Authenticate" header is sent along with the response
	 * 
	 * @param requestContext
	 */
	private void abortWithUnauthorized(ContainerRequestContext requestContext) { 
		requestContext.abortWith(ResponseStatusCode.UNAUTHORIZED.build());
	}

	/**
	 * Extract the roles from the annotated element
	 * 
	 * @param annotatedElement
	 * @return
	 */
	private List<Role> extractRoles(AnnotatedElement annotatedElement) {
		if (annotatedElement == null) {
			return new ArrayList<Role>();
		} else {
			Secured secured = annotatedElement.getAnnotation(Secured.class);
			if (secured == null) {
				return new ArrayList<Role>();
			} else {
				Role[] allowedRoles = secured.value();
				return Arrays.asList(allowedRoles);
			}
		}
	}
	
}
