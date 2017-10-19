/**
 * 
 */
package org.Morphia.Core.dao;

import java.util.List;

import org.Morphia.Core.entities.HarvestedCollectionConfig;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

/**
 * @author amartinez
 *
 */
public interface HarvestedCollectionConfigDAO extends DAO<HarvestedCollectionConfig, ObjectId>{
	
	public List<HarvestedCollectionConfig> findAll();
	
	public HarvestedCollectionConfig findById(String id);
	
}
