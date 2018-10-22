package org.csuc.service.recollect;

import com.auth0.jwk.JwkException;
import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.entities.Recollect;
import org.csuc.utils.Aggregation;
import org.csuc.utils.StreamUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.utils.authorization.Authoritzation;
import org.csuc.utils.response.ResponseEchoes;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author amartinez
 */
@Path("/recollect")
public class Status {

    private static Logger logger = LogManager.getLogger(org.csuc.service.recollect.Status.class);

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest servletRequest;


    @GET
    @Path("/user/{user}/status/{status}")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getRecollectByStatus(@PathParam("user") String user,
                                      @PathParam("status") String status,
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

        if (Objects.isNull(status)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("status is mandatory")
                            .build()
            );
        }

        Client client = new Client("localhost", 27017, "echoes");
        RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());

        org.csuc.utils.Status recollectStatus = org.csuc.utils.Status.convert(status);

        if (Objects.isNull(recollectStatus)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("invalid status")
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
            List<Recollect> queryResults = recollectDAO.getByStatus(recollectStatus, user, page, pagesize);

            double count = new Long(recollectDAO.countByStatus(recollectStatus, user)).doubleValue();

            ResponseEchoes response =
                    new ResponseEchoes(status, (int) count, (int) Math.ceil(count / new Long(pagesize).doubleValue()), queryResults.size(), queryResults);

            logger.debug(response);

            return Response.status(Response.Status.ACCEPTED).entity(response).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/status/aggregation")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getRecollectByStatusUserAggregation(@PathParam("user") String user,
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

        Client client = new Client("localhost", 27017, "echoes");
        RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());

        try {
            Supplier<Iterator<Aggregation>> i  = ()-> recollectDAO.getStatusAggregation(user);
            List<Aggregation> result = StreamUtils.asStream(i.get()).collect(toList());

            logger.debug(result);

            return Response.status(Response.Status.ACCEPTED).entity(result).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
