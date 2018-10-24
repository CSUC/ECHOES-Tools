package org.csuc.dao;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.csuc.entities.Parser;
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
public interface ParserDAO extends DAO<Parser, ObjectId> {

    /*****************************************************id*****************************************************/
    Parser getById(String objectId) throws Exception;

    /*****************************************************user*****************************************************/
    List<Parser> getByUser(String user, String orderby) throws Exception;
    List<Parser> getByUser(String user, int offset, int limit, String orderby) throws Exception;
    long countByUser(String user) throws Exception;

    /*****************************************************method*****************************************************/
    List<Parser> getByMethod(ParserMethod method) throws Exception;
    List<Parser> getByMethod(ParserMethod method, String user) throws Exception;
    List<Parser> getByMethod(ParserMethod method, int offset, int limit) throws Exception;
    List<Parser> getByMethod(ParserMethod method, String user, int offset, int limit) throws Exception;
    long countByMethod(ParserMethod method) throws Exception;
    long countByMethod(ParserMethod method, String user) throws Exception;

    /*****************************************************type*****************************************************/
    List<Parser> getByType(ParserType type) throws Exception;
    List<Parser> getByType(ParserType type, String user) throws Exception;
    List<Parser> getByType(ParserType type, int offset, int limit) throws Exception;
    List<Parser> getByType(ParserType type, String user, int offset, int limit) throws Exception;
    long countByType(ParserType type) throws Exception;
    long countByType(ParserType type, String user) throws Exception;

    /*****************************************************format*****************************************************/
    List<Parser> getByFormat(ParserFormat format) throws Exception;
    List<Parser> getByFormat(ParserFormat format, String user) throws Exception;
    List<Parser> getByFormat(ParserFormat format, int offset, int limit) throws Exception;
    List<Parser> getByFormat(ParserFormat format, String user, int offset, int limit) throws Exception;
    long countByFormat(ParserFormat format) throws Exception;
    long countByFormat(ParserFormat format, String user) throws Exception;

    /*****************************************************status*****************************************************/
    List<Parser> getByStatus(Status status) throws Exception;
    List<Parser> getByStatus(Status status, String user) throws Exception;
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
