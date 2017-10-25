/**
 * 
 */
package org.Morphia.Core.dao;

import java.util.List;

import org.Morphia.Core.entities.ParserConfig;
import org.Morphia.Core.entities.ParserResults;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.DAO;

/**
 * @author amartinez
 *
 */
public interface ParserResultsDAO extends DAO<ParserResults, ObjectId>{

	public List<ParserResults> findAll();
	public List<ParserResults> findAll(ParserConfig parserConfig);
	public List<ParserResults> findAll(String parserConfig_uuid);
	
	public ParserResults findById(String id);
	
}
