/**
 * 
 */
package org.csuc.rest.api.service;

import java.util.List;
import java.util.Objects;

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
import org.Morphia.Core.dao.HarvestedCollectionConfigDAO;
import org.Morphia.Core.dao.impl.HarvestedCollectionConfigDAOImpl;
import org.Morphia.Core.entities.HarvestedCollectionConfig;
import org.Morphia.Core.utils.Role;
import org.csuc.rest.api.typesafe.ApplicationConfig;
import org.csuc.rest.api.typesafe.TypesafeMongoDB;
import org.csuc.rest.api.utils.ResponseStatusCode;
import org.csuc.rest.api.utils.Secured;

/**
 * @author amartinez
 *
 */
@Path("/harvested")
public class Harvested {

	private TypesafeMongoDB mongodbConf = new ApplicationConfig().getMongodbConfig();
	
	private MorphiaEchoes echoes = 
			new MorphiaEchoes(mongodbConf.getHost(), mongodbConf.getPort(), mongodbConf.getDatabase());
	
	@Context
	SecurityContext securityContext;

	@Context
	private HttpServletRequest servletRequest;

	private HarvestedCollectionConfigDAO dao = 
			new HarvestedCollectionConfigDAOImpl(HarvestedCollectionConfig.class, echoes.getDatastore());
	
	
	@GET
	@Secured({ Role.Admin })
	@Produces(MediaType.APPLICATION_JSON)
	public Response allHarvested() {
		try{
			List<HarvestedCollectionConfig> h = dao.findAll();			
			if(h.isEmpty())	return ResponseStatusCode.NO_CONTENT.build();			
			return ResponseStatusCode.OK.responseBuilder().entity(h).build();
		}catch(Exception e){			
			return ResponseStatusCode.INTERNAL_SERVER_ERROR.build();
		}
	}
	
	@GET
	@Secured({ Role.Admin, Role.User })
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getHarvested(@PathParam("id") String id) {
		try {			
			HarvestedCollectionConfig h = dao.findById(id);

			if (Objects.isNull(h))	return ResponseStatusCode.NOT_FOUND.build();			
			else return Response.status(Response.Status.ACCEPTED).entity(h).build();
		}catch(Exception e) {
			return ResponseStatusCode.INTERNAL_SERVER_ERROR.build();
		}		
	}
	
	@POST
	@Secured({ Role.Admin })
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addHarvested(HarvestedCollectionConfig hConfig) {		
		try {
			if(Objects.isNull(hConfig.getId()))	return ResponseStatusCode.BAD_REQUEST.build();
			if(Objects.isNull(hConfig.getOaisetid()))	return ResponseStatusCode.BAD_REQUEST.build();
			if(Objects.isNull(hConfig.getOaisource()))	return ResponseStatusCode.BAD_REQUEST.build();
			if(Objects.isNull(hConfig.getMetadataconfig()))	return ResponseStatusCode.BAD_REQUEST.build();		
			if(Objects.isNull(hConfig.getUser_id()))	return ResponseStatusCode.BAD_REQUEST.build();
		
			if(Objects.isNull(dao.findById(hConfig.getId())))	dao.insertNewHarvested(hConfig);
			else return ResponseStatusCode.CONFLICT.build();
					
			return Response.status(Response.Status.CREATED).entity(hConfig).build();
		}catch(Exception e) {
			return ResponseStatusCode.INTERNAL_SERVER_ERROR.build();
		}		
	}
	
	@PUT
	@Secured({ Role.Admin })
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateHarvested(HarvestedCollectionConfig hConfig) {
		try {
			if(Objects.isNull(hConfig.getId()))	return ResponseStatusCode.BAD_REQUEST.build();
			if(Objects.isNull(hConfig.getOaisetid()))	return ResponseStatusCode.BAD_REQUEST.build();
			if(Objects.isNull(hConfig.getOaisource()))	return ResponseStatusCode.BAD_REQUEST.build();
			if(Objects.isNull(hConfig.getMetadataconfig()))	return ResponseStatusCode.BAD_REQUEST.build();		
			if(Objects.isNull(hConfig.getUser_id()))	return ResponseStatusCode.BAD_REQUEST.build();
		
			if(Objects.isNull(dao.findById(hConfig.getId())))	dao.insertNewHarvested(hConfig);
			else return ResponseStatusCode.NOT_FOUND.build();
					
			return Response.status(Response.Status.CREATED).entity(hConfig).build();
		}catch(Exception e) {
			return ResponseStatusCode.INTERNAL_SERVER_ERROR.build();
		}
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteHarvested(HarvestedCollectionConfig hConfig) {
		try {
			if(Objects.isNull(hConfig.getId()))	return ResponseStatusCode.BAD_REQUEST.build();
			
			if(Objects.isNull(dao.findById(hConfig.getId())))	dao.delete(hConfig);
			else return ResponseStatusCode.NOT_FOUND.build();
					
			return Response.status(Response.Status.CREATED).entity(hConfig).build();
		}catch(Exception e) {
			return ResponseStatusCode.INTERNAL_SERVER_ERROR.build();
		}
	}
}
