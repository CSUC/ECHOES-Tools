package org.csuc.dao.impl;

import com.mongodb.AggregationOptions;
import com.mongodb.WriteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.RecollectDAO;
import org.csuc.entities.Recollect;
import org.csuc.utils.Aggregation;
import org.csuc.utils.Status;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.mongodb.morphia.aggregation.Group.grouping;

/**
 * @author amartinez
 */
public class RecollectDAOImpl extends BasicDAO<Recollect, ObjectId> implements RecollectDAO {

    private static Logger logger = LogManager.getLogger(RecollectDAOImpl.class);

    public RecollectDAOImpl(Class<Recollect> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public Recollect getById(String objectId) throws Exception {
        Recollect recollect = createQuery().field("_id").equal(objectId).get();
        if(Objects.isNull(recollect))  throw new Exception();

        logger.debug("[{}]\t[getById] - objectId: {}", RecollectDAOImpl.class.getSimpleName(), objectId);
        return recollect;
    }

    @Override
    public List<Recollect> getByUser(String user, String orderby) throws Exception {
        if(Objects.isNull(user))  throw new Exception();

        logger.debug("[{}]\t[getByUser] - user: {}    options:[order: {}]", RecollectDAOImpl.class.getSimpleName(), user, orderby);
        return (Objects.nonNull(orderby))
                ? find(createQuery().field("user").equal(user).order(orderby)).asList()
                : find(createQuery().field("user").equal(user)).asList();
    }

    @Override
    public List<Recollect> getByUser(String user, int offset, int limit, String orderby) throws Exception {
        if(Objects.isNull(user))  throw new Exception();

        logger.debug("[{}]\t[getByUser] - user: {}    options[offset: {}\tlimit: {}\torder: {}]", RecollectDAOImpl.class.getSimpleName(), user, offset, limit, orderby);
        return (Objects.nonNull(orderby))
                ? find(createQuery().field("user").equal(user).order(orderby)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit))
                : find(createQuery().field("user").equal(user)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public long countByUser(String user) throws Exception {
        if(Objects.isNull(user))  throw new Exception();

        logger.debug("[{}]\t[countByUser] - {}", RecollectDAOImpl.class.getSimpleName(), user);
        return find(createQuery().field("user").equal(user)).count();
    }

    @Override
    public List<Recollect> getByStatus(Status status) throws Exception {
        if(Objects.isNull(status))  throw new Exception();

        logger.debug("[{}]\t[getByStatus] - status: {}", RecollectDAOImpl.class.getSimpleName(), status);
        return find(createQuery().field("status").equal(status)).asList();
    }

    @Override
    public List<Recollect> getByStatus(Status status, String user) throws Exception {
        if(Objects.isNull(status) || Objects.isNull(user))  throw new Exception();

        Query<Recollect> query = createQuery();

        query.and(
                query.criteria("status").equal(status),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[getByStatus] - status: {}\tuser: {}", RecollectDAOImpl.class.getSimpleName(), status, user);
        return find(query).asList();
    }

    @Override
    public List<Recollect> getByStatus(Status status, int offset, int limit) throws Exception {
        if(Objects.isNull(status))  throw new Exception();

        logger.debug("[{}]\t[getByStatus] - status: {}    options[offset: {}\tlimit: {}]", RecollectDAOImpl.class.getSimpleName(), status, offset, limit);
        return find(createQuery().field("status").equal(status)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public List<Recollect> getByStatus(Status status, String user, int offset, int limit) throws Exception {
        if(Objects.isNull(status) || Objects.isNull(user))  throw new Exception();

        Query<Recollect> query = createQuery();

        query.and(
                query.criteria("status").equal(status),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[getByStatus] - status: {}\tuser: {}\toptions[offset: {}\tlimit: {}]", RecollectDAOImpl.class.getSimpleName(), status, user, offset, limit);
        return find(query).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public long countByStatus(Status status) throws Exception {
        if(Objects.isNull(status))  throw new Exception();

        logger.debug("[{}]\t[countByStatus] - status: {}", RecollectDAOImpl.class.getSimpleName(), status);
        return find(createQuery().field("status").equal(status)).count();
    }

    @Override
    public long countByStatus(Status status, String user) throws Exception {
        if(Objects.isNull(status) || Objects.isNull(user))  throw new Exception();

        Query<Recollect> query = createQuery();

        query.and(
                query.criteria("status").equal(status),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[countByStatus] - status: {}\tuser: {}", RecollectDAOImpl.class.getSimpleName(), status, user);
        return find(query).count();
    }

    @Override
    public Iterator<Aggregation> getStatusAggregation() {
        AggregationOptions options = AggregationOptions.builder().build();

        Iterator<Aggregation> aggregate = getDatastore().createAggregation(Recollect.class)
                .group("status",
                        grouping("total", Accumulator.accumulator("$sum", 1))
                )
                .aggregate(Aggregation.class, AggregationOptions.builder().allowDiskUse(true).batchSize(50).build());

        logger.debug("[{}]\t[getStatusAggregation]", RecollectDAOImpl.class.getSimpleName());
        return aggregate;
    }

    @Override
    public Iterator<Aggregation> getStatusAggregation(String user) {
        AggregationOptions options = AggregationOptions.builder().build();

        Iterator<Aggregation> aggregate = getDatastore().createAggregation(Recollect.class)
                .match(createQuery().field("user").equal(user))
                .group("status",
                        grouping("total", Accumulator.accumulator("$sum", 1))
                )
                .aggregate(Aggregation.class, AggregationOptions.builder().allowDiskUse(true).batchSize(50).build());

        logger.debug("[{}]\t[getStatusAggregation] - user: {}", RecollectDAOImpl.class.getSimpleName(), user);
        return aggregate;
    }

    @Override
    public Key<Recollect> insert(Recollect recollect) throws Exception {
        if(Objects.isNull(recollect))  throw new Exception();
        Key<Recollect> result = save(recollect);
        if(Objects.isNull(result))    throw new Exception();

        logger.debug("[{}]\t[insert] - recollect: {}", RecollectDAOImpl.class.getSimpleName(), recollect.toString());
        return result;
    }

    @Override
    public WriteResult deleteById(String objectId) throws Exception {
        if(Objects.isNull(objectId))    throw new Exception();
        Recollect recollect = getById(objectId);

        if(Objects.nonNull(recollect.getLink()))    getDatastore().delete(recollect.getLink());
        if(Objects.nonNull(recollect.getError()))    getDatastore().delete(recollect.getError());

        WriteResult writeResult = delete(recollect);
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteById] - objectId: {}", RecollectDAOImpl.class.getSimpleName(), objectId);
        return writeResult;
    }

    @Override
    public WriteResult deleteByUser(String user) throws Exception {
        if(Objects.isNull(user))    throw new Exception();
        WriteResult writeResult = getDatastore().delete(createQuery().field("user").equal(user));
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteByUser] - user: {}", RecollectDAOImpl.class.getSimpleName(), user);
        return writeResult;
    }

    @Override
    public WriteResult deleteByStatus(Status status) throws Exception {
        if(Objects.isNull(status))    throw new Exception();
        WriteResult writeResult = getDatastore().delete(createQuery().field("status").equal(status));
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteByStatus] - status: {}", RecollectDAOImpl.class.getSimpleName(), status);
        return writeResult;
    }
}
