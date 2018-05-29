package org.Morphia.Core.dao;

import com.mongodb.WriteResult;
import org.Morphia.Core.entities.Parser;
import org.Morphia.Core.utils.Status;
import org.Morphia.Core.utils.Aggregation;
import org.Morphia.Core.utils.parser.ParserFormat;
import org.Morphia.Core.utils.parser.ParserMethod;
import org.Morphia.Core.utils.parser.ParserType;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;
import org.mongodb.morphia.query.QueryResults;

import java.util.Iterator;
import java.util.List;

/**
 * @author amartinez
 */
public interface ParserDAO extends DAO<Parser, ObjectId> {

    /*****************************************************id*****************************************************/
    Parser getById(String objectId) throws Exception;

    /*****************************************************user*****************************************************/
    QueryResults<Parser> getByUser(String user, String orderby) throws Exception;
    List<Parser> getByUser(String user, int offset, int limit, String orderby) throws Exception;
    long countByUser(String user) throws Exception;

    /*****************************************************method*****************************************************/
    QueryResults<Parser> getByMethod(ParserMethod method) throws Exception;
    QueryResults<Parser> getByMethod(ParserMethod method, String user) throws Exception;
    List<Parser> getByMethod(ParserMethod method, int offset, int limit) throws Exception;
    List<Parser> getByMethod(ParserMethod method, String user, int offset, int limit) throws Exception;
    long countByMethod(ParserMethod method) throws Exception;
    long countByMethod(ParserMethod method, String user) throws Exception;

    /*****************************************************type*****************************************************/
    QueryResults<Parser> getByType(ParserType type) throws Exception;
    QueryResults<Parser> getByType(ParserType type, String user) throws Exception;
    List<Parser> getByType(ParserType type, int offset, int limit) throws Exception;
    List<Parser> getByType(ParserType type, String user, int offset, int limit) throws Exception;
    long countByType(ParserType type) throws Exception;
    long countByType(ParserType type, String user) throws Exception;

    /*****************************************************format*****************************************************/
    QueryResults<Parser> getByFormat(ParserFormat format) throws Exception;
    QueryResults<Parser> getByFormat(ParserFormat format, String user) throws Exception;
    List<Parser> getByFormat(ParserFormat format, int offset, int limit) throws Exception;
    List<Parser> getByFormat(ParserFormat format, String user, int offset, int limit) throws Exception;
    long countByFormat(ParserFormat format) throws Exception;
    long countByFormat(ParserFormat format, String user) throws Exception;

    /*****************************************************status*****************************************************/
    QueryResults<Parser> getByStatus(Status status) throws Exception;
    QueryResults<Parser> getByStatus(Status status, String user) throws Exception;
    List<Parser> getByStatus(Status status, int offset, int limit) throws Exception;
    List<Parser> getByStatus(Status status, String user, int offset, int limit) throws Exception;
    long countByStatus(Status status) throws Exception;
    long countByStatus(Status status, String user) throws Exception;

    Iterator<Aggregation> getStatusAggregation();
    Iterator<Aggregation> getStatusAggregation(String user);

    /*****************************************************insert*****************************************************/
    Key<Parser> insert(Parser parser) throws Exception;

    /*****************************************************delete*****************************************************/
    WriteResult deleteById(String objectId) throws Exception;
    WriteResult deleteByUser(String user) throws Exception;
    WriteResult deleteByMethod(ParserMethod method) throws Exception;
    WriteResult deleteByType(ParserType type) throws Exception;
    WriteResult deleteByStatus(Status status) throws Exception;

}
