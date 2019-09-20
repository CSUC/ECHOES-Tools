package org.csuc.analyse.factory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amartinez
 */
public class ParserOAI implements Parser {

    private static Logger logger = LogManager.getLogger(ParserOAI.class);

    private ParserMethod method;

    private AtomicInteger iter = new AtomicInteger(0);
    private int buffer = 15000;

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
        Observable<Pair<ParserMethod, URL>> observable = Observable.create(emitter -> {
            iterate(url, emitter);

            emitter.onComplete();
        });

        AtomicInteger assigner = new AtomicInteger(0);
        int coreCount = Runtime.getRuntime().availableProcessors();

        observable
                .doOnNext(i -> logger.info(String.format("#%s   Emiting  %s in %s", iter.incrementAndGet(), i.getValue0(), Thread.currentThread().getName())))
                .groupBy(i -> assigner.incrementAndGet() % coreCount)
                .flatMap(grp -> grp.observeOn(Schedulers.io())
                        .map(i2 -> intenseCalculation(i2))
                )
                .subscribe(
                        (Pair<ParserMethod, URL> l) -> {
                            if ((iter.get() % buffer) == 0) Garbage.gc();
                            logger.info("Received in {} value {}", Thread.currentThread().getName(), l.getValue0());
                        },
                        e -> logger.error("Error: " + e),
                        () -> logger.info(String.format("Completed "))
                );
        Thread.sleep(3000);
    }

    public static <T> T intenseCalculation(T value) throws Exception {
        logger.info("Calculating {} {} on thread {}", ((Pair<ParserMethod, URL>) value).getValue0(), LocalTime.now(), Thread.currentThread().getName());

        ((Pair<ParserMethod, URL>) value).getValue0().parser(((Pair<ParserMethod, URL>) value).getValue1());

        return value;
    }

    /**
     *
     * @param url
     * @param emitter
     * @throws MalformedURLException
     */
    private void iterate(URL url, ObservableEmitter<Pair<ParserMethod, URL>> emitter) throws MalformedURLException {
        OAIPMHtype oaipmHtype =
                    (OAIPMHtype) new JaxbUnmarshal(url, new Class[]{OAIPMHtype.class}).getObject();

        emitter.onNext(new Pair<>(method, url));

        if (oaipmHtype.getListRecords().getResumptionToken() != null)
            if (!oaipmHtype.getListRecords().getResumptionToken().getValue().isEmpty())
                iterate(next(url, oaipmHtype.getListRecords().getResumptionToken().getValue()), emitter);
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


    private URL next(URL url, String resumptionToken) throws MalformedURLException {
        return new URL(String.format("%s?verb=ListRecords&resumptionToken=%s",
                url.toString().replaceAll("\\?verb=.+", ""), resumptionToken));
    }
}
