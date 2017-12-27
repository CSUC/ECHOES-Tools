/**
 * 
 */
package org.Morphia.Core.dao;

import java.util.List;

import org.Morphia.Core.entities.HarvestedCollectionConfig;
import org.Morphia.Core.entities.User;
import org.Morphia.Core.utils.HarvestStatus;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

/**
 * @author amartinez
 *
 */
public interface HarvestedCollectionConfigDAO extends DAO<HarvestedCollectionConfig, ObjectId>{
	
	public List<HarvestedCollectionConfig> findAll();
	public List<HarvestedCollectionConfig> findAll(User user);
	public List<HarvestedCollectionConfig> findAll(String user_id);
	
	public HarvestedCollectionConfig findById(String id);
	
	public List<HarvestedCollectionConfig> findByStatus(HarvestStatus status);
	public List<HarvestedCollectionConfig> findByStatus(HarvestStatus status, User user);
	public List<HarvestedCollectionConfig> findByStatus(HarvestStatus status, String user_id);
	
	public List<HarvestedCollectionConfig> findByXsdConfig(String xsd);
	public List<HarvestedCollectionConfig> findByXsdConfig(String xsd, User user);
	public List<HarvestedCollectionConfig> findByXsdConfig(String xsd, String user_id);
	
	public List<HarvestedCollectionConfig> findByUser(User user);
	public List<HarvestedCollectionConfig> findByUser(String user_id);
	
	
	public HarvestedCollectionConfig insertNewHarvested(HarvestedCollectionConfig h);
}
