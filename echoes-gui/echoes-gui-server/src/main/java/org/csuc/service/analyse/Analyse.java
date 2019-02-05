package org.csuc.service.analyse;

import com.auth0.jwk.JwkException;
import com.mongodb.WriteResult;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Producer;
import org.csuc.client.Client;
import org.csuc.dao.AnalyseDAO;
import org.csuc.dao.AnalyseErrorDAO;
import org.csuc.dao.impl.AnalyseDAOImpl;
import org.csuc.dao.impl.AnalyseErrorDAOImpl;
import org.csuc.entities.AnalyseError;
import org.csuc.typesafe.consumer.Queues;
import org.csuc.typesafe.server.Application;
import org.csuc.utils.Status;
import org.csuc.utils.authorization.Authoritzation;
import org.csuc.utils.parser.ParserFormat;
import org.csuc.utils.parser.ParserMethod;
import org.csuc.utils.parser.ParserType;
import org.csuc.utils.response.ResponseEchoes;
import org.mongodb.morphia.Key;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author amartinez
 */
@Path("/analyse")
public class Analyse {

    private static Logger logger = LogManager.getLogger(Analyse.class);

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
    public Response getParserById(
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
            AnalyseDAO analyseDAO = new AnalyseDAOImpl(org.csuc.entities.Analyse.class, client.getDatastore());
            org.csuc.entities.Analyse analyse = analyseDAO.getById(id);

            if(!Objects.equals(user, analyse.getUser())) return Response.status(Response.Status.UNAUTHORIZED).build();

            logger.debug(analyse);

            return Response.status(Response.Status.ACCEPTED).entity(analyse.toString()).type(APPLICATION_JSON.toString()).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getParserByUser(@PathParam("user") String user,
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
            AnalyseDAO analyseDAO = new AnalyseDAOImpl(org.csuc.entities.Analyse.class, client.getDatastore());
            List<org.csuc.entities.Analyse> queryResults = analyseDAO.getByUser(user, page, pagesize, "-timestamp");

            double count = new Long(analyseDAO.countByUser(user)).doubleValue();

            ResponseEchoes response =
                    new ResponseEchoes(user, (int) count, (int) Math.ceil(count / new Long(pagesize).doubleValue()), queryResults.size(), queryResults);

            logger.debug(response);

            return Response.status(Response.Status.ACCEPTED).entity(response).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/create")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(AnalyseRequest analyseRequest, @HeaderParam("Authorization") String authorization) {

        if (Objects.isNull(analyseRequest.getMethod()) || Objects.isNull(analyseRequest.getType())
                || Objects.isNull(analyseRequest.getFormat()) || Objects.isNull(analyseRequest.getUser()) || Objects.isNull(analyseRequest.getValue())) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("invalid form")
                            .build()
            );
        }

        Authoritzation authoritzation = new Authoritzation(analyseRequest.getUser(), authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            AnalyseDAO analyseDAO = new AnalyseDAOImpl(org.csuc.entities.Analyse.class, client.getDatastore());
            org.csuc.entities.Analyse analyse = new org.csuc.entities.Analyse();

            analyse.setFormat(ParserFormat.convert(analyseRequest.getFormat()));
            analyse.setMethod(ParserMethod.convert(analyseRequest.getMethod()));
            analyse.setType(ParserType.convert(analyseRequest.getType()));
            analyse.setValue(analyseRequest.getValue());
            analyse.setStatus(Status.QUEUE);
            analyse.setUser(analyseRequest.getUser());

            Key<org.csuc.entities.Analyse> key = analyseDAO.insert(analyse);

            logger.debug(key);

            HashMap<String, Object> message = new HashMap<>();

            message.put("_id", analyse.get_id());
            message.put("method", ParserMethod.convert(analyseRequest.getMethod()));
            message.put("type", ParserType.convert(analyseRequest.getType()));
            message.put("format", ParserFormat.convert(analyseRequest.getFormat()));
            message.put("value", analyse.getValue());

            new Producer(rabbitMQConfig.getAnalyse()).sendMessage(message);

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
            AnalyseDAO analyseDAO = new AnalyseDAOImpl(org.csuc.entities.Analyse.class, client.getDatastore());
            WriteResult writeResult = analyseDAO.deleteById(id);

            logger.debug(writeResult);

            Files.deleteIfExists(Paths.get(applicationConfig.getParserFolder(id)));

            return Response.status(Response.Status.ACCEPTED).entity(writeResult).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/id/{id}/download")
    @Produces({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA})
    public Response download(
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

        try {
            File file = new File(applicationConfig.getParserFolder(id));
            java.nio.file.Path result =
                    Files.list(file.toPath())
                            .findFirst().get();

            String extension = FilenameUtils.getExtension(result.getFileName().toString());

            StreamingOutput fileStream = outputStream -> {
                try{
                    byte[] data = Files.readAllBytes(result);
                    outputStream.write(data);
                    outputStream.flush();
                }catch (Exception e){
                    throw new WebApplicationException("File Not Found !!");
                }
            };
            return Response
                    .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition","attachment; filename = " + id + "." + extension)
                    .build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    @GET
    @Path("/user/{user}/id/{id}/error")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getParserByIdError(
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
            AnalyseErrorDAO analyseErrorDAO = new AnalyseErrorDAOImpl(AnalyseError.class, client.getDatastore());

            AnalyseError parser = analyseErrorDAO.getByReference(id);

            if(Objects.isNull(parser))
                return Response.status(Response.Status.BAD_REQUEST).build();

            logger.debug(parser);

            return Response.status(Response.Status.ACCEPTED).entity(parser.toString()).type(APPLICATION_JSON.toString()).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
