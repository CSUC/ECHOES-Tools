/**
 *
 */
package org.csuc.client;

import com.mongodb.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.utils.LocalDateTimeConverter;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author amartinez
 */
public class Client {

    private static Logger logger = LogManager.getLogger(Client.class);


    private Datastore datastore = null;
    private Morphia morphia = null;
    private String database = null;
    private String host = "localhost";
    private int port = 27017;
    private WriteConcern concern = WriteConcern.JOURNALED;

    public Client(String host, int port, String Database) {
        try {
            this.database = Database;
            this.host = host;

            if (Objects.nonNull(port)) this.port = port;

            List<ServerAddress> addrs = new ArrayList<>();
            addrs.add(new ServerAddress(this.host, this.port));

            MongoClient mongo = new MongoClient(addrs,
                    MongoClientOptions.builder()
                            .retryWrites(true)
                            .socketKeepAlive(true)
                            .connectionsPerHost(100)
                            .threadsAllowedToBlockForConnectionMultiplier(100)
                            .maxConnectionIdleTime(60000)
                            .socketTimeout(60000)
                            .requiredReplicaSetName("rs0")
                            .build());

            ReplicaSetStatus status = mongo.getReplicaSetStatus();

            this.morphia = new Morphia();
            this.morphia.getMapper().getOptions().setStoreNulls(true);
            this.morphia.getMapper().getOptions().setStoreEmpties(true);
            this.morphia.getMapper().getConverters().addConverter(new LocalDateTimeConverter());

            this.datastore = morphia.createDatastore(mongo, this.database);

            logger.debug("WriteConcern   :       {}", mongo.getWriteConcern().toString());
            logger.debug("Status         :       {}", status);
            logger.debug("Read prefrence :       {}", datastore.getMongo().getReadPreference());
            this.datastore.ensureIndexes(); // creates all defined with @Indexed
            this.datastore.ensureCaps();
        } catch (MongoSocketOpenException e) {
            logger.error(e);
        }
    }

    public Client(String host, int port, String Database, WriteConcern concern) {
        try {
            this.database = Database;
            this.host = host;

            if (Objects.nonNull(port)) this.port = port;

            List<ServerAddress> addrs = new ArrayList<>();
            addrs.add(new ServerAddress(this.host, this.port));

            MongoClient mongo = new MongoClient(addrs,
                    MongoClientOptions.builder()
                            .readPreference(ReadPreference.secondaryPreferred())
                            .writeConcern(concern)
                            .retryWrites(true)
                            .socketKeepAlive(true)
                            .connectionsPerHost(100)
                            .threadsAllowedToBlockForConnectionMultiplier(100)
                            .maxConnectionIdleTime(60000)
                            .socketTimeout(60000)
                            .requiredReplicaSetName("rs0")
                            .build());

            ReplicaSetStatus status = mongo.getReplicaSetStatus();

            this.morphia = new Morphia();
            this.morphia.getMapper().getOptions().setStoreNulls(true);
            this.morphia.getMapper().getOptions().setStoreEmpties(true);
            this.morphia.getMapper().getConverters().addConverter(new LocalDateTimeConverter());

            this.datastore = morphia.createDatastore(mongo, this.database);

            logger.debug("WriteConcern   :       {}", mongo.getWriteConcern().toString());
            logger.debug("Status         :       {}", status);
            logger.debug("Read prefrence :       {}", datastore.getMongo().getReadPreference());

            this.datastore.setDefaultWriteConcern(concern);
            this.datastore.ensureIndexes(); // creates all defined with @Indexed
            this.datastore.ensureCaps();
        } catch (MongoSocketOpenException e) {
            logger.error(e);
        }
    }


    public Client(String host, int port, String Database, MongoClientOptions mongoClientOptions ) {
        try {
            this.database = Database;
            this.host = host;

            if (Objects.nonNull(port)) this.port = port;

            List<ServerAddress> addrs = new ArrayList<>();
            addrs.add(new ServerAddress(this.host, this.port));

            MongoClient mongo = new MongoClient(addrs, mongoClientOptions);

            ReplicaSetStatus status = mongo.getReplicaSetStatus();

            this.morphia = new Morphia();
            this.morphia.getMapper().getOptions().setStoreNulls(true);
            this.morphia.getMapper().getOptions().setStoreEmpties(true);
            this.morphia.getMapper().getConverters().addConverter(new LocalDateTimeConverter());

            this.datastore = morphia.createDatastore(mongo, this.database);

            logger.debug("WriteConcern   :       {}", mongo.getWriteConcern().toString());
            logger.debug("Status         :       {}", status);
            logger.debug("Read prefrence :       {}", datastore.getMongo().getReadPreference());
            this.datastore.ensureIndexes(); // creates all defined with @Indexed
            this.datastore.ensureCaps();
        } catch (MongoSocketOpenException e) {
            logger.error(e);
        }
    }

    public Client(String host, int port, String Database, WriteConcern concern, MongoClientOptions mongoClientOptions) {
        try {
            this.database = Database;
            this.host = host;

            if (Objects.nonNull(port)) this.port = port;

            List<ServerAddress> addrs = new ArrayList<>();
            addrs.add(new ServerAddress(this.host, this.port));

            MongoClient mongo = new MongoClient(addrs, mongoClientOptions);

            ReplicaSetStatus status = mongo.getReplicaSetStatus();

            this.morphia = new Morphia();
            this.morphia.getMapper().getOptions().setStoreNulls(true);
            this.morphia.getMapper().getOptions().setStoreEmpties(true);
            this.morphia.getMapper().getConverters().addConverter(new LocalDateTimeConverter());

            this.datastore = morphia.createDatastore(mongo, this.database);

            logger.debug("WriteConcern   :       {}", mongo.getWriteConcern().toString());
            logger.debug("Status         :       {}", status);
            logger.debug("Read prefrence :       {}", datastore.getMongo().getReadPreference());

            this.datastore.setDefaultWriteConcern(concern);
            this.datastore.ensureIndexes(); // creates all defined with @Indexed
            this.datastore.ensureCaps();
        } catch (MongoSocketOpenException e) {
            logger.error(e);
        }
    }

    public Datastore getDatastore() {
        return datastore;
    }

}
