package org.csuc.dao.impl;

import com.mongodb.AggregationOptions;
import com.mongodb.WriteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.entity.Aggregation;
import org.csuc.dao.entity.Quality;
import org.csuc.dao.quality.QualityDAO;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.FindOptions;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.mongodb.morphia.aggregation.Group.grouping;


/**
 * @author amartinez
 */
public class QualityDAOImpl extends BasicDAO<Quality, ObjectId> implements QualityDAO {

    private static Logger logger = LogManager.getLogger(QualityDAOImpl.class);

    public QualityDAOImpl(Class<Quality> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public Quality getById(String objectId) throws Exception {
        Quality quality = createQuery().field("_id").equal(objectId).get();
        if (Objects.isNull(quality)) throw new Exception(String.format("Quality id not exists '%s'", objectId));

        logger.debug("[{}]\t[getById] - objectId: {}", QualityDAOImpl.class.getSimpleName(), objectId);
        return quality;
    }

    @Override
    public List<Quality> getByUser(String user, String orderby) throws Exception {
        if (Objects.isNull(user)) throw new Exception();

        logger.debug("[{}]\t[getByUser] - user: {}\toption:[order: {}]", QualityDAOImpl.class.getSimpleName(), user, orderby);
        return (Objects.nonNull(orderby))
                ? find(createQuery().field("user").equal(user).order(orderby)).asList()
                : find(createQuery().field("user").equal(user)).asList();
    }

    @Override
    public List<Quality> getByUser(String user, int offset, int limit, String orderby) throws Exception {
        if (Objects.isNull(user)) throw new Exception();

        logger.debug("[{}]\t[getByUser] - user: {}\toption:[offset: {}\tlimit: {}\torder: {}]", QualityDAOImpl.class.getSimpleName(), user, offset, limit, orderby);
        return (Objects.nonNull(orderby))
                ? find(createQuery().field("user").equal(user).order(orderby)).asList(new FindOptions().skip(offset > 0 ? ((offset - 1) * limit) : 0).limit(limit))
                : find(createQuery().field("user").equal(user)).asList(new FindOptions().skip(offset > 0 ? ((offset - 1) * limit) : 0).limit(limit));
    }

    @Override
    public long countByUser(String user) throws Exception {
        if (Objects.isNull(user)) throw new Exception();

        logger.debug("[{}]\t[countByUser] - user: {}", QualityDAOImpl.class.getSimpleName(), user);
        return find(createQuery().field("user").equal(user)).count();
    }

    @Override
    public Key<Quality> insert(Quality quality) throws Exception {
        if (Objects.isNull(quality)) throw new Exception();
        Key<Quality> result = save(quality);
        if (Objects.isNull(result)) throw new Exception();

        logger.debug("[{}]\t[insert] - quality: {}", QualityDAOImpl.class.getSimpleName(), quality.toString());
        return result;
    }

    @Override
    public WriteResult deleteById(String objectId) throws Exception {
        if (Objects.isNull(objectId)) throw new Exception();
        WriteResult writeResult = delete(getById(objectId));
        if (Objects.isNull(writeResult)) throw new Exception();

        logger.debug("[{}]\t[deleteById] - objectId: {}", QualityDAOImpl.class.getSimpleName(), objectId);
        return writeResult;
    }

    @Override
    public Iterator<Aggregation> getStatusAggregation() {
        AggregationOptions options = AggregationOptions.builder().build();

        Iterator<Aggregation> aggregate = getDatastore().createAggregation(Quality.class)
                .group("status",
                        grouping("total", Accumulator.accumulator("$sum", 1))
                )
                .aggregate(Aggregation.class, AggregationOptions.builder().allowDiskUse(true).batchSize(50).build());

        logger.debug("[{}]\t[getStatusAggregation]", QualityDAOImpl.class.getSimpleName());
        return aggregate;
    }

    @Override
    public Iterator<Aggregation> getStatusAggregation(String user) {
        AggregationOptions options = AggregationOptions.builder().build();

        Iterator<Aggregation> aggregate = getDatastore().createAggregation(Quality.class)
                .match(createQuery().field("user").equal(user))
                .group("status",
                        grouping("total", Accumulator.accumulator("$sum", 1))
                )
                .aggregate(Aggregation.class, AggregationOptions.builder().allowDiskUse(true).batchSize(50).build());

        logger.debug("[{}]\t[getStatusAggregation] - user: {}", QualityDAOImpl.class.getSimpleName(), user);
        return aggregate;
    }

}
