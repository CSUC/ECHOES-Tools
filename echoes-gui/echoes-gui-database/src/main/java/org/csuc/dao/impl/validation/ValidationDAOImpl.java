package org.csuc.dao.impl.validation;

import com.mongodb.WriteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.validation.ValidationDAO;
import org.csuc.entities.validation.Validation;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.Objects;


/**
 * @author amartinez
 */
public class ValidationDAOImpl extends BasicDAO<Validation, ObjectId> implements ValidationDAO {

    private static Logger logger = LogManager.getLogger(ValidationDAOImpl.class);

    public ValidationDAOImpl(Class<Validation> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public Validation getById(String objectId) throws Exception {
        Validation validation = createQuery().field("_id").equal(objectId).get();
        if(Objects.isNull(validation))  throw new Exception();

        logger.debug("[{}]\t[getById] - objectId: {}", ValidationDAOImpl.class.getSimpleName(), objectId);
        return validation;
    }

    @Override
    public Key<Validation> insert(Validation validation) throws Exception {
        if(Objects.isNull(validation))  throw new Exception();
        Key<Validation> result = save(validation);
        if(Objects.isNull(result))    throw new Exception();

        logger.debug("[{}]\t[insert] - validation: {}", ValidationDAOImpl.class.getSimpleName(), validation.toString());
        return result;
    }

    @Override
    public WriteResult deleteById(String objectId) throws Exception {
        if(Objects.isNull(objectId))    throw new Exception();
        WriteResult writeResult = delete(getById(objectId));
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteById] - objectId: {}", ValidationDAOImpl.class.getSimpleName(), objectId);
        return writeResult;
    }
}
