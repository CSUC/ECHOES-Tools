/**
 * 
 */
package org.Morphia.Core.dao.impl;

import org.Morphia.Core.dao.TokenDAO;
import org.Morphia.Core.entities.UserToken;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

/**
 * @author amartinez
 *
 */
public class TokenDAOImpl extends BasicDAO<UserToken, ObjectId> implements TokenDAO {

	public TokenDAOImpl(Class<UserToken> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

	@Override
	public UserToken findById(String id) {
		return createQuery().field("id").equal(id).get();
	}

	@Override
	public UserToken findByToken(String token) {
		return createQuery().field("access_token").equal(token).get();	
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserToken findByToken(String token, String type) {
		return ((Query<UserToken>) createQuery().and(
				createQuery().criteria("access_token").equal(token),
				createQuery().criteria("token_type").equal(type))).get();
		
	}

}
