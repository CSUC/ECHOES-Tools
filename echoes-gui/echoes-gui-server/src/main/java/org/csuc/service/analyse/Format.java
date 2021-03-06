package org.csuc.service.analyse;

import com.auth0.jwk.JwkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.client.Client;
import org.csuc.dao.AnalyseDAO;
import org.csuc.dao.impl.AnalyseDAOImpl;
import org.csuc.entities.Analyse;
import org.csuc.utils.authorization.Authoritzation;
import org.csuc.utils.parser.ParserFormat;
import org.csuc.utils.response.ResponseEchoes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author amartinez
 */
@Path("/analyse")
public class Format {

    private static Logger logger = LogManager.getLogger(Format.class);

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpServletRequest servletRequest;

    @Inject
    private Client client;

    @GET
    @Path("/user/{user}/format/{format}")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getParserByFormat(@PathParam("user") String user,
                                      @PathParam("format") String format,
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

        if (Objects.isNull(format)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("format is mandatory")
                            .build()
            );
        }

        ParserFormat parserFormat = ParserFormat.convert(format);

        if (Objects.isNull(parserFormat)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("format status")
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
            AnalyseDAO analyseDAO = new AnalyseDAOImpl(Analyse.class, client.getDatastore());
            List<Analyse> queryResults = analyseDAO.getByFormat(parserFormat, user, page, pagesize);

            double count = new Long(analyseDAO.countByFormat(parserFormat, user)).doubleValue();

            ResponseEchoes response =
                    new ResponseEchoes(format, (int) count, (int) Math.ceil(count / new Long(pagesize).doubleValue()), queryResults.size(), queryResults);

            logger.debug(response);

            return Response.status(Response.Status.ACCEPTED).entity(response).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
