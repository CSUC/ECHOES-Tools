package org.csuc.dao;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.csuc.dao.entity.Aggregation;
import org.csuc.dao.entity.Quality;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;

import java.util.Iterator;
import java.util.List;

/**
 * @author amartinez
 */
public interface QualityDAO extends DAO<Quality, ObjectId> {

    /*****************************************************id*****************************************************/
    Quality getById(String objectId) throws Exception;

    List<Quality> getByUser(String user, String orderby) throws Exception;

    List<Quality> getByUser(String user, int offset, int limit, String orderby) throws Exception;

    long countByUser(String user) throws Exception;

    /*****************************************************insert*****************************************************/
    Key<Quality> insert(Quality quality) throws Exception;

    Iterator<Aggregation> getStatusAggregation();

    Iterator<Aggregation> getStatusAggregation(String user);

    /*****************************************************delete*****************************************************/
    WriteResult deleteById(String objectId) throws Exception;
}
