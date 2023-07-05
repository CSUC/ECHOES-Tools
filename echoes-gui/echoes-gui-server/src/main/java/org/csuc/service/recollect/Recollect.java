package org.csuc.service.recollect;

import com.auth0.jwk.JwkException;
import com.mongodb.WriteResult;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Producer;
import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.typesafe.consumer.Queues;
import org.csuc.typesafe.server.Application;
import org.csuc.utils.Status;
import org.csuc.utils.authorization.Authoritzation;
import org.csuc.utils.recollect.TransformationType;
import org.csuc.utils.response.ResponseEchoes;
import org.mongodb.morphia.Key;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author amartinez
 */
@Path("/recollect")
public class Recollect {

    private static Logger logger = LogManager.getLogger(Recollect.class);

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

            org.csuc.entities.Recollect recollect = recollectDAO.getById(id);

            logger.info(recollect);

            if(!Objects.equals(user, recollect.getUser())) return Response.status(Response.Status.UNAUTHORIZED).build();


            return Response.status(Response.Status.ACCEPTED).entity(recollect.toString()).type(APPLICATION_JSON.toString()).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getRecollectByUser(@PathParam("user") String user,
                                    @DefaultValue("50") @QueryParam("pagesize") int pagesize,
                                    @DefaultValue("0") @QueryParam("page") int page,
                                    @HeaderParam("Authorization") String authorization) {
        if (Objects.isNull(user)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("user is mandatory")
                            .entity("user is mandatory")
                            .build()
            );
        }

        if(Objects.isNull(authorization))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        Authoritzation authoritzation = new Authoritzation(user, authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());
            List<org.csuc.entities.Recollect> queryResults = recollectDAO.getByUser(user, page, pagesize, "-timestamp");

            double count = new Long(recollectDAO.countByUser(user)).doubleValue();

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
    public Response create(RecollectRequest recollectRequest, @HeaderParam("Authorization") String authorization) {

        if (Objects.isNull(recollectRequest.getInput()) || Objects.isNull(recollectRequest.getUser()) ) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("invalid form")
                            .build()
            );
        }

        if(Objects.isNull(authorization))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        Authoritzation authoritzation = new Authoritzation(recollectRequest.getUser(), authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());
            org.csuc.entities.Recollect recollect = new org.csuc.entities.Recollect();

            logger.info(recollectRequest.toString());

            recollect.setInput(recollectRequest.getInput());
            recollect.setType(TransformationType.convert(recollectRequest.getType()));
            if(Objects.nonNull(recollectRequest.getProperties())) recollect.setProperties(recollectRequest.getProperties());
            recollect.setUser(recollectRequest.getUser());
            recollect.setFormat(recollectRequest.getFormat());
            //recollect.setSchema(recollectRequest.getSchema());
            recollect.setFilename(recollectRequest.getFilename());

            recollect.setStatus(Status.QUEUE);

            Key<org.csuc.entities.Recollect> key = recollectDAO.insert(recollect);

            logger.debug(key);

            HashMap<String, Object> message = new HashMap<>();

            message.put("_id", recollect.get_id());
            message.put("input", recollect.getInput());
            message.put("format", recollect.getFormat());
            //message.put("schema", recollect.getSchema());
            message.put("properties", recollect.getProperties());


            new Producer(rabbitMQConfig.getRecollect()).sendMessage(message);

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
            RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, client.getDatastore());
            WriteResult writeResult = recollectDAO.deleteById(id);

            logger.debug(writeResult);

            Files.walk(Paths.get(applicationConfig.getRecollectFolder(id)))
                    .sorted(Comparator.reverseOrder())
                    .map(java.nio.file.Path::toFile)
                    .peek(logger::debug)
                    .forEach(File::delete);

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
            File file = new File(applicationConfig.getRecollectFolder(id));
            java.nio.file.Path result =
                    Files.list(file.toPath())
                            .filter(f -> f.toString().endsWith(".zip"))
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
}
