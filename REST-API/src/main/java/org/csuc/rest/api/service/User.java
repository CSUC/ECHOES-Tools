/**
 * 
 */
package org.csuc.rest.api.service;

import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.dao.UserDAO;
import org.Morphia.Core.dao.impl.UserDAOImpl;
import org.Morphia.Core.utils.Role;
import org.csuc.rest.api.typesafe.ApplicationConfig;
import org.csuc.rest.api.typesafe.TypesafeMongoDB;
import org.csuc.rest.api.utils.ResponseStatusCode;
import org.csuc.rest.api.utils.Secured;


/**
 * @author amartinez
 *
 */
@Path("/user")
public class User {

private TypesafeMongoDB mongodbConf = new ApplicationConfig().getMongodbConfig();
	
	private MorphiaEchoes echoes = 
			new MorphiaEchoes(mongodbConf.getHost(), mongodbConf.getPort(), mongodbConf.getDatabase());
	
	@Context
	SecurityContext securityContext;

	@Context
	private HttpServletRequest servletRequest;

	private UserDAO dao = new UserDAOImpl(org.Morphia.Core.entities.User.class, echoes.getDatastore());
	
	@GET
	@Secured({ Role.Admin })
	@Produces(MediaType.APPLICATION_JSON)
	public Response allUsers() {
		try {
			return Response.status(Response.Status.ACCEPTED).entity(dao.findAll()).build();
		}catch(Exception e) {
			return ResponseStatusCode.INTERNAL_SERVER_ERROR.build();
		}
		
	}

	@GET
	@Secured({ Role.Admin, Role.User })
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getUser(@PathParam("id") String id) {
		try {
			org.Morphia.Core.entities.User user = dao.findByUUID(id);

			if (Objects.isNull(user))	return ResponseStatusCode.NOT_FOUND.build();			
			else return Response.status(Response.Status.ACCEPTED).entity(user).build();
		}catch(Exception e) {
			return ResponseStatusCode.INTERNAL_SERVER_ERROR.build();
		}		
	}
	
	@POST
	@Secured({ Role.Admin })
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUser(org.Morphia.Core.entities.User user) {
		try {
			if(Objects.isNull(user.getId()))	return ResponseStatusCode.BAD_REQUEST.build();
			if(Objects.isNull(user.getPassword()))	return ResponseStatusCode.BAD_REQUEST.build();
			if(Objects.isNull(user.getDigest()))	return ResponseStatusCode.BAD_REQUEST.build();
			if(Objects.isNull(user.getRole()))	return ResponseStatusCode.BAD_REQUEST.build();		
			if(Objects.isNull(user.getUuid()))	user.setUuid(UUID.randomUUID().toString());
		
			if(Objects.isNull(dao.findById(user.getId())))	dao.insertNewUser(user);
			else return ResponseStatusCode.CONFLICT.build();
					
			return Response.status(Response.Status.CREATED).entity(user).build();
		}catch(Exception e) {
			return ResponseStatusCode.INTERNAL_SERVER_ERROR.build();
		}		
	}
	
	
	@PUT
	@Secured({ Role.Admin, Role.User})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editUser(org.Morphia.Core.entities.User user) {
		try {
			if(Objects.isNull(user.getId()))	return ResponseStatusCode.BAD_REQUEST.build();
			if(Objects.isNull(user.getUuid()))	return ResponseStatusCode.BAD_REQUEST.build();
			
			if(Objects.isNull(dao.findById(user.getId())))	return ResponseStatusCode.BAD_REQUEST.build();
			dao.getDatastore().save(user);
			
			return Response.status(Response.Status.ACCEPTED).entity(user).build();
		}catch(Exception e) {
			return ResponseStatusCode.INTERNAL_SERVER_ERROR.build();
		}		
	}
	
	
	@DELETE
	@Secured({ Role.Admin })
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUser(org.Morphia.Core.entities.User user) {
		try {
			if(Objects.isNull(user.getId()))	return ResponseStatusCode.BAD_REQUEST.build();
			
			org.Morphia.Core.entities.User userDAO = dao.findById(user.getId());
			
			if(Objects.isNull(userDAO))	return ResponseStatusCode.NOT_FOUND.build();
			
			return Response.status(Response.Status.ACCEPTED).entity(dao.delete(user)).build();
		}catch(Exception e) {
			return ResponseStatusCode.INTERNAL_SERVER_ERROR.build();
		}		
	}
	
}
