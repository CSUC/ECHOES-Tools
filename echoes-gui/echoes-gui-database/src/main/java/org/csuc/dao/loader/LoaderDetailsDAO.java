package org.csuc.dao.loader;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.csuc.entities.loader.LoaderDetails;
import org.csuc.entities.quality.QualityDetails;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;

import java.util.List;

/**
 * @author amartinez
 */
public interface LoaderDetailsDAO extends DAO<LoaderDetails, ObjectId> {

    /*****************************************************id*****************************************************/
    LoaderDetails getById(String objectId) throws Exception;


    /*****************************************************insert*****************************************************/
    Key<LoaderDetails> insert(LoaderDetails loaderDetails) throws Exception;


    /*****************************************************delete*****************************************************/
    WriteResult deleteById(String objectId) throws Exception;

    /*****************************************************errors******************************************************/
    List<LoaderDetails> getErrorsById(String objectId, int offset, int limit, String orderby) throws Exception;

    long countErrorsById(String objectId) throws Exception;

}
