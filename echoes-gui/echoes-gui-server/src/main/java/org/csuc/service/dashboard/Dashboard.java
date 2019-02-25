package org.csuc.service.dashboard;

import com.auth0.jwk.JwkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.client.Client;
import org.csuc.dao.DashboardDAO;
import org.csuc.dao.impl.DashboardDAOImpl;
import org.csuc.service.analyse.Analyse;
import org.csuc.utils.authorization.Authoritzation;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.Objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author amartinez
 */
@Path("/dashboard")
public class Dashboard {

    private static Logger logger = LogManager.getLogger(Analyse.class);

    @Inject
    private Client client;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest servletRequest;


    @GET
    @Path("/user/{user}/status/{status}")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getStatus(@PathParam("user") String user,
                                      @PathParam("status") String status,
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

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            DashboardDAO dashboardDAO = new DashboardDAOImpl(client.getDatastore());

            return Response.status(Response.Status.ACCEPTED).entity(dashboardDAO.getStatus(parserStatus, user)).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/status/{status}/month")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getStatusMonth(@PathParam("user") String user,
                              @PathParam("status") String status,
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

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            DashboardDAO dashboardDAO = new DashboardDAOImpl(client.getDatastore());

            return Response.status(Response.Status.ACCEPTED).entity(dashboardDAO.getStatusMonth(parserStatus, user)).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/status/{status}/lastMonth")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getStatusLastMonth(@PathParam("user") String user,
                              @PathParam("status") String status,
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

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            DashboardDAO dashboardDAO = new DashboardDAOImpl(client.getDatastore());

            return Response.status(Response.Status.ACCEPTED).entity(dashboardDAO.getStatusLastMonth(parserStatus, user)).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/status/{status}/lastMonthIncrease")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getStatusLastMonthIncrease(@PathParam("user") String user,
                                       @PathParam("status") String status,
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

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            DashboardDAO dashboardDAO = new DashboardDAOImpl(client.getDatastore());

            return Response.status(Response.Status.ACCEPTED).entity(dashboardDAO.getStatusLastMonthIncrease(parserStatus, user)).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/status/{status}/lastYear")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getStatusLastYear(@PathParam("user") String user,
                                       @PathParam("status") String status,
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

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            DashboardDAO dashboardDAO = new DashboardDAOImpl(client.getDatastore());

            return Response.status(Response.Status.ACCEPTED).entity(dashboardDAO.getStatusLastYear(parserStatus, user)).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/status/{status}/lastDay")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getStatusLastDay(@PathParam("user") String user,
                                      @PathParam("status") String status,
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

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            DashboardDAO dashboardDAO = new DashboardDAOImpl(client.getDatastore());

            return Response.status(Response.Status.ACCEPTED).entity(dashboardDAO.getStatusLastDay(parserStatus, user)).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/recollect")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getDashboardRecollect(@PathParam("user") String user,
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
            DashboardDAO dashboardDAO = new DashboardDAOImpl(client.getDatastore());

            return Response.status(Response.Status.ACCEPTED).entity(dashboardDAO.getDashboardRecollect(user)).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/analyse")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getDashboardAnalyse(@PathParam("user") String user,
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
            DashboardDAO dashboardDAO = new DashboardDAOImpl(client.getDatastore());

            return Response.status(Response.Status.ACCEPTED).entity(dashboardDAO.getDashboardAnalyse(user)).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
