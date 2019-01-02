package org.csuc.service.loader;

import com.auth0.jwk.JwkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.client.Client;
import org.csuc.dao.LoaderDAO;
import org.csuc.dao.impl.AnalyseDAOImpl;
import org.csuc.dao.impl.LoaderDAOImpl;
import org.csuc.entities.Analyse;
import org.csuc.entities.Loader;
import org.csuc.utils.Aggregation;
import org.csuc.utils.StreamUtils;
import org.csuc.utils.authorization.Authoritzation;
import org.csuc.utils.response.ResponseEchoes;

import javax.inject.Inject;
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
@Path("/loader")
public class Status {

    private static Logger logger = LogManager.getLogger(Status.class);

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest servletRequest;

    @Inject
    private Client client;

    @GET
    @Path("/user/{user}/status/{status}")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getLoaderByStatus(@PathParam("user") String user,
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
        org.csuc.utils.Status parserStatus = org.csuc.utils.Status.convert(status);

        if (Objects.isNull(parserStatus)) {
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
            LoaderDAO loaderDAO = new LoaderDAOImpl(Loader.class, client.getDatastore());
            List<Loader> queryResults = loaderDAO.getByStatus(parserStatus, user, page, pagesize);

            double count = new Long(loaderDAO.countByStatus(parserStatus, user)).doubleValue();

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
    public Response getLoaderByStatusUserAggregation(@PathParam("user") String user,
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
            LoaderDAO loaderDAO = new LoaderDAOImpl(Loader.class, client.getDatastore());
            Supplier<Iterator<Aggregation>> i  = ()-> loaderDAO.getStatusAggregation(user);
            List<Aggregation> result = StreamUtils.asStream(i.get()).collect(toList());

            logger.debug(result);

            return Response.status(Response.Status.ACCEPTED).entity(result).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
