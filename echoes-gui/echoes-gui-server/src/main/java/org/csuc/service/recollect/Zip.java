package org.csuc.service.recollect;

import com.auth0.jwk.JwkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Producer;
import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.entities.Recollect;
import org.csuc.typesafe.consumer.Queues;
import org.csuc.typesafe.server.Application;
import org.csuc.utils.authorization.Authoritzation;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author amartinez
 */
@Path("/recollect")
public class Zip {


    private static Logger logger = LogManager.getLogger(Zip.class);


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

    @POST
    @Path("/user/{user}/id/{id}/zip")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getRecollectById(
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
            RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());
            Recollect recollect = recollectDAO.getById(id);

            HashMap<String, Object> message = new HashMap<>();

            message.put("_id", id);
            message.put("user", user);

            new Producer(rabbitMQConfig.getZip()).sendMessage(message);

            return Response.status(Response.Status.ACCEPTED).entity(message).type(APPLICATION_JSON.toString()).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
