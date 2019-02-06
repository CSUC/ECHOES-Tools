package org.csuc.dao.impl;

import com.mongodb.WriteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.AnalyseErrorDAO;
import org.csuc.entities.Analyse;
import org.csuc.entities.AnalyseError;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.Objects;

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
        return find(createQuery().field("analyse-id").equal(analyse)).get();
    }

    @Override
    public WriteResult deleteByReference(String objectId) throws Exception {
        if(Objects.isNull(objectId))    throw new Exception();
        Analyse analyse = getDatastore().createQuery(Analyse.class).field("_id").equal(objectId).get();

        return deleteByReference(analyse);
    }

    @Override
    public WriteResult deleteByReference(Analyse analyse) throws Exception {
        if(Objects.isNull(analyse))    throw new Exception();
        WriteResult writeResult = delete(find(createQuery().field("analyse-id").equal(analyse)).get());
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteByReference] - analyse: {}", AnalyseErrorDAOImpl.class.getSimpleName(), analyse);
        return writeResult;
    }

    @Override
    public WriteResult deleteById(String objectId) throws Exception {
        if(Objects.isNull(objectId))    throw new Exception();

        AnalyseError analyseError = createQuery().field("_id").equal(objectId).get();

        WriteResult writeResult =  delete(analyseError);
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteById] - objectId: {}", AnalyseErrorDAOImpl.class.getSimpleName(), objectId);
        return writeResult;
    }

}
