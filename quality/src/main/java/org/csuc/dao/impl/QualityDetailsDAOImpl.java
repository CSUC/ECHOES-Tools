package org.csuc.dao.impl;

import com.mongodb.WriteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.QualityDetailsDAO;
import org.csuc.dao.entity.Quality;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.dao.quality.QualityDAO;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
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

        logger.debug("[{}]\t[getByUser] - user: {}\toption:[offset: {}\tlimit: {}\torder: {}]", QualityDAOImpl.class.getSimpleName(), objectId, offset, limit, orderby);

        QualityDAO qualityDAO = new QualityDAOImpl(Quality.class, getDatastore());

        Quality quality = qualityDAO.getById(objectId);

        Query<QualityDetails> query = createQuery();

        query.and(
                query.criteria("quality").equal(quality),
                query.or(
                        query.criteria("schema").notEqual(null),
                        query.criteria("schematron").notEqual(null)
                )
        );

        return (Objects.nonNull(orderby))
                ? query.order(orderby).asList(new FindOptions().skip(offset > 0 ? ((offset - 1) * limit) : 0).limit(limit))
                : query.asList(new FindOptions().skip(offset > 0 ? ((offset - 1) * limit) : 0).limit(limit));
    }

    @Override
    public long countErrorsById(String objectId) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();

        logger.debug("[{}]\t[countErrorsById] - objectId: {}", QualityDAOImpl.class.getSimpleName(), objectId);

        QualityDAO qualityDAO = new QualityDAOImpl(Quality.class, getDatastore());

        Quality quality = qualityDAO.getById(objectId);

        Query<QualityDetails> query = createQuery();

        query.and(
                query.criteria("quality").equal(quality),
                query.or(
                        query.criteria("schema").notEqual(null),
                        query.criteria("schematron").notEqual(null)
                )
        );

        return query.count();
    }
}
