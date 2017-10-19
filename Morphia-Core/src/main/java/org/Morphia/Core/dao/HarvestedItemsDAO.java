/**
 * 
 */
package org.Morphia.Core.dao;

import java.util.List;

import org.Morphia.Core.entities.HarvestedItems;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

/**
 * @author amartinez
 *
 */
public interface HarvestedItemsDAO extends DAO<HarvestedItems, ObjectId>{

	public List<HarvestedItems> findAll();
	
	public HarvestedItems findById(String id);
}
