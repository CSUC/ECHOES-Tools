package org.csuc.dao.impl.loader;

import com.mongodb.AggregationOptions;
import com.mongodb.WriteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.loader.LoaderDAO;
import org.csuc.entities.loader.Loader;
import org.csuc.entities.loader.LoaderDetails;
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
public class LoaderDAOImpl extends BasicDAO<Loader, ObjectId> implements LoaderDAO {

    private static Logger logger = LogManager.getLogger(LoaderDAOImpl.class);

    public LoaderDAOImpl(Class<Loader> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public Loader getById(String objectId) throws Exception {
        Loader loader = createQuery().field("_id").equal(objectId).get();
        if(Objects.isNull(loader))  throw new Exception();

        logger.debug("[{}]\t[getById] - objectId: {}", LoaderDAOImpl.class.getSimpleName(), objectId);
        return loader;
    }

    @Override
    public List<Loader> getByUser(String user, String orderby) throws Exception {
        if(Objects.isNull(user))  throw new Exception();

        logger.debug("[{}]\t[getByUser] - user: {}\toption:[order: {}]", LoaderDAOImpl.class.getSimpleName(), user, orderby);
        return (Objects.nonNull(orderby))
                ? find(createQuery().field("user").equal(user).order(orderby)).asList()
                : find(createQuery().field("user").equal(user)).asList();
    }

    @Override
    public List<Loader> getByUser(String user, int offset, int limit, String orderby) throws Exception {
        if(Objects.isNull(user))  throw new Exception();

        logger.debug("[{}]\t[getByUser] - user: {}\toption:[offset: {}\tlimit: {}\torder: {}]", LoaderDAOImpl.class.getSimpleName(), user, offset, limit, orderby);
        return (Objects.nonNull(orderby))
                ? find(createQuery().field("user").equal(user).order(orderby)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit))
                : find(createQuery().field("user").equal(user)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public long countByUser(String user) throws Exception {
        if(Objects.isNull(user))  throw new Exception();

        logger.debug("[{}]\t[countByUser] - user: {}", LoaderDAOImpl.class.getSimpleName(), user);
        return find(createQuery().field("user").equal(user)).count();
    }

    @Override
    public List<Loader> getByStatus(Status status) throws Exception {
        if(Objects.isNull(status))  throw new Exception();

        logger.debug("[{}]\t[getByStatus] - status: {}", LoaderDAOImpl.class.getSimpleName(), status);
        return find(createQuery().field("status").equal(status)).asList();
    }

    @Override
    public List<Loader> getByStatus(Status status, String user) throws Exception {
        if(Objects.isNull(status) || Objects.isNull(user))  throw new Exception();

        Query<Loader> query = createQuery();

        query.and(
                query.criteria("status").equal(status),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[getByStatus] - status: {}\tuser: {}", LoaderDAOImpl.class.getSimpleName(), status, user);
        return find(query).asList();
    }

    @Override
    public List<Loader> getByStatus(Status status, int offset, int limit) throws Exception {
        if(Objects.isNull(status))  throw new Exception();

        //new FindOptions().skip(offset).limit(limit)

        logger.debug("[{}]\t[getByStatus] - status: {}\toptions:[offset: {}\tlimit: {}]", LoaderDAOImpl.class.getSimpleName(), status, offset, limit);
        return find(createQuery().field("status").equal(status)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public List<Loader> getByStatus(Status status, String user, int offset, int limit) throws Exception {
        if(Objects.isNull(status) || Objects.isNull(user))  throw new Exception();

        Query<Loader> query = createQuery();

        query.and(
                query.criteria("status").equal(status),
                query.criteria("user").equal(user)
        );

        //new FindOptions().skip(offset).limit(limit)

        logger.debug("[{}]\t[getByStatus] - status: {}\toptions:[offset: {}\tlimit: {}]", LoaderDAOImpl.class.getSimpleName(), status, offset, limit);
        return find(query).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public long countByStatus(Status status) throws Exception {
        if(Objects.isNull(status))  throw new Exception();
        return find(createQuery().field("status").equal(status)).count();
    }

    @Override
    public long countByStatus(Status status, String user) throws Exception {
        if(Objects.isNull(status) || Objects.isNull(user))  throw new Exception();

        Query<Loader> query = createQuery();

        query.and(
                query.criteria("status").equal(status),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[countByStatus] - status: {}\tuser: {}", LoaderDAOImpl.class.getSimpleName(), status, user);
        return find(query).count();
    }

    @Override
    public long getStatusLastMonth(Status status) throws Exception {
        return 0;
    }

    @Override
    public long getStatusLastMonth(Status status, String user) throws Exception {
        return 0;
    }

    @Override
    public long getStatusMonth(Status status) throws Exception {
        return 0;
    }

    @Override
    public long getStatusMonth(Status status, String user) throws Exception {
        return 0;
    }

    @Override
    public long getStatusLastYear(Status status) throws Exception {
        return 0;
    }

    @Override
    public long getStatusLastYear(Status status, String user) throws Exception {
        return 0;
    }

    @Override
    public long getStatusYear(Status status) throws Exception {
        return 0;
    }

    @Override
    public long getStatusYear(Status status, String user) throws Exception {
        return 0;
    }

    @Override
    public long getStatusLastDay(Status status) throws Exception {
        return 0;
    }

    @Override
    public long getStatusLastDay(Status status, String user) throws Exception {
        return 0;
    }

    @Override
    public long getStatusDay(Status status) throws Exception {
        return 0;
    }

    @Override
    public long getStatusDay(Status status, String user) throws Exception {
        return 0;
    }

    @Override
    public Iterator<Aggregation> getStatusAggregation() {
        return null;
    }

    @Override
    public Iterator<Aggregation> getStatusAggregation(String user) {
        AggregationOptions options = AggregationOptions.builder().build();

        Iterator<Aggregation> aggregate = getDatastore().createAggregation(Loader.class)
                .match(createQuery().field("user").equal(user))
                .group("status",
                        grouping("total", Accumulator.accumulator("$sum", 1))
                )
                .aggregate(Aggregation.class, AggregationOptions.builder().allowDiskUse(true).batchSize(50).build());

        logger.debug("[{}]\t[getStatusAggregation] - user: {}", LoaderDAOImpl.class.getSimpleName(), user);
        return aggregate;
    }

    @Override
    public Key<Loader> insert(Loader loader) throws Exception {
        if(Objects.isNull(loader))  throw new Exception();
        Key<Loader> result = save(loader);
        if(Objects.isNull(result))    throw new Exception();

        logger.debug("[{}]\t[insert] - loader: {}", LoaderDAOImpl.class.getSimpleName(), loader.toString());
        return result;
    }

    @Override
    public WriteResult deleteById(String objectId) throws Exception {
        Loader loader = getById(objectId);
        Query<LoaderDetails> query = getDatastore().createQuery(LoaderDetails.class);

        query.criteria("loader").equal(loader);

        getDatastore().delete(query);

        WriteResult writeResult = getDatastore().delete(loader);
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteById] - objectId: {}", LoaderDAOImpl.class.getSimpleName(), objectId);
        return writeResult;
    }

    @Override
    public WriteResult deleteByUser(String user) throws Exception {
        return null;
    }

    @Override
    public WriteResult deleteByStatus(Status status) throws Exception {
        return null;
    }
}
