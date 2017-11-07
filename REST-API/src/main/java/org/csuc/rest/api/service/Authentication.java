package org.csuc.rest.api.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.Morphia.Core.utils.Role;
import org.csuc.rest.api.utils.Auth;
import org.csuc.rest.api.utils.Credentials;
import org.csuc.rest.api.utils.ResponseStatusCode;
import org.csuc.rest.api.utils.Secured;
import org.csuc.rest.api.utils.jwt.Token;

@Path("/authentication")
public class Authentication {
	
	@Context
	SecurityContext securityContext;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Secured({ Role.Admin, Role.User })
	@Produces(MediaType.APPLICATION_JSON)
	public Response isAuthenticate() {
		return Response.ok(securityContext.getUserPrincipal().getName()).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateUser(Credentials credentials) {		
		try {
			String username = credentials.getUsername();
			String password = credentials.getPassword();

			// Authenticate the user using the credentials provided
			Auth auth = new Auth(username, password);
			auth.authenticate();

			// Issue a token for the user
			Token token = auth.issueToken();
			
			// Return the token on the response
			return Response.ok(token).build();
		} catch (Exception e) {
			return ResponseStatusCode.UNAUTHORIZED.build();
		}
	}
}
