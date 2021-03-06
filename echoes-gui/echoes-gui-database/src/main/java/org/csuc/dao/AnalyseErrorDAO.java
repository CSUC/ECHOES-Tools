package org.csuc.dao;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.csuc.entities.Analyse;
import org.csuc.entities.AnalyseError;
import org.mongodb.morphia.dao.DAO;

/**
 * @author amartinez
 */
public interface AnalyseErrorDAO extends DAO<AnalyseError, ObjectId> {

    AnalyseError getByReference(Analyse analyse) throws Exception;

    AnalyseError getByReference(String objectId) throws Exception;

    WriteResult deleteByReference(String objectId) throws Exception;
    WriteResult deleteByReference(Analyse analyse) throws Exception;
    WriteResult deleteById(String objectId) throws Exception;

}
