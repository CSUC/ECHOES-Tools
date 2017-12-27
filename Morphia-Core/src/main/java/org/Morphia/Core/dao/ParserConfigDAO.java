/**
 * 
 */
package org.Morphia.Core.dao;

import java.util.List;

import org.Morphia.Core.entities.ParserConfig;
import org.Morphia.Core.entities.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

/**
 * @author amartinez
 *
 */
public interface ParserConfigDAO extends DAO<ParserConfig, ObjectId> {

	public List<ParserConfig> findAll();
	
	public List<ParserConfig> findByUser(User user);
	
	public List<ParserConfig> findByUser(String user_id);
	
	public ParserConfig findById(String id);
	
}
