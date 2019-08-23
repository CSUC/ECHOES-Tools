package org.csuc.dao.loader;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.csuc.entities.loader.Loader;
import org.csuc.utils.Aggregation;
import org.csuc.utils.Status;

import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;

import java.util.Iterator;
import java.util.List;

/**
 * @author amartinez
 */
public interface LoaderDAO extends DAO<Loader, ObjectId> {

    /*****************************************************id*****************************************************/
    Loader getById(String objectId) throws Exception;

    /*****************************************************user*****************************************************/
    List<Loader> getByUser(String user, String orderby) throws Exception;
    List<Loader> getByUser(String user, int offset, int limit, String orderby) throws Exception;
    long countByUser(String user) throws Exception;

    /*****************************************************status*****************************************************/
    List<Loader> getByStatus(Status status) throws Exception;
    List<Loader> getByStatus(Status status, String user) throws Exception;
    List<Loader> getByStatus(Status status, int offset, int limit) throws Exception;
    List<Loader> getByStatus(Status status, String user, int offset, int limit) throws Exception;
    long countByStatus(Status status) throws Exception;
    long countByStatus(Status status, String user) throws Exception;

    long getStatusLastMonth(Status status) throws Exception;
    long getStatusLastMonth(Status status, String user) throws Exception;
    long getStatusMonth(Status status) throws Exception;
    long getStatusMonth(Status status, String user) throws Exception;

    long getStatusLastYear(Status status) throws Exception;
    long getStatusLastYear(Status status, String user) throws Exception;
    long getStatusYear(Status status) throws Exception;
    long getStatusYear(Status status, String user) throws Exception;

    long getStatusLastDay(Status status) throws Exception;
    long getStatusLastDay(Status status, String user) throws Exception;
    long getStatusDay(Status status) throws Exception;
    long getStatusDay(Status status, String user) throws Exception;

    Iterator<Aggregation> getStatusAggregation();
    Iterator<Aggregation> getStatusAggregation(String user);

    /*****************************************************insert*****************************************************/
    Key<Loader> insert(Loader loader) throws Exception;

    /*****************************************************delete*****************************************************/
    WriteResult deleteById(String objectId) throws Exception;
    WriteResult deleteByUser(String user) throws Exception;
    WriteResult deleteByStatus(Status status) throws Exception;
}
