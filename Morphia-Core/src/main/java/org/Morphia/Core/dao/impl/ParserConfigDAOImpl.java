/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.List;

import org.Morphia.Core.dao.ParserConfigDAO;
import org.Morphia.Core.entities.ParserConfig;
import org.Morphia.Core.entities.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.DBRef;

/**
 * @author amartinez
 *
 */
public class ParserConfigDAOImpl extends BasicDAO<ParserConfig, ObjectId> implements ParserConfigDAO {

	public ParserConfigDAOImpl(Class<ParserConfig> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

	@Override
	public List<ParserConfig> findAll() {
		return createQuery().asList();
	}

	@Override
	public ParserConfig findById(String id) {
		return createQuery().field("id").equal(id).get();
	}

	@Override
	public List<ParserConfig> findByUser(User user) {	
		return createQuery()
				.field("user_id")
				.equal(new DBRef(User.class.getAnnotation(Entity.class).value(), user.getId()))
				.asList();	
	}

	@Override
	public List<ParserConfig> findByUser(String user_id) {
		return createQuery().field("user_id").equal(new DBRef("user", user_id)).asList();	
	}

}
