/**
 * 
 */
package org.Morphia.Core.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.ReplicaSetStatus;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

/**
 * @author amartinez
 *
 */
public class MorphiaEchoes {

	private static Logger logger = LogManager.getLogger(MorphiaEchoes.class);
	
	private Datastore datastore = null;
	private Morphia morphia = null;
	private String database = null;
	
	private String host = "localhost";
	private int port = 27017;
	
	public MorphiaEchoes(String database) {	
		try {
			this.database = database;
			
		    List<ServerAddress> addrs = new ArrayList<ServerAddress>();
		     addrs.add( new ServerAddress( this.host , this.port ) );
		     
		    MongoClient mongo = new MongoClient(addrs);
		    
		    ReplicaSetStatus status = mongo.getReplicaSetStatus();
		    
		    this.morphia = new Morphia();
		    //morphia.mapPackage("org.Morphia.Entities");
		    
		    morphia.getMapper().getOptions().setStoreNulls(true);
			morphia.getMapper().getOptions().setStoreEmpties(true);
		    
		    this.datastore = morphia.createDatastore(mongo, this.database);
		    
		    datastore.setDefaultWriteConcern(WriteConcern.JOURNALED);
		    
//		    this.datastore.getMongo().setWriteConcern(WriteConcern.REPLICAS_SAFE);
//		    this.datastore.getMongo().setReadPreference(ReadPreference.primary());
		    
		    logger.info(String.format("Status: %s", status));		  
		    logger.info(String.format("Read prefrence %s ", datastore.getMongo().getReadPreference()));
		    
		    this.datastore.ensureIndexes(); //creates all defined with @Indexed
			this.datastore.ensureCaps();
		    
		} catch (MongoSocketOpenException e) {
		   logger.error(e);
		}
		
	}
	
	public MorphiaEchoes(String host, int port, String database) {		
		try {
			this.database = database;
			this.host = host;
			this.port = port;
			
		    List<ServerAddress> addrs = new ArrayList<ServerAddress>();
		     addrs.add( new ServerAddress( this.host , this.port ) );
		     
		    MongoClient mongo = new MongoClient(addrs);
		    
		    ReplicaSetStatus status = mongo.getReplicaSetStatus();
		    
		    this.morphia = new Morphia();
		    //morphia.mapPackage("org.Morphia.Entities");
		    
		    morphia.getMapper().getOptions().setStoreNulls(true);
			morphia.getMapper().getOptions().setStoreEmpties(true);
		    
		    this.datastore = morphia.createDatastore(mongo, this.database);
		    
		    datastore.setDefaultWriteConcern(WriteConcern.JOURNALED);
		    
//		    this.datastore.getMongo().setWriteConcern(WriteConcern.REPLICAS_SAFE);
//		    this.datastore.getMongo().setReadPreference(ReadPreference.primary());
		    
		    logger.info(String.format("Status: %s", status));		  
		    logger.info(String.format("Read prefrence %s ", datastore.getMongo().getReadPreference()));
		    
		    this.datastore.ensureIndexes(); //creates all defined with @Indexed
			this.datastore.ensureCaps();
		    
		} catch (MongoSocketOpenException e) {
		   logger.error(e);
		}
	}
	
	public Datastore getDatastore() {
		return datastore;
	}
	
}
