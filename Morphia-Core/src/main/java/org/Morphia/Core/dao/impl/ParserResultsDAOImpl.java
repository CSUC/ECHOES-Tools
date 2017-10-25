/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.List;

import org.Morphia.Core.dao.ParserResultsDAO;
import org.Morphia.Core.entities.ParserConfig;
import org.Morphia.Core.entities.ParserResults;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.DBRef;

/**
 * @author amartinez
 *
 */
public class ParserResultsDAOImpl extends BasicDAO<ParserResults, ObjectId> implements ParserResultsDAO{

	
	public ParserResultsDAOImpl(Class<ParserResults> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

	@Override
	public List<ParserResults> findAll() {
		return createQuery().asList();
	}
	
	@Override
	public List<ParserResults> findAll(ParserConfig parserConfig) {
		return createQuery()
				.field("parser_config_id")
				.equal(new DBRef(ParserConfig.class.getAnnotation(Entity.class).value(), parserConfig.getId()))
				.asList();
	}
	
	@Override
	public List<ParserResults> findAll(String parserConfig_uuid) {
		return createQuery()
				.field("parser_config_id")
				.equal(new DBRef(ParserConfig.class.getAnnotation(Entity.class).value(), parserConfig_uuid))
				.asList();
	}

	@Override
	public ParserResults findById(String id) {
		return createQuery().field("id").equal(id).get();
	}

}
