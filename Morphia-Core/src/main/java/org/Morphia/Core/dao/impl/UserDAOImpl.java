/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.List;

import org.Morphia.Core.dao.UserDAO;
import org.Morphia.Core.entities.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * @author amartinez
 *
 */
public class UserDAOImpl extends BasicDAO<User, ObjectId> implements UserDAO {

	public UserDAOImpl(Class<User> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

	@Override
	public List<User> findAll() {
		return createQuery().asList();
	}

	@Override
	public User findById(String id) {
		return createQuery().field("id").equal(id).get();
	}

	@Override
	public User findByUUID(String uuid) {
		return createQuery().field("uuid").equal(uuid).get();
	}

	@Override
	public User insertNewUser(User user) {
		save(user);
		return user;
	}
	
	@Override
	public User update(User user) {
		save(user);
		return user;
	}

	@Override
	public User findByToken(String token) {
		return createQuery().field("token").equal(token).get();
	}

}
