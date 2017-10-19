/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.List;

import org.Morphia.Core.dao.HarvestedItemsDAO;
import org.Morphia.Core.entities.HarvestedItems;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

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

}
