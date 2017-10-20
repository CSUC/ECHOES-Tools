/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.List;

import org.Morphia.Core.dao.HarvestedItemsDAO;
import org.Morphia.Core.entities.HarvestedCollectionConfig;
import org.Morphia.Core.entities.HarvestedItems;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.DBRef;

/**
 * @author amartinez
 *
 */
public class HarvestedItemsDAOImpl extends BasicDAO<HarvestedItems, ObjectId> implements HarvestedItemsDAO{

	public HarvestedItemsDAOImpl(Class<HarvestedItems> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

	@Override
	public List<HarvestedItems> findAll() {
		return createQuery().asList();
	}

	@Override
	public HarvestedItems findById(String id) {
		return createQuery().field("id").equal(id).get();
	}

	@Override
	public List<HarvestedItems> findAll(HarvestedCollectionConfig colConfig) {
		return createQuery()
				.field("harvested_collection_config_id")
				.equal(new DBRef(HarvestedCollectionConfig.class.getAnnotation(Entity.class).value(), colConfig.getId())).asList();	
	}

	@Override
	public List<HarvestedItems> findAll(String colConfig_id) {
		return createQuery()
				.field("harvested_collection_config_id")
				.equal(new DBRef(HarvestedCollectionConfig.class.getAnnotation(Entity.class).value(), colConfig_id)).asList();	
	}
}
