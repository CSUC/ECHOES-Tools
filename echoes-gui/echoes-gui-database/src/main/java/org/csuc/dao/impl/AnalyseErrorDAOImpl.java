package org.csuc.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.AnalyseErrorDAO;
import org.csuc.entities.Analyse;
import org.csuc.entities.AnalyseError;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

/**
 * @author amartinez
 */
public class AnalyseErrorDAOImpl extends BasicDAO<AnalyseError, ObjectId> implements AnalyseErrorDAO {

    private static Logger logger = LogManager.getLogger(AnalyseErrorDAOImpl.class);

    public AnalyseErrorDAOImpl(Class<AnalyseError> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public AnalyseError getByReference(Analyse analyse) throws Exception {
        logger.info("[{}]\t[getByReference] - analyse: {}", AnalyseErrorDAOImpl.class.getSimpleName(), analyse.toString());
        return find(createQuery().field("analyse-id").equal(analyse)).get();
    }

    @Override
    public AnalyseError getByReference(String objectId) throws Exception {
        logger.debug("[{}]\t[getByReference] - objectId: {}", AnalyseErrorDAOImpl.class.getSimpleName(), objectId);

        Analyse analyse = getDatastore().createQuery(Analyse.class).field("_id").equal(objectId).get();
        Query<AnalyseError> query = createQuery();
        query.criteria("analyse-id").equals(getDatastore().getKey(analyse));

        return find(query).get();
    }

}
