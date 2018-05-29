package org.Morphia.Core.dao.impl;

import org.Morphia.Core.dao.ParserErrorDAO;
import org.Morphia.Core.entities.Parser;
import org.Morphia.Core.entities.ParserError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import static org.mongodb.morphia.aggregation.Group.grouping;


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
        return find(createQuery().field("parser-id").equal(parser)).get();
    }

    @Override
    public ParserError getByReference(String objectId) throws Exception {
        return find(createQuery().filter("parser-id", ds.createQuery(Parser.class).filter("_id", objectId).getKey())).get();
    }

}
