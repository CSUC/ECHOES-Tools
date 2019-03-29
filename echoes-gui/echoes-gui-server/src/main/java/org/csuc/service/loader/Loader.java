package org.csuc.service.loader;

import com.auth0.jwk.JwkException;
import com.mongodb.WriteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Producer;
import org.csuc.client.Client;
import org.csuc.dao.impl.loader.LoaderDetailsDAOImpl;
import org.csuc.dao.loader.LoaderDAO;
import org.csuc.dao.impl.loader.LoaderDAOImpl;
import org.csuc.dao.loader.LoaderDetailsDAO;
import org.csuc.entities.loader.LoaderDetails;
import org.csuc.typesafe.consumer.Queues;
import org.csuc.typesafe.server.Application;
import org.csuc.utils.Status;
import org.csuc.utils.authorization.Authoritzation;
import org.csuc.utils.response.ResponseEchoes;
import org.mongodb.morphia.Key;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author amartinez
 */
@Path("/loader")
public class Loader {

    private static Logger logger = LogManager.getLogger(Loader.class);

    @Inject
    private Client client;

    @Inject
    private Application applicationConfig;

    @Inject
    private Queues rabbitMQConfig;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest servletRequest;

    @GET
    @Path("/user/{user}/id/{id}")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getLoaderById(
            @PathParam("user") String user,
            @PathParam("id") String id,
            @HeaderParam("Authorization") String authorization) {

        if (Objects.isNull(id)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("id is mandatory")
                            .build()
            );
        }

        if (Objects.isNull(user)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("user is mandatory")
                            .build()
            );
        }

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            LoaderDAO loaderDAO = new LoaderDAOImpl(org.csuc.entities.loader.Loader.class, client.getDatastore());
            org.csuc.entities.loader.Loader loader = loaderDAO.getById(id);

            if(!Objects.equals(user, loader.getUser())) return Response.status(Response.Status.UNAUTHORIZED).build();

            logger.debug(loader);

            return Response.status(Response.Status.ACCEPTED).entity(loader.toString()).type(APPLICATION_JSON.toString()).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getLoaderByUser(@PathParam("user") String user,
                                    @DefaultValue("50") @QueryParam("pagesize") int pagesize,
                                    @DefaultValue("0") @QueryParam("page") int page,
                                    @HeaderParam("Authorization") String authorization) {
        if (Objects.isNull(user)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("user is mandatory")
                            .build()
            );
        }

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            LoaderDAO loaderDAO = new LoaderDAOImpl(org.csuc.entities.loader.Loader.class, client.getDatastore());
            List<org.csuc.entities.loader.Loader> queryResults = loaderDAO.getByUser(user, page, pagesize, "-timestamp");

            double count = new Long(loaderDAO.countByUser(user)).doubleValue();

            ResponseEchoes response =
                    new ResponseEchoes(user, (int) count, (int) Math.ceil(count / new Long(pagesize).doubleValue()), queryResults.size(), queryResults);

            logger.debug(response);

            return Response.status(Response.Status.ACCEPTED).entity(response).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    @GET
    @Path("/user/{user}/id/{id}/error/{page}")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getQualityErrorById(
            @PathParam("user") String user,
            @PathParam("id") String id,
            @PathParam("page") int page,
            @DefaultValue("50") @QueryParam("pagesize") int pagesize,
            @HeaderParam("Authorization") String authorization) {

        if (Objects.isNull(id)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("id is mandatory")
                            .build()
            );
        }

        if (Objects.isNull(user)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("user is mandatory")
                            .build()
            );
        }

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            LoaderDetailsDAO loaderDetailsDAO = new LoaderDetailsDAOImpl(LoaderDetails.class, client.getDatastore());

            List<LoaderDetails> queryResults = loaderDetailsDAO.getErrorsById(id, page, pagesize, null);

            double count = new Long(loaderDetailsDAO.countErrorsById(id)).doubleValue();

            ResponseEchoes response =
                    new ResponseEchoes(user, (int) count, (int) Math.ceil(count / new Long(pagesize).doubleValue()), queryResults.size(), queryResults);

            logger.info(response);

            return Response.status(Response.Status.ACCEPTED).entity(response).type(APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/create")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(LoaderRequest loaderRequest) {

        if (Objects.isNull(loaderRequest.getEndpoint()) || Objects.isNull(loaderRequest.getContentType())
               || Objects.isNull(loaderRequest.getUser()) || Objects.isNull(loaderRequest.getUuid())) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("invalid form")
                            .build()
            );
        }


        try {
            LoaderDAO loaderDAO = new LoaderDAOImpl(org.csuc.entities.loader.Loader.class, client.getDatastore());
            org.csuc.entities.loader.Loader loader = new org.csuc.entities.loader.Loader();

            loader.setEndpoint(loaderRequest.getEndpoint());
            loader.setContentType(loaderRequest.getContentType());
            loader.setContextUri(loaderRequest.getContextUri());
            loader.setUser(loaderRequest.getUser());
            loader.setUuid(loaderRequest.getUuid());
            loader.setStatus(Status.QUEUE);

            Key<org.csuc.entities.loader.Loader> key = loaderDAO.insert(loader);

            logger.info(key);

            HashMap<String, Object> message = new HashMap<>();

            message.put("_id", loader.get_id());
            message.put("endpoint", loader.getEndpoint());
            message.put("contentType", loader.getContentType());
            message.put("contextUri", loader.getContextUri());
            message.put("uuid", loader.getUuid());

            new Producer(rabbitMQConfig.getLoader()).sendMessage(message);

            return Response.status(Response.Status.ACCEPTED).entity(key).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/user/{user}/id/{id}/delete")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response detelete(
            @PathParam("user") String user,
            @PathParam("id") String id,
            @HeaderParam("Authorization") String authorization) {

        if (Objects.isNull(user)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("user is mandatory")
                            .build()
            );
        }

        if (Objects.isNull(id)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("id is mandatory")
                            .build()
            );
        }

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            LoaderDAO loaderDAO = new LoaderDAOImpl(org.csuc.entities.loader.Loader.class, client.getDatastore());
            WriteResult writeResult = loaderDAO.deleteById(id);

            logger.debug(writeResult);

            return Response.status(Response.Status.ACCEPTED).entity(writeResult).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/count")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getLoaderCountByUser(@PathParam("user") String user,
                                    @HeaderParam("Authorization") String authorization) {
        if (Objects.isNull(user)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("user is mandatory")
                            .build()
            );
        }

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            LoaderDAO loaderDAO = new LoaderDAOImpl(org.csuc.entities.loader.Loader.class, client.getDatastore());
            Long result = loaderDAO.countByUser(user);

            logger.info(result);

            return Response.status(Response.Status.ACCEPTED).entity(result).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
