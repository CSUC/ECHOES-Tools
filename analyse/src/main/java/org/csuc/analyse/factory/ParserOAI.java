package org.csuc.analyse.factory;

import org.apache.hadoop.fs.FileSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.analyse.strategy.ParserMethod;
import org.csuc.analyse.util.Garbage;
import org.csuc.deserialize.JaxbUnmarshal;
import org.javatuples.Pair;
import org.openarchives.oai._2.OAIPMHtype;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amartinez
 */
public class ParserOAI implements Parser {

    private static Logger logger = LogManager.getLogger(ParserOAI.class);

    private ParserMethod method;

    private int buffer = 1000;
    private AtomicInteger atomicInteger = new AtomicInteger();

    public ParserOAI(ParserMethod method){
        logger.debug(String.format("analyse: %s", getClass().getSimpleName()));
        this.method = method;
    }

    @Override
    public synchronized void execute(String fileOrPath) throws Exception {
        throw new IllegalArgumentException("execute fileOrPath is not valid for ParserFile!");
    }

    @Override
    public synchronized void execute(URL url)  throws Exception {
        iterate(url);

        logger.info(String.format("Completed "));
    }

    /**
     *
     * @param url
     */
    private void iterate(URL url) {
        logger.info("#{} - {}", atomicInteger.get(), url);

        try{
            OAIPMHtype oaipmHtype = (OAIPMHtype) new JaxbUnmarshal(url, new Class[]{OAIPMHtype.class}).getObject();

            Pair<ParserMethod, URL> pair = new Pair<>(method, url);
            pair.getValue0().parser(pair.getValue1());

            if (oaipmHtype.getListRecords().getResumptionToken() != null) {
                while (!oaipmHtype.getListRecords().getResumptionToken().getValue().isEmpty()) {
                    if ((atomicInteger.incrementAndGet() % buffer) == 0) Garbage.gc();

                    url = new URL(String.format("%s?verb=ListRecords&resumptionToken=%s", url.toString().replaceAll("\\?verb=.+", ""), oaipmHtype.getListRecords().getResumptionToken().getValue()));

                    logger.info("#{} - {}", atomicInteger.get(), url);

                    oaipmHtype = (OAIPMHtype) new JaxbUnmarshal(url, new Class[]{OAIPMHtype.class}).getObject();

                    if (Objects.nonNull(oaipmHtype)) {
                        pair = new Pair<>(method, url);
                        pair.getValue0().parser(pair.getValue1());
                    }
                }
            }
        }catch (Exception e){
            logger.error(e);
            logger.error("{}", url);
        }
    }


    @Override
    public synchronized void XML(OutputStream outs) {
        method.createXML(outs);
    }

    @Override
    public synchronized void HDFS_XML(FileSystem fileSystem, org.apache.hadoop.fs.Path dest) throws IOException{
        method.createHDFS_XML(fileSystem, dest);
    }

    @Override
    public synchronized void JSON(OutputStream outs) {
        method.createJSON(outs);
    }

    @Override
    public synchronized void HDFS_JSON(FileSystem fileSystem, org.apache.hadoop.fs.Path dest) throws IOException {
        method.createHDFS_JSON(fileSystem, dest);
    }
}
