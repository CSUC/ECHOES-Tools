package org.csuc.service.parser;

import com.auth0.jwk.JwkException;
import com.mongodb.WriteResult;
import org.csuc.client.Client;
import org.csuc.dao.ParserDAO;
import org.csuc.dao.ParserErrorDAO;
import org.csuc.dao.impl.ParserDAOImpl;
import org.csuc.dao.impl.ParserErrorDAOImpl;
import org.csuc.entities.ParserError;
import org.csuc.utils.Status;
import org.csuc.utils.parser.ParserFormat;
import org.csuc.utils.parser.ParserMethod;
import org.csuc.utils.parser.ParserType;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Producer;
import org.csuc.typesafe.producer.ProducerConfig;
import org.csuc.typesafe.producer.RabbitMQConfig;
import org.csuc.typesafe.server.Application;
import org.csuc.typesafe.server.ServerConfig;
import org.csuc.utils.authorization.Authoritzation;
import org.csuc.utils.response.ResponseEchoes;
import org.mongodb.morphia.Key;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.nio.file.Files;
import java.util.*;


import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author amartinez
 */
@Path("/parser")
public class Parser {

    private static Logger logger = LogManager.getLogger(Parser.class);


    private RabbitMQConfig config = new ProducerConfig(new File(getClass().getClassLoader().getResource("rabbitmq.defaults.conf").getFile()).toPath()).getRabbitMQConfig();

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
            Client client = new Client("localhost", 27017, "echoes");
            ParserDAO parserDAO = new ParserDAOImpl(org.csuc.entities.Parser.class, client.getDatastore());

            org.csuc.entities.Parser parser = parserDAO.getById(id);

            if(!Objects.equals(user, parser.getUser())) return Response.status(Response.Status.UNAUTHORIZED).build();

            logger.debug(parser);

            return Response.status(Response.Status.ACCEPTED).entity(parser.toString()).type(APPLICATION_JSON.toString()).build();
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
            Client client = new Client("localhost", 27017, "echoes");
            ParserDAO parserDAO = new ParserDAOImpl(org.csuc.entities.Parser.class, client.getDatastore());

            List<org.csuc.entities.Parser> queryResults = parserDAO.getByUser(user, page, pagesize, "-timestamp");

            double count = new Long(parserDAO.countByUser(user)).doubleValue();

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
    public Response create(ParserRequest parserRequest, @HeaderParam("Authorization") String authorization) {

        if (Objects.isNull(parserRequest.getMethod()) || Objects.isNull(parserRequest.getType())
                || Objects.isNull(parserRequest.getFormat()) || Objects.isNull(parserRequest.getUser()) || Objects.isNull(parserRequest.getValue())) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("invalid form")
                            .build()
            );
        }

        Authoritzation authoritzation = new Authoritzation(parserRequest.getUser(), authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            Client client = new Client("localhost", 27017, "echoes");
            ParserDAO parserDAO = new ParserDAOImpl(org.csuc.entities.Parser.class, client.getDatastore());

            org.csuc.entities.Parser parser = new org.csuc.entities.Parser();

            parser.setFormat(ParserFormat.convert(parserRequest.getFormat()));
            parser.setMethod(ParserMethod.convert(parserRequest.getMethod()));
            parser.setType(ParserType.convert(parserRequest.getType()));
            parser.setValue(parserRequest.getValue());
            parser.setStatus(Status.QUEUE);
            parser.setUser(parserRequest.getUser());

            Key<org.csuc.entities.Parser> key = parserDAO.insert(parser);

            logger.debug(key);

            HashMap<String, Object> message = new HashMap<>();

            message.put("_id", parser.get_id());
            message.put("method", ParserMethod.convert(parserRequest.getMethod()));
            message.put("type", ParserType.convert(parserRequest.getType()));
            message.put("format", ParserFormat.convert(parserRequest.getFormat()));
            message.put("value", parser.getValue());

            new Producer(config.getParserQueue(), config).sendMessage(message);

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
            Client client = new Client("localhost", 27017, "echoes");
            ParserDAO parserDAO = new ParserDAOImpl(org.csuc.entities.Parser.class, client.getDatastore());

            WriteResult writeResult = parserDAO.deleteById(id);

            logger.debug(writeResult);

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
            Application serverConfig = new ServerConfig(null).getConfig();
            File file = new File(serverConfig.getParserFolder(id));
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
            Client client = new Client("localhost", 27017, "echoes");
            ParserErrorDAO parserErrorDAO = new ParserErrorDAOImpl(ParserError.class, client.getDatastore());

            ParserError parser = parserErrorDAO.getByReference(id);

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
