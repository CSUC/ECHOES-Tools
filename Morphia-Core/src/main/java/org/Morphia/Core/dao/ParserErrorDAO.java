package org.Morphia.Core.dao;

import org.Morphia.Core.entities.Parser;
import org.Morphia.Core.entities.ParserError;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

/**
 * @author amartinez
 */
public interface ParserErrorDAO extends DAO<ParserError, ObjectId> {

    ParserError getByReference(Parser parser) throws Exception;

    ParserError getByReference(String objectId) throws Exception;
}
