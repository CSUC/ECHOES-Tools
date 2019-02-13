package org.csuc.dao.validation;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.csuc.entities.validation.ValidationDetails;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;

/**
 * @author amartinez
 */
public interface ValidationDetilsDAO extends DAO<ValidationDetails, ObjectId> {

    /*****************************************************id*****************************************************/
    ValidationDetails getById(String objectId) throws Exception;


    /*****************************************************insert*****************************************************/
    Key<ValidationDetails> insert(ValidationDetails validationDetails) throws Exception;


    /*****************************************************delete*****************************************************/
    WriteResult deleteById(String objectId) throws Exception;

}
