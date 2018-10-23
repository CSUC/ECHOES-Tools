package org.csuc.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.ParserErrorDAO;
import org.csuc.entities.Parser;
import org.csuc.entities.ParserError;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * @author amartinez
 */
public class ParserErrorDAOImpl extends BasicDAO<ParserError, ObjectId> implements ParserErrorDAO {

    private static Logger logger = LogManager.getLogger(ParserErrorDAOImpl.class);

    public ParserErrorDAOImpl(Class<ParserError> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public ParserError getByReference(Parser parser) throws Exception {
        logger.info("[{}]\t[getByReference] - parser: {}", ParserErrorDAOImpl.class.getSimpleName(), parser.toString());
        return find(createQuery().field("parser-id").equal(parser)).get();
    }

    @Override
    public ParserError getByReference(String objectId) throws Exception {
        logger.debug("[{}]\t[getByReference] - objectId: {}", ParserErrorDAOImpl.class.getSimpleName(), objectId);
        return find(createQuery().filter("parser-id", ds.createQuery(Parser.class).filter("_id", objectId).getKey())).get();
    }

}
