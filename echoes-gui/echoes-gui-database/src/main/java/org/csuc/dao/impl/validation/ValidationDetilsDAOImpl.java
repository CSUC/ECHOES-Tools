package org.csuc.dao.impl.validation;

import com.mongodb.WriteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.validation.ValidationDetilsDAO;
import org.csuc.entities.validation.ValidationDetails;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.Objects;


/**
 * @author amartinez
 */
public class ValidationDetilsDAOImpl extends BasicDAO<ValidationDetails, ObjectId> implements ValidationDetilsDAO {

    private static Logger logger = LogManager.getLogger(ValidationDetilsDAOImpl.class);

    public ValidationDetilsDAOImpl(Class<ValidationDetails> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public ValidationDetails getById(String objectId) throws Exception {
        ValidationDetails validationDetailsDAO = createQuery().field("_id").equal(objectId).get();
        if(Objects.isNull(validationDetailsDAO))  throw new Exception();

        logger.debug("[{}]\t[getById] - objectId: {}", ValidationDetilsDAOImpl.class.getSimpleName(), objectId);
        return validationDetailsDAO;
    }

    @Override
    public Key<ValidationDetails> insert(ValidationDetails validationDetails) throws Exception {
        if(Objects.isNull(validationDetails))  throw new Exception();
        Key<ValidationDetails> result = save(validationDetails);
        if(Objects.isNull(result))    throw new Exception();

        logger.debug("[{}]\t[insert] - validation: {}", ValidationDetilsDAOImpl.class.getSimpleName(), validationDetails.toString());
        return result;
    }

    @Override
    public WriteResult deleteById(String objectId) throws Exception {
        if(Objects.isNull(objectId))    throw new Exception();
        WriteResult writeResult = delete(getById(objectId));
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteById] - objectId: {}", ValidationDetilsDAOImpl.class.getSimpleName(), objectId);
        return writeResult;
    }
}
