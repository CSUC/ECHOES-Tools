package org.csuc.dao;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.csuc.entities.Analyse;
import org.csuc.utils.Aggregation;
import org.csuc.utils.Status;
import org.csuc.utils.parser.ParserFormat;
import org.csuc.utils.parser.ParserMethod;
import org.csuc.utils.parser.ParserType;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;

import java.util.Iterator;
import java.util.List;

/**
 * @author amartinez
 */
public interface AnalyseDAO extends DAO<Analyse, ObjectId> {

    /*****************************************************id*****************************************************/
    Analyse getById(String objectId) throws Exception;

    /*****************************************************user*****************************************************/
    List<Analyse> getByUser(String user, String orderby) throws Exception;
    List<Analyse> getByUser(String user, int offset, int limit, String orderby) throws Exception;
    long countByUser(String user) throws Exception;

    /*****************************************************method*****************************************************/
    List<Analyse> getByMethod(ParserMethod method) throws Exception;
    List<Analyse> getByMethod(ParserMethod method, String user) throws Exception;
    List<Analyse> getByMethod(ParserMethod method, int offset, int limit) throws Exception;
    List<Analyse> getByMethod(ParserMethod method, String user, int offset, int limit) throws Exception;
    long countByMethod(ParserMethod method) throws Exception;
    long countByMethod(ParserMethod method, String user) throws Exception;

    /*****************************************************type*****************************************************/
    List<Analyse> getByType(ParserType type) throws Exception;
    List<Analyse> getByType(ParserType type, String user) throws Exception;
    List<Analyse> getByType(ParserType type, int offset, int limit) throws Exception;
    List<Analyse> getByType(ParserType type, String user, int offset, int limit) throws Exception;
    long countByType(ParserType type) throws Exception;
    long countByType(ParserType type, String user) throws Exception;

    /*****************************************************format*****************************************************/
    List<Analyse> getByFormat(ParserFormat format) throws Exception;
    List<Analyse> getByFormat(ParserFormat format, String user) throws Exception;
    List<Analyse> getByFormat(ParserFormat format, int offset, int limit) throws Exception;
    List<Analyse> getByFormat(ParserFormat format, String user, int offset, int limit) throws Exception;
    long countByFormat(ParserFormat format) throws Exception;
    long countByFormat(ParserFormat format, String user) throws Exception;

    /*****************************************************status*****************************************************/
    List<Analyse> getByStatus(Status status) throws Exception;
    List<Analyse> getByStatus(Status status, String user) throws Exception;
    List<Analyse> getByStatus(Status status, int offset, int limit) throws Exception;
    List<Analyse> getByStatus(Status status, String user, int offset, int limit) throws Exception;
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
    Key<Analyse> insert(Analyse analyse) throws Exception;

    /*****************************************************delete*****************************************************/
    WriteResult deleteById(String objectId) throws Exception;
    WriteResult deleteByUser(String user) throws Exception;
    WriteResult deleteByMethod(ParserMethod method) throws Exception;
    WriteResult deleteByType(ParserType type) throws Exception;
    WriteResult deleteByStatus(Status status) throws Exception;

}
