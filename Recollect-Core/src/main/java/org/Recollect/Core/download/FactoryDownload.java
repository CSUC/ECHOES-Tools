package org.Recollect.Core.download;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import io.reactivex.Observable;
import nl.mindbus.a2a.A2AType;
import org.Recollect.Core.util.StreamUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.oai._2.*;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import javax.xml.bind.JAXBElement;
import java.util.*;

/**
 * @author amartinez
 */
public class FactoryDownload {

    private static Logger logger = LogManager.getLogger(FactoryDownload.class);

    public static Observable<Download> createDownloadIterator(Iterator<?> iteratorTypes, String xslt){
        Observable<Download> observable = Observable.create(emitter -> {
            try {
                if (Objects.nonNull(iteratorTypes)) {
                    //StreamUtils.asStream(iteratorTypes).parallel().forEach(record-> {
                    iteratorTypes.forEachRemaining(record -> {
                        if (Objects.nonNull(record)) {
                            if(record instanceof RecordType){
                                RecordType cast = (RecordType) record;
                                if (Objects.nonNull(cast.getHeader().getStatus()) && cast.getHeader().getStatus().equals(StatusType.DELETED)) {
                                    logger.debug(String.format("%s %s", cast.getHeader().getIdentifier(), StatusType.DELETED));
                                }else {
                                    if (cast.getMetadata().getAny() instanceof JAXBElement<?>) {
                                        emitter.onNext(new DownloadJaxb(new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}, cast));
                                    }else if (((RecordType)record).getMetadata().getAny() instanceof ElementNSImpl){
                                        emitter.onNext(new DownloadNode(xslt, cast));
                                    }else{
                                        logger.error(cast.getMetadata().getAny().getClass());
                                    }
                                }
                            }
                        }
                    });
                }
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
       return observable;
    }

    /**
     *
     * @param record
     */
    public static Observable<Download> createDownloadRecordType(RecordType record, String xslt){
        Observable<Download> observable = Observable.create(emitter -> {
            if (Objects.nonNull(record)) {
                if (record instanceof RecordType) {
                    RecordType cast = (RecordType) record;
                    if (Objects.nonNull(cast.getHeader().getStatus()) && cast.getHeader().getStatus().equals(StatusType.DELETED)) {
                        logger.debug(String.format("%s %s", cast.getHeader().getIdentifier(), StatusType.DELETED));
                    } else {
                        if (cast.getMetadata().getAny() instanceof JAXBElement<?>) {
                            emitter.onNext(new DownloadJaxb(new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class}, cast));
                        } else if (((RecordType) record).getMetadata().getAny() instanceof ElementNSImpl) {
                            emitter.onNext(new DownloadNode(xslt, cast));
                        } else {
                            logger.error(cast.getMetadata().getAny().getClass());
                        }
                    }
                }
            }
            emitter.onComplete();
        });

       return observable;
    }

    public void createDownloadIdentifyType(IdentifyType identifyType){

    }
}
