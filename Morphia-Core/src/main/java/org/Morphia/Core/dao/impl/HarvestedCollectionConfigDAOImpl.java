/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.List;

import org.Morphia.Core.dao.HarvestedCollectionConfigDAO;
import org.Morphia.Core.entities.HarvestedCollectionConfig;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * @author amartinez
 *
 */
public class HarvestedCollectionConfigDAOImpl extends BasicDAO<HarvestedCollectionConfig, ObjectId> implements HarvestedCollectionConfigDAO{

	public HarvestedCollectionConfigDAOImpl(Class<HarvestedCollectionConfig> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

	@Override
	public List<HarvestedCollectionConfig> findAll() {
		return createQuery().asList();
	}

	@Override
	public HarvestedCollectionConfig findById(String id) {
		return createQuery().field("id").equal(id).get();
	}

}
