package org.Morphia.Core.dao;

import com.mongodb.WriteResult;

import org.Morphia.Core.entities.Recollect;
import org.Morphia.Core.utils.Status;
import org.Morphia.Core.utils.Aggregation;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;
import org.mongodb.morphia.query.QueryResults;

import java.util.Iterator;
import java.util.List;

/**
 * @author amartinez
 */
public interface RecollectDAO extends DAO<Recollect, ObjectId> {

    /*****************************************************id*****************************************************/
    Recollect getById(String objectId) throws Exception;

    /*****************************************************user*****************************************************/
    QueryResults<Recollect> getByUser(String user, String orderby) throws Exception;
    List<Recollect> getByUser(String user, int offset, int limit, String orderby) throws Exception;
    long countByUser(String user) throws Exception;

    /*****************************************************status*****************************************************/
    QueryResults<Recollect> getByStatus(Status status) throws Exception;
    QueryResults<Recollect> getByStatus(Status status, String user) throws Exception;
    List<Recollect> getByStatus(Status status, int offset, int limit) throws Exception;
    List<Recollect> getByStatus(Status status, String user, int offset, int limit) throws Exception;
    long countByStatus(Status status) throws Exception;
    long countByStatus(Status status, String user) throws Exception;

    Iterator<Aggregation> getStatusAggregation();
    Iterator<Aggregation> getStatusAggregation(String user);

    /*****************************************************insert*****************************************************/
    Key<Recollect> insert(Recollect recollect) throws Exception;

    /*****************************************************delete*****************************************************/
    WriteResult deleteById(String objectId) throws Exception;
    WriteResult deleteByUser(String user) throws Exception;
    WriteResult deleteByStatus(Status status) throws Exception;
}
