package org.csuc.analyse.factory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;
import org.apache.hadoop.fs.FileSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.analyse.strategy.ParserMethod;
import org.csuc.deserialize.JaxbUnmarshal;
import org.javatuples.Pair;
import org.openarchives.oai._2.OAIPMHtype;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amartinez
 */
public class ParserOAI implements Parser {

    private static Logger logger = LogManager.getLogger(ParserOAI.class);

    private ParserMethod method;

    private AtomicInteger iter = new AtomicInteger(0);

    public ParserOAI(ParserMethod method){
        logger.debug(String.format("analyse: %s", getClass().getSimpleName()));
        this.method = method;
    }

    @Override
    public void execute(String fileOrPath) throws Exception {
        throw new IllegalArgumentException("execute fileOrPath is not valid for ParserFile!");
    }

    @Override
    public void execute(URL url)  throws Exception {
        Observable<Pair<ParserMethod, URL>> observable = Observable.create(emitter -> {
            iterate(null, url, emitter);

            emitter.onComplete();
        });

        AtomicInteger batch = new AtomicInteger(0);
        observable
                .doOnNext(i -> logger.info(String.format("#%s   Emiting  %s in %s", iter.incrementAndGet(), i, Thread.currentThread().getName())))
                .observeOn(Schedulers.io())
                .subscribe(
                        (Pair<ParserMethod, URL> l) -> {
                            logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                            l.getValue0().parser(l.getValue1());
                        },
                        e -> logger.error("Error: " + e),
                        () -> logger.info(String.format("Completed "))
                );
        Thread.sleep(3000);
    }


    /**
     *
     * @param oaipmHtype
     * @param url
     * @param emitter
     * @throws MalformedURLException
     */
    private void iterate(OAIPMHtype oaipmHtype, URL url, ObservableEmitter<Pair<ParserMethod, URL>> emitter) throws MalformedURLException {
        //if(Objects.isNull(oaipmHtype)) {
        oaipmHtype =
                    (OAIPMHtype) new JaxbUnmarshal(url, new Class[]{OAIPMHtype.class}).getObject();
        emitter.onNext(new Pair<>(method, url));

        if (oaipmHtype.getListRecords().getResumptionToken() != null) {
            if (!oaipmHtype.getListRecords().getResumptionToken().getValue().isEmpty()) {
                //logger.info("{}\t{}", iter.incrementAndGet(), next(url, oaipmHtype.getListRecords().getResumptionToken().getValue()));
                iterate(oaipmHtype, next(url, oaipmHtype.getListRecords().getResumptionToken().getValue()), emitter);
            }
        }
    }

    @Override
    public void XML(OutputStream outs) {
        method.createXML(outs);
    }

    @Override
    public void HDFS_XML(FileSystem fileSystem, org.apache.hadoop.fs.Path dest) throws IOException{
        method.createHDFS_XML(fileSystem, dest);
    }

    @Override
    public void JSON(OutputStream outs) {
        method.createJSON(outs);
    }

    @Override
    public void HDFS_JSON(FileSystem fileSystem, org.apache.hadoop.fs.Path dest) throws IOException {
        method.createHDFS_JSON(fileSystem, dest);
    }


    private URL next(URL url, String resumptionToken) throws MalformedURLException {
        return new URL(String.format("%s?verb=ListRecords&resumptionToken=%s",
                url.toString().replaceAll("\\?verb=.+", ""), resumptionToken));
    }
}
