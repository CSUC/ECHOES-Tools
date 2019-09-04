package org.csuc.dao.impl;

import com.mongodb.DBRef;
import com.mongodb.WriteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.QualityDetailsDAO;
import org.csuc.dao.entity.Quality;
import org.csuc.dao.entity.QualityDetails;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import java.util.List;
import java.util.Objects;


/**
 * @author amartinez
 */
public class QualityDetailsDAOImpl extends BasicDAO<QualityDetails, ObjectId> implements QualityDetailsDAO {

    private static Logger logger = LogManager.getLogger(QualityDetailsDAOImpl.class);

    public QualityDetailsDAOImpl(Class<QualityDetails> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public QualityDetails getById(String objectId) throws Exception {
        QualityDetails qualityDetailsDAO = createQuery().field("_id").equal(objectId).get();
        if (Objects.isNull(qualityDetailsDAO)) throw new Exception();

        logger.debug("[{}]\t[getById] - objectId: {}", QualityDetailsDAOImpl.class.getSimpleName(), objectId);
        return qualityDetailsDAO;
    }

    @Override
    public Key<QualityDetails> insert(QualityDetails qualityDetails) throws Exception {
        if (Objects.isNull(qualityDetails)) throw new Exception();
        Key<QualityDetails> result = save(qualityDetails);
        if (Objects.isNull(result)) throw new Exception();

        logger.debug("[{}]\t[insert] - quality: {}", QualityDetailsDAOImpl.class.getSimpleName(), qualityDetails.toString());
        return result;
    }

    @Override
    public WriteResult deleteById(String objectId) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();
        WriteResult writeResult = delete(getById(objectId));
        if (Objects.isNull(writeResult)) throw new Exception();

        logger.debug("[{}]\t[deleteById] - objectId: {}", QualityDetailsDAOImpl.class.getSimpleName(), objectId);
        return writeResult;
    }

    @Override
    public List<QualityDetails> getErrorsById(String objectId, int offset, int limit, String orderby) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();

        logger.debug("[{}]\t[getErrorsById] - user: {}\toption:[offset: {}\tlimit: {}\torder: {}]", QualityDetailsDAOImpl.class.getSimpleName(), objectId, offset, limit, orderby);

        Query<QualityDetails> query = createQuery();

        query.and(
                query.criteria("quality").equal(new DBRef(Quality.class.getAnnotation(Entity.class).value(), objectId)),
                query.or(
                        query.criteria("schema").exists(),
                        query.criteria("schematron").exists(),
                        query.criteria("edm.edm:ProvidedCHO.errorList").exists(),
                        query.criteria("edm.edm:Place.errorList").exists(),
                        query.criteria("edm.skos:Concept.errorList").exists(),
                        query.criteria("edm.edm:TimeSpan.errorList").exists(),
                        query.criteria("edm.edm:Agent.errorList").exists(),
                        query.criteria("edm.ore:Aggregation.errorList").exists(),
                        query.criteria("edm.edm:WebResource.errorList").exists()
                )
        );

