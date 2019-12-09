package org.csuc.service.quality;

import com.auth0.jwk.JwkException;
import com.mongodb.WriteResult;
import com.typesafe.config.ConfigRenderOptions;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Producer;
import org.csuc.client.Client;
import org.csuc.dao.QualityDAO;
import org.csuc.dao.QualityDetailsDAO;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.dao.entity.Status;
import org.csuc.dao.impl.QualityDAOImpl;
import org.csuc.dao.impl.QualityDetailsDAOImpl;
import org.csuc.poi.Report;
import org.csuc.typesafe.QualityConfig;
import org.csuc.typesafe.consumer.Queues;
import org.csuc.typesafe.server.Application;
import org.csuc.utils.StreamUtils;
import org.csuc.utils.authorization.Authoritzation;
import org.csuc.utils.response.ResponseEchoes;
import org.mongodb.morphia.Key;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author amartinez
 */
@Path("/quality")
public class Quality {

    private static Logger logger = LogManager.getLogger(Quality.class);

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
    public Response getQualityById(
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
            QualityDAO qualityDAO = new QualityDAOImpl(org.csuc.dao.entity.Quality.class, client.getDatastore());
            org.csuc.dao.entity.Quality quality = qualityDAO.getById(id);

            if(!Objects.equals(user, quality.getUser())) return Response.status(Response.Status.UNAUTHORIZED).build();

            logger.debug(quality);

            return Response.status(Response.Status.ACCEPTED).entity(quality.toString()).type(APPLICATION_JSON.toString()).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }



    @GET
    @Path("/user/{user}")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getQualityByUser(@PathParam("user") String user,
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
            logger.info(user);

            QualityDAO qualityDAO = new QualityDAOImpl(org.csuc.dao.entity.Quality.class, client.getDatastore());
            List<org.csuc.dao.entity.Quality> queryResults = qualityDAO.getByUser(user, page, pagesize, "-timestamp");

            double count = new Long(qualityDAO.countByUser(user)).doubleValue();

            ResponseEchoes response =
                    new ResponseEchoes(user, (int) count, (int) Math.ceil(count / new Long(pagesize).doubleValue()), queryResults.size(), queryResults);

            logger.info(response);

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
            QualityDetailsDAO qualityDetailsDAO = new QualityDetailsDAOImpl(QualityDetails.class, client.getDatastore());

            List<QualityDetails> queryResults = qualityDetailsDAO.getErrorsById(id, page, pagesize, null);

            double count = new Long(qualityDetailsDAO.countErrorsById(id)).doubleValue();

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
    public Response create(QualityRequest qualityRequest, @HeaderParam("Authorization") String authorization) {

        if (Objects.isNull(qualityRequest.getDataset()) || Objects.isNull(qualityRequest.getFormat())
                || Objects.isNull(qualityRequest.getUser())) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("invalid form")
                            .build()
            );
        }

        Authoritzation authoritzation = new Authoritzation(qualityRequest.getUser(), authorization.split("\\s")[1]);
        try {
            authoritzation.execute();
        } catch (JwkException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            QualityDAO qualityDAO = new QualityDAOImpl(org.csuc.dao.entity.Quality.class, client.getDatastore());
            org.csuc.dao.entity.Quality quality = new org.csuc.dao.entity.Quality();

            quality.setContentType(qualityRequest.getFormat());
            quality.setStatus(Status.QUEUE);
            quality.setUser(qualityRequest.getUser());
            quality.setData(qualityRequest.getDataset());

            Key<org.csuc.dao.entity.Quality> key = qualityDAO.insert(quality);

            logger.debug(key);

            HashMap<String, Object> message = new HashMap<>();

            message.put("_id", quality.get_id());
            message.put("content-type", quality.getContentType());
            message.put("user", quality.getUser());
            message.put("dataset", quality.getData());

            new Producer(rabbitMQConfig.getQuality()).sendMessage(message);

            return Response.status(Response.Status.ACCEPTED).entity(key).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/user/{user}/id/{id}/create-report")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createReport(
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
            Report report = new Report(client.getDatastore());

            report.create(id);

            return Response.status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/id/{id}/download-report")
    @Produces({"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", MediaType.APPLICATION_OCTET_STREAM})
    public Response downloadReport(
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

        logger.info("[download-report] - {}", id);

        try {
            if(Files.notExists(Paths.get(String.format("/tmp/%s.xlsx", id)))){
                new Report(client.getDatastore()).create(id);
            }
            return Response.status(200)
                    .entity(Paths.get(String.format("/tmp/%s.xlsx", id)).toFile())
                    .header("Content-Disposition", "attachment; filename=" + id + ".xlsx")
                    .build();
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
            QualityDAO qualityDAO = new QualityDAOImpl(org.csuc.dao.entity.Quality.class, client.getDatastore());

            WriteResult writeResult = qualityDAO.deleteById(id);

            logger.debug(writeResult);

            return Response.status(Response.Status.ACCEPTED).entity(writeResult).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/status/aggregation")
    @Produces(APPLICATION_JSON + "; charset=utf-8")
    public Response getQualityByStatusUserAggregation(@PathParam("user") String user,
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
            QualityDAO qualityDAO = new QualityDAOImpl(org.csuc.dao.entity.Quality.class, client.getDatastore());

            Supplier<Iterator<org.csuc.dao.entity.Aggregation>> i  = ()-> qualityDAO.getStatusAggregation(user);
            List<org.csuc.dao.entity.Aggregation> result = StreamUtils.asStream(i.get()).collect(toList());

            logger.debug(result);

            return Response.status(Response.Status.ACCEPTED).entity(result).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/user/{user}/id/{id}/{filename}/download")
    @Produces({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA})
    public Response download(
            @PathParam("user") String user,
            @PathParam("id") String id,
            @PathParam("filename") String filename,
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

            java.nio.file.Path result =
                    Files.walk(Paths.get(applicationConfig.getRecollectFolder(id)))
                    .filter(Files::isRegularFile)
                    .filter(f-> Objects.equals( f.getFileName().toString(), filename))
                    .findFirst().orElse(null);

            //java.nio.file.Path result = Paths.get(String.format("%s/%s", applicationConfig.getRecollectFolder(id), filename));

            if(Files.notExists(result))
                return Response.status(Response.Status.NOT_FOUND).build();

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
                    .header("content-disposition","attachment; filename = " + filename)
                    .build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/config-default.json")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getConfigJSON() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("quality.defaults.conf");
            String input = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            return Response.status(Response.Status.ACCEPTED).entity(
                    new QualityConfig(input).getQualityConfig().root().render(ConfigRenderOptions.concise()))
                    .type(MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}
