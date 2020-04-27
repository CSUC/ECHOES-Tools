package org.csuc.publish.cli;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.core.HDFS;
import org.csuc.dao.QualityDetailsDAO;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.dao.impl.QualityDetailsDAOImpl;
import org.csuc.format.Datastore;
import org.csuc.publish.BlazegraphResponse;
import org.csuc.publish.RdfDAO;
import org.csuc.util.StreamUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

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
                                .forEach((Path f) -> httpPost(bean.getContentType().lang().getContentType().getContentType(), f.toFile()));
                    } catch (IOException e) {
                        logger.error(e);
                    }
                }).join();
            } else if (bean.isHdfs()) {
                HDFS hdfs = new HDFS(bean.getHdfsuri(), bean.getHdfsuser(), bean.getHdfshome());

                //Job job = Job.getInstance(hdfs.getFileSystem().getConf());
                RemoteIterator<LocatedFileStatus> fileStatusListIterator =
                        hdfs.getFileSystem().listFiles(new org.apache.hadoop.fs.Path(System.getProperty("hadoop.home.dir"), "transformation"), true);

                while(fileStatusListIterator.hasNext()){
                    LocatedFileStatus fileStatus = fileStatusListIterator.next();
                    logger.info(fileStatus.getPath().toString());

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    HDFS.get(
                            hdfs.getFileSystem(),
                            fileStatus.getPath(),
                            byteArrayOutputStream);

                    httpPost(bean.getContentType().lang().getContentType().getContentType(), byteArrayOutputStream);
                }
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
    private static void httpPost(String type, File data) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httppost;
        if (Objects.nonNull(bean.getContext_uri()))
            httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql?context-uri=%s", bean.getHostname(), bean.getNamespace(), bean.getContext_uri()));
        else
            httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql", bean.getHostname(), bean.getNamespace()));


        RequestConfig requestConfig =
                RequestConfig.copy(RequestConfig.DEFAULT)
                        .setProxy(new HttpHost(bean.getHostname()))
                        .build();

        httppost.setConfig(requestConfig);

        httppost.addHeader("content-type", type + ";charset=" + bean.getCharset());

        FileEntity entity = new FileEntity(data);

        httppost.setEntity(entity);

        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                BlazegraphResponse blazegraphResponse = xmlMapper(EntityUtils.toString(resEntity));
                if (Objects.nonNull(blazegraphResponse)) {
                    if (Integer.parseInt(blazegraphResponse.getModified()) != 0) {
                        logger.info(String.format("%s %s %s", "OK", data.getAbsolutePath(), blazegraphResponse.toString()));

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

    private static void httpPost(String type, String id, ByteArrayOutputStream byteArrayOutputStream) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httppost;
        if (Objects.nonNull(bean.getContext_uri()))
            httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql?context-uri=%s", bean.getHostname(), bean.getNamespace(), bean.getContext_uri()));
        else
            httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql", bean.getHostname(), bean.getNamespace()));


        RequestConfig requestConfig =
                RequestConfig.copy(RequestConfig.DEFAULT)
                        .setProxy(new HttpHost(bean.getHostname()))
                        .build();

        httppost.setConfig(requestConfig);

        httppost.addHeader("content-type", type + ";charset=" + bean.getCharset());

        httppost.setEntity(new ByteArrayEntity(byteArrayOutputStream.toByteArray()));

        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                BlazegraphResponse blazegraphResponse = xmlMapper(EntityUtils.toString(resEntity));
                if (Objects.nonNull(blazegraphResponse)) {
                    if (Integer.parseInt(blazegraphResponse.getModified()) != 0) {
                        logger.info(String.format("%s %s %s", "OK", id, blazegraphResponse.toString()));
                    } else logger.error(String.format("%s %s %s", "ERROR", id, blazegraphResponse.toString()));
                } else logger.error(String.format("%s %s", "ERROR", id));
            }
            httpclient.close();

        } catch (IOException e) {
            logger.error(String.format("%s %s\n%s", "ERROR", id, e));
        }
    }

    private static void httpPost(String type, ByteArrayOutputStream byteArrayOutputStream) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httppost;
        if (Objects.nonNull(bean.getContext_uri()))
            httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql?context-uri=%s", bean.getHostname(), bean.getNamespace(), bean.getContext_uri()));
        else
            httppost = new HttpPost(String.format("http://%s/namespace/%s/sparql", bean.getHostname(), bean.getNamespace()));


        RequestConfig requestConfig =
                RequestConfig.copy(RequestConfig.DEFAULT)
                        .setProxy(new HttpHost(bean.getHostname()))
                        .build();

        httppost.setConfig(requestConfig);

        httppost.addHeader("content-type", type + ";charset=" + bean.getCharset());

        httppost.setEntity(new ByteArrayEntity(byteArrayOutputStream.toByteArray()));

        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                BlazegraphResponse blazegraphResponse = xmlMapper(EntityUtils.toString(resEntity));
                if (Objects.nonNull(blazegraphResponse)) {
                    if (Integer.parseInt(blazegraphResponse.getModified()) != 0) {
                        logger.info(String.format("%s %s", "OK", blazegraphResponse.toString()));
                    } else logger.error(String.format("%s %s", "ERROR", blazegraphResponse.toString()));
                } else logger.error(String.format("%s", "ERROR"));
            }
            httpclient.close();

        } catch (IOException e) {
            logger.error(String.format("%s\n%s", "ERROR", e));
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
