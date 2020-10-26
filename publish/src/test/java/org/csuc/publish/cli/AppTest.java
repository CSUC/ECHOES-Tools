package org.csuc.publish.cli;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.io.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
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
import org.csuc.publish.BlazegraphResponse;
import org.csuc.util.FormatType;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;


public class AppTest {

    private static Logger logger = LogManager.getLogger(AppTest.class);
    private String HOSTNAME = "blazegraph.test.csuc.cat";
    private String NAMESPACE = "test";
    private String CONTEXT_URI = "vg:test";
    private String CONTENT_TYPE = "RDFXML";

    @Test
    public void test(){
        CollectorStreamTriples inputStream = new CollectorStreamTriples();

        RDFParser.source(Paths.get("/tmp/edm.rdf")).lang(Lang.RDFXML).parse(inputStream);

//        inputStream.getCollected().forEach(quad -> {
//            System.out.println(quad.getSubject().getURI());
//        });


//        System.out.println(inputStream.getCollected().stream().map(Triple::getSubject).map(Node::getURI).collect(Collectors.toSet()));
        inputStream.getCollected().stream().map(Triple::getSubject).map(Node::getURI).collect(Collectors.toSet()).forEach(System.out::println);

    }

    @Test
    public void test2(){
        App app = new App();
        try {
            Files.walk(Paths.get("/home/albert/Descargas/edm.rdf"))
                    .filter(Files::isRegularFile)
                    .parallel()
                    .forEach((Path f) -> httpPost(CONTENT_TYPE, f.toFile(), true));
        } catch (IOException e) {
            logger.error(e);
        }
    }


    private void httpPost(String type, File data, boolean replace) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        if(replace){
            CollectorStreamTriples inputStream = new CollectorStreamTriples();
            RDFParser.source(data.toPath()).lang(Lang.RDFXML).parse(inputStream);

            inputStream.getCollected().stream().map(Triple::getSubject).map(Node::getURI).collect(Collectors.toSet()).forEach(subject->{
                try {
                    HttpDelete httpDelete =
                            new HttpDelete(String.format("http://%s/namespace/%s/sparql?c=%s&s=%s",
                                HOSTNAME, NAMESPACE, URLEncoder.encode(String.format("<%s>",CONTEXT_URI), StandardCharsets.UTF_8.toString()), URLEncoder.encode(String.format("<%s>",subject), StandardCharsets.UTF_8.toString())));

                    HttpResponse response = httpclient.execute(httpDelete);
                    HttpEntity entity = response.getEntity();
                    if (entity != null) logger.info("[DELETE] - {} {}", subject, EntityUtils.toString(entity));
                } catch (IOException e) {
                    logger.error(e);
                }
            });
        }

//        try {
//            HttpPost httppost;
//            if (Objects.nonNull(CONTEXT_URI))
//                httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql?context-uri=%s", HOSTNAME, NAMESPACE, CONTEXT_URI));
//            else
//                httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql", HOSTNAME, NAMESPACE));
//
//            RequestConfig requestConfig =
//                    RequestConfig.copy(RequestConfig.DEFAULT)
//                            .setProxy(new HttpHost(HOSTNAME))
//                            .build();
//
//            httppost.setConfig(requestConfig);
//
//            httppost.addHeader("content-type", FormatType.convert(CONTENT_TYPE).lang().getContentType().getContentType());
//
//            FileEntity entity = new FileEntity(data);
//
//            httppost.setEntity(entity);
//
//
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity resEntity = response.getEntity();
//
//            if (resEntity != null) {
//                BlazegraphResponse blazegraphResponse = xmlMapper(EntityUtils.toString(resEntity));
//                if (Objects.nonNull(blazegraphResponse)) {
//                    if (Integer.parseInt(blazegraphResponse.getModified()) != 0) {
//                        logger.info(String.format("%s %s %s", "OK", data.getAbsolutePath(), blazegraphResponse.toString()));
//                    } else
//                        logger.error(String.format("%s %s %s", "ERROR", data.getAbsolutePath(), blazegraphResponse.toString()));
//                } else
//                    logger.error(String.format("%s %s", "ERROR", data.getAbsolutePath()));
//
//            }
//            httpclient.close();
//        } catch (IOException e) {
//            logger.error(String.format("%s %s\n%s", "ERROR", data.getAbsolutePath(), e));
//        }
    }

    public BlazegraphResponse xmlMapper(String value) throws JsonParseException, JsonMappingException, IOException {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(true);
        XmlMapper xmlMapper = new XmlMapper(module);
        return xmlMapper.readValue(value, BlazegraphResponse.class);
    }
}