/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.List;

import org.Morphia.Core.utils.HarvestStatus;
import org.Morphia.Core.dao.HarvestedCollectionConfigDAO;
import org.Morphia.Core.entities.HarvestedCollectionConfig;
import org.Morphia.Core.entities.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.mongodb.DBRef;

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

	@Override
	public List<HarvestedCollectionConfig> findAll(User user) {		
		return createQuery()
				.field("user_id")
				.equal(new DBRef(User.class.getAnnotation(Entity.class).value(), user.getId())).asList();	
	}

	@Override
	public List<HarvestedCollectionConfig> findAll(String user_id) {
		return createQuery()
				.field("user_id")
				.equal(new DBRef(User.class.getAnnotation(Entity.class).value(), user_id)).asList();	
	}

	@Override
	public List<HarvestedCollectionConfig> findByStatus(HarvestStatus status) {		
		return createQuery()
				.field("harvest_status")
				.equal(status.getValue()).asList();		
	}

	@Override
	public List<HarvestedCollectionConfig> findByStatus(HarvestStatus status, User user) {
		Query<HarvestedCollectionConfig> query = getDatastore().find(HarvestedCollectionConfig.class);
		query.and(
		  query.criteria("harvest_status").equal(status.getValue()),
		  query.criteria("user_id").equal(new DBRef(User.class.getAnnotation(Entity.class).value(), user.getId())));
		
		return query.asList();
	}

	@Override
	public List<HarvestedCollectionConfig> findByStatus(HarvestStatus status, String user_id) {
		Query<HarvestedCollectionConfig> query = getDatastore().find(HarvestedCollectionConfig.class);
		query.and(
		  query.criteria("harvest_status").equal(status.getValue()),
		  query.criteria("user_id").equal(new DBRef(User.class.getAnnotation(Entity.class).value(), user_id)));
		
		return query.asList();
	}

	@Override
	public List<HarvestedCollectionConfig> findByXsdConfig(String xsd) {
		return createQuery().field("xsd_config").equal(xsd).asList();	
	}

	@Override
	public List<HarvestedCollectionConfig> findByXsdConfig(String xsd, User user) {
		Query<HarvestedCollectionConfig> query = getDatastore().find(HarvestedCollectionConfig.class);
		query.and(
		  query.criteria("xsd_config").equal(xsd),
		  query.criteria("user_id").equal(new DBRef(User.class.getAnnotation(Entity.class).value(), user.getId())));
		
		return query.asList();
	}

	@Override
	public List<HarvestedCollectionConfig> findByXsdConfig(String xsd, String user_id) {
		Query<HarvestedCollectionConfig> query = getDatastore().find(HarvestedCollectionConfig.class);
		query.and(
		  query.criteria("xsd_config").equal(xsd),
		  query.criteria("user_id").equal(new DBRef(User.class.getAnnotation(Entity.class).value(), user_id)));
		
		return query.asList();
	}

	@Override
	public List<HarvestedCollectionConfig> findByUser(User user) {
		return createQuery()
				.field("user_id")
				.equal(new DBRef(User.class.getAnnotation(Entity.class).value(), user.getId())).asList();
	}

	@Override
	public List<HarvestedCollectionConfig> findByUser(String user_id) {
		return createQuery()
				.field("user_id")
				.equal(new DBRef(User.class.getAnnotation(Entity.class).value(), user_id)).asList();
	}

	@Override
	public HarvestedCollectionConfig insertNewHarvested(HarvestedCollectionConfig h) {
		save(h);
		return h;
	}

}
