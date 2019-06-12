package org.csuc.dao;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.csuc.dao.entity.QualityDetails;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;

import java.util.List;

/**
 * @author amartinez
 */
public interface QualityDetailsDAO extends DAO<QualityDetails, ObjectId> {

    /*****************************************************id*****************************************************/
    QualityDetails getById(String objectId) throws Exception;


    /*****************************************************insert*****************************************************/
    Key<QualityDetails> insert(QualityDetails qualityDetails) throws Exception;


    /*****************************************************delete*****************************************************/
    WriteResult deleteById(String objectId) throws Exception;

    /*****************************************************errors******************************************************/
    List<QualityDetails> getErrorsById(String objectId, int offset, int limit, String orderby) throws Exception;

    long countErrorsById(String objectId) throws Exception;

}