        return (Objects.nonNull(orderby))
                ? query.order(orderby).asList(new FindOptions().skip(offset > 0 ? ((offset - 1) * limit) : 0).limit(limit))
                : query.asList(new FindOptions().skip(offset > 0 ? ((offset - 1) * limit) : 0).limit(limit));
    }

    @Override
    public Query<QualityDetails> getErrorsById(String objectId) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();

        Query<QualityDetails> query = createQuery();

        query.and(
                query.criteria("quality").equal(new DBRef(Quality.class.getAnnotation(Entity.class).value(), objectId)),
                query.or(
                        query.criteria("schema").exists(),
                        query.criteria("schematron").exists(),
                        query.criteria("edm.edm:ProvidedCHO.errorList").exists(),
                        query.criteria("edm.edm:Place.errorList").exists(),
                        query.criteria("edm.skos:Concept.errorList").exists(),
                        query.criteria("edm.edm:TimeSpan.errorList").exists(),
                        query.criteria("edm.edm:Agent.errorList").exists(),
                        query.criteria("edm.ore:Aggregation.errorList").exists(),
                        query.criteria("edm.edm:WebResource.errorList").exists()
                )
        );

        return query;
    }

    @Override
    public Query<QualityDetails> getStep1Errors(String objectId) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();

        Query<QualityDetails> query = createQuery();

        query.and(
                query.criteria("quality").equal(new DBRef(Quality.class.getAnnotation(Entity.class).value(), objectId)),
                query.criteria("schema").exists()
                );

        return query;
    }

    @Override
    public Query<QualityDetails> getStep2Errors(String objectId) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();

        Query<QualityDetails> query = createQuery();

        query.and(
                query.criteria("quality").equal(new DBRef(Quality.class.getAnnotation(Entity.class).value(), objectId)),
                query.criteria("schematron").exists()
        );

        return query;
    }

    @Override
    public Query<QualityDetails> getStep3Errors(String objectId) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();

        Query<QualityDetails> query = createQuery();

        query.and(
                query.criteria("quality").equal(new DBRef(Quality.class.getAnnotation(Entity.class).value(), objectId)),
                query.or(
                        query.criteria("edm.edm:ProvidedCHO.errorList").exists(),
                        query.criteria("edm.edm:Place.errorList").exists(),
                        query.criteria("edm.skos:Concept.errorList").exists(),
                        query.criteria("edm.edm:TimeSpan.errorList").exists(),
                        query.criteria("edm.edm:Agent.errorList").exists(),
                        query.criteria("edm.ore:Aggregation.errorList").exists(),
                        query.criteria("edm.edm:WebResource.errorList").exists()
                )
        );

        return query;
    }

    @Override
    public long countErrorsById(String objectId) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();

        logger.debug("[{}]\t[countErrorsById] - objectId: {}", QualityDetailsDAOImpl.class.getSimpleName(), objectId);

        Query<QualityDetails> query = createQuery();

        query.and(
                query.criteria("quality").equal(new DBRef(Quality.class.getAnnotation(Entity.class).value(), objectId)),
                query.or(
                        query.criteria("schema").exists(),
                        query.criteria("schematron").exists(),
                        query.criteria("edm.edm:ProvidedCHO.errorList").exists(),
                        query.criteria("edm.edm:Place.errorList").exists(),
                        query.criteria("edm.skos:Concept.errorList").exists(),
                        query.criteria("edm.edm:TimeSpan.errorList").exists(),
                        query.criteria("edm.edm:Agent.errorList").exists(),
                        query.criteria("edm.ore:Aggregation.errorList").exists(),
                        query.criteria("edm.edm:WebResource.errorList").exists()
                )
        );

        return query.count();
    }

    @Override
    public long countErrorsStep1ById(String objectId) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();

        Query<QualityDetails> query = createQuery();

        query.and(
                query.criteria("quality").equal(new DBRef(Quality.class.getAnnotation(Entity.class).value(), objectId)),
                query.criteria("schema").exists()
        );

        return query.count();
    }

    @Override
    public long countErrorsStep2ById(String objectId) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();

        Query<QualityDetails> query = createQuery();

        query.and(
                query.criteria("quality").equal(new DBRef(Quality.class.getAnnotation(Entity.class).value(), objectId)),
                query.criteria("schematron").exists()
        );

        return query.count();
    }

    @Override
    public long countErrorsStep3ById(String objectId) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();

        Query<QualityDetails> query = createQuery();

        query.and(
                query.criteria("quality").equal(new DBRef(Quality.class.getAnnotation(Entity.class).value(), objectId)),
                query.or(
                        query.criteria("edm.edm:ProvidedCHO.errorList").exists(),
                        query.criteria("edm.edm:Place.errorList").exists(),
                        query.criteria("edm.skos:Concept.errorList").exists(),
                        query.criteria("edm.edm:TimeSpan.errorList").exists(),
                        query.criteria("edm.edm:Agent.errorList").exists(),
                        query.criteria("edm.ore:Aggregation.errorList").exists(),
                        query.criteria("edm.edm:WebResource.errorList").exists()
                )
        );

        return query.count();
    }
}
