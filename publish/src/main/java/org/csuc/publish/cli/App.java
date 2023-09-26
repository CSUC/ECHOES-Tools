package org.csuc.publish.cli;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.lang.CollectorStreamTriples;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.dao.QualityDetailsDAO;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.dao.impl.QualityDetailsDAOImpl;
import org.csuc.format.Datastore;
import org.csuc.publish.BlazegraphResponse;
import org.csuc.publish.RdfDAO;
import org.csuc.util.FormatType;
import org.csuc.util.StreamUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * @author amartinez
 */
public class App {

    private static Logger logger = LogManager.getLogger(App.class);

    private static ArgsBean bean;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Instant now = Instant.now();

        bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.exit(1);
        }

        bean.promptEnterKey();

        logger.info(String.format("start %s %s", logger.getName(), now));

        try {
            if (Objects.nonNull(bean.getInput())) {
                new ForkJoinPool(bean.getThreads()).submit(() -> {
                    try {
                        Files.walk(bean.getInput())
                                .filter(Files::isRegularFile)
                                .parallel()
                                .forEach((Path f) -> httpPost(bean.getContentType(), f.toFile(), bean.isReplace()));
                    } catch (IOException e) {
                        logger.error(e);
                    }
                }).join();
            } else if (bean.isDatastore()) {
                org.mongodb.morphia.Datastore datastore = new Datastore(bean.getHost(), bean.getPort(), bean.getName(), null, null).getDatastore();

                QualityDetailsDAO qualityDetailsDAO = new QualityDetailsDAOImpl(QualityDetails.class, datastore);

                Query<QualityDetails> qualityDetailsQuery =
                        Objects.nonNull(bean.getQuality_id())
                                ? qualityDetailsDAO.getValidById(bean.getQuality_id())
                                : qualityDetailsDAO.getValid();

                StreamUtils
                        .asStream(qualityDetailsQuery.fetch(new FindOptions().batchSize(50).noCursorTimeout(true)))
                        .parallel().forEach(qualityDetails -> {
                    RdfDAO<ByteArrayOutputStream> outputStreamRdfDAO = new RdfDAO<>(qualityDetails, ByteArrayOutputStream::new);
                    try {
                        httpPost(bean.getContentType(), qualityDetails.get_id(), outputStreamRdfDAO.toRDF(), bean.isReplace());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } finally {
            long diff = Duration.between(now, Instant.now()).getSeconds();
            logger.info(String.format("end %s %02d:%02d:%02d", logger.getName(), diff / 3600, diff % 3600 / 60, diff % 60));
        }
    }

    /**
     * @param type
     * @throws ClientProtocolException
     * @throws IOException
     */
    private static void httpPost(String type, File data, boolean replace) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        if(replace){
            CollectorStreamTriples inputStream = new CollectorStreamTriples();

            RDFParser.source(data.toPath()).lang(FormatType.convert(type).lang()).parse(inputStream);

            inputStream.getCollected().stream().map(Triple::getSubject).map(Node::getURI).collect(Collectors.toSet()).forEach(subject->{
                try {
                    //String encodedurl = URLEncoder.encode(String.format("http://%s/namespace/%s/sparql?c=<%s>&s=<%s>", bean.getHostname(), bean.getNamespace(), bean.getContext_uri(), subject),"UTF-8");
                    HttpDelete httpDelete =
                            new HttpDelete(String.format("http://%s/namespace/%s/sparql?c=%s&s=%s",
                                    bean.getHostname(), bean.getNamespace(), URLEncoder.encode(String.format("<%s>", bean.getContext_uri()), StandardCharsets.UTF_8.toString()), URLEncoder.encode(String.format("<%s>",subject), StandardCharsets.UTF_8.toString())));

                    HttpResponse response = httpclient.execute(httpDelete);
                    HttpEntity entity = response.getEntity();
                    if (entity != null) logger.info("[DELETE] - {} {}", subject, EntityUtils.toString(entity));
                } catch (IOException e) {
                    logger.error(e);
                }
            });
        }
        try {
            HttpPost httppost;
            if (Objects.nonNull(bean.getContext_uri()))
                httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql?context-uri=%s", bean.getHostname(), bean.getNamespace(), bean.getContext_uri()));
            else
                httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql", bean.getHostname(), bean.getNamespace()));


//            RequestConfig requestConfig =
//                    RequestConfig.copy(RequestConfig.DEFAULT)
//                            .setProxy(new HttpHost(bean.getHostname()))
//                            .build();
//
//            httppost.setConfig(requestConfig);

            //httppost.addHeader("content-type", type + ";charset=" + bean.getCharset());
            httppost.addHeader("content-type", FormatType.convert(type).lang().getContentType().getContentType());

            FileEntity entity = new FileEntity(data);

            httppost.setEntity(entity);


            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                BlazegraphResponse blazegraphResponse = xmlMapper(EntityUtils.toString(resEntity));
                if (Objects.nonNull(blazegraphResponse)) {
                    if (Integer.parseInt(blazegraphResponse.getModified()) != 0) {
                        logger.info("[INSERT] - {} {}", data.getAbsolutePath(), blazegraphResponse.toString());
                        if (bean.isDeleteFiles()) {
                            Files.delete(Paths.get(data.toURI()));
                            logger.info(String.format("%s deleted %s", "OK", data));
                        }
                    } else
                        logger.error(String.format("%s %s %s", "ERROR", data.getAbsolutePath(), blazegraphResponse.toString()));
                } else
                    logger.error(String.format("%s %s", "ERROR", data.getAbsolutePath()));

            }
            httpclient.close();

        } catch (IOException e) {
            logger.error(String.format("%s %s\n%s", "ERROR", data.getAbsolutePath(), e));
        }
    }

    private static void httpPost(String type, String id, ByteArrayOutputStream byteArrayOutputStream, boolean replace) throws UnsupportedEncodingException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httppost;
        if (Objects.nonNull(bean.getContext_uri()))
            httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql?context-uri=%s", bean.getHostname(), bean.getNamespace(), bean.getContext_uri()));
        else
            httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql", bean.getHostname(), bean.getNamespace()));


//        RequestConfig requestConfig =
//                RequestConfig.copy(RequestConfig.DEFAULT)
//                        .setProxy(new HttpHost(bean.getHostname()))
//                        .build();
//
//        httppost.setConfig(requestConfig);

        httppost.addHeader("content-type", type + ";charset=" + bean.getCharset());
        httppost.addHeader("content-type", FormatType.convert(type).lang().getContentType().getContentType());

        httppost.setEntity(new ByteArrayEntity(byteArrayOutputStream.toByteArray()));

        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                BlazegraphResponse blazegraphResponse = xmlMapper(EntityUtils.toString(resEntity));
                if (Objects.nonNull(blazegraphResponse)) {
                    if (Integer.parseInt(blazegraphResponse.getModified()) != 0) {
                        logger.info("[INSERT] - {} {}", id, blazegraphResponse.toString());
                    } else logger.error(String.format("%s %s %s", "ERROR", id, blazegraphResponse.toString()));
                } else logger.error(String.format("%s %s", "ERROR", id));
            }
            httpclient.close();

        } catch (IOException e) {
            logger.error(String.format("%s %s\n%s", "ERROR", id, e));
        }
    }


    /**
     * @param value
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public static BlazegraphResponse xmlMapper(String value) throws JsonParseException, JsonMappingException, IOException {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(true);
        XmlMapper xmlMapper = new XmlMapper(module);
        return xmlMapper.readValue(value, BlazegraphResponse.class);
    }
}
