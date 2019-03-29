package org.transformation.download;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import org.EDM.Transformations.formats.utils.SchemaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2.StatusType;
import org.transformation.util.ItemIterator;
import org.transformation.util.StreamUtils;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBIntrospector;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.transformation.util.StreamUtils.asStream;

/**
 * @author amartinez
 */
public class FactoryDownload {

    private static Logger logger = LogManager.getLogger(FactoryDownload.class);

    public static Observable<Download> createDownloadIterator(ItemIterator<?> iteratorTypes, SchemaType schemaType) {
        Observable<Download> observable = Observable.create(emitter -> {
                if (Objects.nonNull(iteratorTypes)) {
                    StreamUtils.asStream(iteratorTypes).parallel().filter(Objects::nonNull).forEach(record->{
//                    Stream.generate(iteratorTypes::next).parallel().forEach(record-> {
//                    iteratorTypes.forEachRemaining(record -> {
//                        if (Objects.nonNull(record)) {
                            if(record instanceof RecordType){
                                RecordType cast = (RecordType) record;
                                getRecord(cast, schemaType, emitter);
                            }
//                        }
                    });
                    if(!iteratorTypes.getException().isEmpty())
                        throw new Exception(iteratorTypes.getException().stream().collect(Collectors.joining()));
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
    private static Object getMetadata(Object object, SchemaType schemaType) throws Exception {
        if(JAXBIntrospector.getValue(object).getClass().equals(schemaType.getType()))
            return JAXBIntrospector.getValue(object);
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
                    emitter.onNext(new Jaxb(record, getMetadata(record.getAbout().stream().findFirst().get().getAny(), schemaType), schemaType));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (Objects.nonNull(record.getMetadata()))
                    new Exception(String.format("[%s] MetadataType is empty or null", record.getHeader().getIdentifier()));
                try {
                    if(record.getMetadata().getAny() instanceof JAXBElement)
                        emitter.onNext(new Jaxb(record, getMetadata(record.getMetadata().getAny(), schemaType), schemaType));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
