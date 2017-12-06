package org.Recollect.Core.download;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import io.reactivex.schedulers.Schedulers;
import nl.mindbus.a2a.A2AType;
import org.Recollect.Core.observable.ObservableList;
import org.Recollect.Core.observable.ObservableSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.oai._2.*;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import javax.xml.bind.JAXBElement;
import java.nio.file.Path;
import java.util.*;

/**
 * @author amartinez
 */
public class FactoryDownload {

    private static Logger logger = LogManager.getLogger(FactoryDownload.class);

    public static void createDownloadIterator(Iterator<?> iteratorTypes, Path outs, Map<String, String> properties, String xslt){
        ObservableSource<Download> observable = new ObservableSource<>();
        if(Objects.isNull(outs)){
            observable.getObservable()
                    .doOnNext(i-> logger.info(String.format("Emiting  %s in %s", i, Thread.currentThread().getName())))
                    .observeOn(Schedulers.io())
                    .subscribe(
                            (Download l) ->{
                                logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                                l.execute(properties);
                            } ,
                            e -> logger.error("Error: " + e),
                            () -> logger.info("Completed")
                    );

        }else{
            observable.getObservable()
                    .doOnNext(i-> logger.info(String.format("Emiting  %s in %s", i, Thread.currentThread().getName())))
                    .observeOn(Schedulers.io())
                    .subscribe(
                            (Download l) ->{
                                logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                                l.execute(outs, properties );
                            } ,
                            e -> logger.error("Error: " + e),
                            () -> logger.info("Completed")
                    );
        }

        if (Objects.nonNull(iteratorTypes)) {
            iteratorTypes.forEachRemaining(record -> {
                if (Objects.nonNull(record)) {
                    if(record instanceof RecordType){
                        RecordType cast = (RecordType) record;
                        if (Objects.nonNull(cast.getHeader().getStatus()) && cast.getHeader().getStatus().equals(StatusType.DELETED)) {
                            logger.debug(String.format("%s %s", cast.getHeader().getIdentifier(), StatusType.DELETED));
                        }else {
                            if (cast.getMetadata().getAny() instanceof JAXBElement<?>) {
                                observable.add(new DownloadJaxb(new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}, cast));
                            }else if (((RecordType)record).getMetadata().getAny() instanceof ElementNSImpl){
                                observable.add(new DownloadNode(xslt,cast));
                            }else{
                                logger.error(cast.getMetadata().getAny().getClass());
                            }
                        }
                    }
                }
            });
            observable.onCompleted();
        }
    }

    /**
     *
     * @param record
     */
    public static void createDownloadRecordType(RecordType record, Path outs, Map<String, String> properties, String xslt){
        ObservableSource<Download> observable = new ObservableSource<>();

        if(Objects.isNull(outs)){
            observable.getObservable()
                    .doOnNext(i-> logger.info(String.format("Emiting  %s in %s", i, Thread.currentThread().getName())))
                    .observeOn(Schedulers.io())
                    .subscribe(
                            (Download l) ->{
                                logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                                l.execute(properties);
                            } ,
                            e -> logger.error("Error: " + e),
                            () -> logger.info("Completed")
                    );

        }else{
            observable.getObservable()
                    .doOnNext(i-> logger.info(String.format("Emiting  %s in %s", i, Thread.currentThread().getName())))
                    .observeOn(Schedulers.io())
                    .subscribe(
                            (Download l) ->{
                                logger.info(String.format("Received in %s value %s", Thread.currentThread().getName(), l));
                                l.execute(outs, properties );
                            } ,
                            e -> logger.error("Error: " + e),
                            () -> logger.info("Completed")
                    );
        }

        if (Objects.nonNull(record)) {
            if (record instanceof RecordType) {
                RecordType cast = (RecordType) record;
                if (Objects.nonNull(cast.getHeader().getStatus()) && cast.getHeader().getStatus().equals(StatusType.DELETED)) {
                    logger.debug(String.format("%s %s", cast.getHeader().getIdentifier(), StatusType.DELETED));
                } else {
                    if (cast.getMetadata().getAny() instanceof JAXBElement<?>) {
                        observable.add(new DownloadJaxb(new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}, cast));
                    } else if (((RecordType) record).getMetadata().getAny() instanceof ElementNSImpl) {
                        observable.add(new DownloadNode(xslt, cast));
                    } else {
                        logger.error(cast.getMetadata().getAny().getClass());
                    }
                }
            }
            observable.onCompleted();
        }
    }

    public void createDownloadIdentifyType(IdentifyType identifyType){

    }
}
