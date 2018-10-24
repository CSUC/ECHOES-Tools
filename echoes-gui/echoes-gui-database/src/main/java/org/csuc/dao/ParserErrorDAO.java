package org.csuc.dao;

import org.bson.types.ObjectId;
import org.csuc.entities.Parser;
import org.csuc.entities.ParserError;
import org.mongodb.morphia.dao.DAO;

/**
 * @author amartinez
 */
public interface ParserErrorDAO extends DAO<ParserError, ObjectId> {

    ParserError getByReference(Parser parser) throws Exception;

    ParserError getByReference(String objectId) throws Exception;
}
