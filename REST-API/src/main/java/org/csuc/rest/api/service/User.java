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
import org.csuc.rest.api.utils.BasicResponse;
import org.csuc.rest.api.utils.HTTPStatusCode;
import org.csuc.rest.api.utils.ResponseError;
import org.csuc.rest.api.utils.Secured;

import com.mongodb.WriteResult;

/**
 * @author amartinez
 *
 */
@Path("/user")
public class User {

	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");

	
	
	private Response BAD_REQUEST = Response.status(Response.Status.BAD_REQUEST)
			.entity(new ResponseError(HTTPStatusCode.BAD_REQUEST))
			.build();
	
	@Context
	SecurityContext securityContext;

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Secured({ Role.Admin })
	@Produces(MediaType.APPLICATION_JSON)
	public Response allUsers(@PathParam("id") String id) {
		UserDAO dao = new UserDAOImpl(org.Morphia.Core.entities.User.class, echoes.getDatastore());
		return Response.status(Response.Status.ACCEPTED).entity(dao.findAll()).build();
	}

	@GET
	@Secured({ Role.Admin, Role.User })
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getUser(@PathParam("id") String id) {
		UserDAO dao = new UserDAOImpl(org.Morphia.Core.entities.User.class, echoes.getDatastore());
		org.Morphia.Core.entities.User user = dao.findByUUID(id);

		if (Objects.isNull(user))
			return Response.status(Response.Status.BAD_GATEWAY)
					.entity(new BasicResponse(String.format("%s User not exist.", id)))
					.build();			
		else return Response.status(Response.Status.ACCEPTED).entity(user).build();
			
	}
	
	@POST
	@Secured({ Role.Admin })
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUser(org.Morphia.Core.entities.User user) {
		if(Objects.isNull(user.getId()))	return BAD_REQUEST;
		if(Objects.isNull(user.getPassword()))	return BAD_REQUEST;
		if(Objects.isNull(user.getDigest()))	return BAD_REQUEST;
		if(Objects.isNull(user.getRole()))	return BAD_REQUEST;		
		if(Objects.isNull(user.getUuid()))	user.setUuid(UUID.randomUUID().toString());
		
		UserDAO dao = new UserDAOImpl(org.Morphia.Core.entities.User.class, echoes.getDatastore());		
	
		if(Objects.isNull(dao.findById(user.getId())))	dao.insert(user);
		
		else return Response.status(Response.Status.BAD_GATEWAY)
				.entity(new BasicResponse(String.format("%s exist user. Select another email.", user.getId())))
				.build();
			
		return Response.status(Response.Status.ACCEPTED).entity(user).build();
	}
	
	
	@PUT
	@Secured({ Role.Admin, Role.User})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editUser(org.Morphia.Core.entities.User user) {
		if(Objects.isNull(user.getId()))	return BAD_REQUEST;
		if(Objects.isNull(user.getUuid()))	return BAD_REQUEST;
		
		UserDAO dao = new UserDAOImpl(org.Morphia.Core.entities.User.class, echoes.getDatastore());
		
		if(Objects.isNull(dao.findById(user.getId())))	return BAD_REQUEST;
		
		dao.getDatastore().save(user);
		
		return Response.status(Response.Status.ACCEPTED).entity(user).build();
	}
	
	
	@DELETE
	@Secured({ Role.Admin })
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUser(org.Morphia.Core.entities.User user) {
		WriteResult result;
		if(Objects.isNull(user.getId()))	return BAD_REQUEST;
		
		UserDAO dao = new UserDAOImpl(org.Morphia.Core.entities.User.class, echoes.getDatastore());		
		org.Morphia.Core.entities.User userDAO = dao.findById(user.getId());
		
		if(Objects.isNull(userDAO))
			return Response.status(Response.Status.ACCEPTED)
					.entity(new BasicResponse("User not exist"))
					.build();
		else result= echoes.getDatastore().delete(user); 
		
		return Response.status(Response.Status.ACCEPTED).entity(result).build();
	}
	

}
