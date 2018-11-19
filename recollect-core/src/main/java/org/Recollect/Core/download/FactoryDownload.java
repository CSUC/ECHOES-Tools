package org.Recollect.Core.download;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import nl.mindbus.a2a.A2AType;
import org.EDM.Transformations.formats.utils.SchemaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2.StatusType;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author amartinez
 */
public class FactoryDownload {

    private static Logger logger = LogManager.getLogger(FactoryDownload.class);

    public static Observable<Download> createDownloadIterator(Iterator<?> iteratorTypes, SchemaType schemaType) {
        Observable<Download> observable = Observable.create(emitter -> {
                if (Objects.nonNull(iteratorTypes)) {
                    iteratorTypes.forEachRemaining(record -> {
                        if (Objects.nonNull(record)) {
                            if(record instanceof RecordType){
                                RecordType cast = (RecordType) record;
                                getRecord(cast, schemaType, emitter);
                            }
                        }
                    });
                }
                emitter.onComplete();
        });
       return observable;
    }

    /**
     *
     * @param record
     */
    public static Observable<Download> createDownloadRecordType(RecordType record, SchemaType schemaType) {
        Observable<Download> observable = Observable.create(emitter -> {
            if (Objects.nonNull(record)) {
                getRecord(record, schemaType, emitter);
            }
            emitter.onComplete();
        });

       return observable;
    }

    /**
     *
     * @param object
     * @param schemaType
     * @return
     * @throws Exception
     */
    private static JAXBElement<?> getMetadata(Object object, SchemaType schemaType) throws Exception {
        if (object instanceof JAXBElement<?>) {
            JAXBElement<?> metadata = (JAXBElement<?>) object;
            if(metadata.getDeclaredType().equals(schemaType.getType())){
                return metadata;
            }
        }
        throw new Exception(String.format("%s don't cast into %s", object, schemaType));
    }

    /**
     *
     * @param object
     * @param schemaType
     * @return
     * @throws Exception
     */
    private static JAXBElement<?> getAbout(Object object, SchemaType schemaType) throws Exception {
        if(object.getClass().equals(schemaType.getType()))
            return new JAXBElement(new QName(schemaType.getType().getSimpleName()), schemaType.getType(), object);
        throw new Exception(String.format("%s don't cast into %s", object, schemaType));
    }

    /**
     *
     * @param record
     * @param schemaType
     * @param emitter
     */
    private static void getRecord(RecordType record, SchemaType schemaType, ObservableEmitter<Download> emitter) {
        if (Objects.nonNull(record.getHeader().getStatus()) && record.getHeader().getStatus().equals(StatusType.DELETED)) {
            logger.debug(String.format("%s %s", record.getHeader().getIdentifier(), StatusType.DELETED));
        } else {
            if (schemaType.equals(SchemaType.MEMORIX)) {
                if (record.getAbout().isEmpty())
                    new Exception(String.format("[%s] AboutType is empty or null", record.getHeader().getIdentifier()));
                try {
                    emitter.onNext(new Jaxb(record, getAbout(record.getAbout().stream().findFirst().get().getAny(), schemaType), schemaType));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (Objects.nonNull(record.getMetadata()))
                    new Exception(String.format("[%s] MetadataType is empty or null", record.getHeader().getIdentifier()));
                try {
                    emitter.onNext(new Jaxb(record, getMetadata(record.getMetadata().getAny(), schemaType), schemaType));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
