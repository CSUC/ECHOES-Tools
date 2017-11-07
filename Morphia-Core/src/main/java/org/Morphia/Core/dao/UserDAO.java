/**
 * 
 */
package org.Morphia.Core.dao;

import java.util.List;

import org.Morphia.Core.entities.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;


/**
 * @author amartinez
 *
 */
public interface UserDAO extends DAO<User, ObjectId>{

	public List<User> findAll();	
	public User findById(String id);
	
	public User findByUUID(String uuid);
	
	public User findByToken(String token);
	
	public User insertNewUser(User user);
	
	public User update(User user);
	
}
