package org.csuc.service.recollect;

import com.auth0.jwk.JwkException;
import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.entities.Recollect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Producer;
import org.csuc.typesafe.producer.ProducerConfig;
import org.csuc.typesafe.producer.RabbitMQConfig;
import org.csuc.utils.authorization.Authoritzation;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author amartinez
 */
@Path("/recollect")
public class Zip {


    private static Logger logger = LogManager.getLogger(Zip.class);

    private Client client = new Client("localhost", 27017, "echoes");

    private RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());

    private RabbitMQConfig config = new ProducerConfig(new File(getClass().getClassLoader().getResource("rabbitmq.defaults.conf").getFile()).toPath()).getRabbitMQConfig();

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
            Recollect recollect = recollectDAO.getById(id);

            HashMap<String, Object> message = new HashMap<>();

            message.put("_id", id);
            message.put("user", user);
            message.put("set", recollect.getSet());


            new Producer(config.getZipQueue(), config).sendMessage(message);

            return Response.status(Response.Status.ACCEPTED).entity(message).type(APPLICATION_JSON.toString()).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
