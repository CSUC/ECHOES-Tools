package org.csuc.dao.validation;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.csuc.entities.validation.Validation;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;

/**
 * @author amartinez
 */
public interface ValidationDAO extends DAO<Validation, ObjectId> {

    /*****************************************************id*****************************************************/
    Validation getById(String objectId) throws Exception;


    /*****************************************************insert*****************************************************/
    Key<Validation> insert(Validation validation) throws Exception;


    /*****************************************************delete*****************************************************/
    WriteResult deleteById(String objectId) throws Exception;

}
