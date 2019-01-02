package org.csuc.dao.impl;

import com.mongodb.AggregationOptions;
import com.mongodb.WriteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.AnalyseDAO;
import org.csuc.entities.Analyse;
import org.csuc.utils.Aggregation;
import org.csuc.utils.Status;
import org.csuc.utils.parser.ParserFormat;
import org.csuc.utils.parser.ParserMethod;
import org.csuc.utils.parser.ParserType;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.mongodb.morphia.aggregation.Group.grouping;


/**
 * @author amartinez
 */
public class AnalyseDAOImpl extends BasicDAO<Analyse, ObjectId> implements AnalyseDAO {

    private static Logger logger = LogManager.getLogger(AnalyseDAOImpl.class);

    public AnalyseDAOImpl(Class<Analyse> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    @Override
    public Analyse getById(String objectId) throws Exception {
        Analyse analyse = createQuery().field("_id").equal(objectId).get();
        if(Objects.isNull(analyse))  throw new Exception();

        logger.debug("[{}]\t[getById] - objectId: {}", AnalyseDAOImpl.class.getSimpleName(), objectId);
        return analyse;
    }

    @Override
    public List<Analyse> getByUser(String user, String orderby) throws Exception {
        if(Objects.isNull(user))  throw new Exception();

        logger.debug("[{}]\t[getByUser] - user: {}\toption:[order: {}]", AnalyseDAOImpl.class.getSimpleName(), user, orderby);
        return (Objects.nonNull(orderby))
                ? find(createQuery().field("user").equal(user).order(orderby)).asList()
                : find(createQuery().field("user").equal(user)).asList();
    }

    @Override
    public List<Analyse> getByUser(String user, int offset, int limit, String orderby) throws Exception {
        if(Objects.isNull(user))  throw new Exception();

        logger.debug("[{}]\t[getByUser] - user: {}\toption:[offset: {}\tlimit: {}\torder: {}]", AnalyseDAOImpl.class.getSimpleName(), user, offset, limit, orderby);
        return (Objects.nonNull(orderby))
                ? find(createQuery().field("user").equal(user).order(orderby)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit))
                : find(createQuery().field("user").equal(user)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));

    }

    @Override
    public long countByUser(String user) throws Exception {
        if(Objects.isNull(user))  throw new Exception();

        logger.debug("[{}]\t[countByUser] - user: {}", AnalyseDAOImpl.class.getSimpleName(), user);
        return find(createQuery().field("user").equal(user)).count();
    }

    @Override
    public List<Analyse> getByMethod(ParserMethod method) throws Exception {
        if(Objects.isNull(method))  throw new Exception();

        logger.debug("[{}]\t[getByMethod] - method: {}", AnalyseDAOImpl.class.getSimpleName(), method.toString());
        return find(createQuery().field("method").equal(method)).asList();
    }

    @Override
    public List<Analyse> getByMethod(ParserMethod method, String user) throws Exception {
        if(Objects.isNull(method) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("method").equal(method),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[getByMethod] - method: {}\tuser: {}", AnalyseDAOImpl.class.getSimpleName(), method.toString(), user);
        return find(query).asList();
    }

    @Override
    public List<Analyse> getByMethod(ParserMethod method, int offset, int limit) throws Exception {
        if(Objects.isNull(method))  throw new Exception();
        //new FindOptions().skip(offset).limit(limit)

        logger.debug("[{}]\t[getByMethod] - method: {}\toptions:[offset: {}\tlimit: {}]", AnalyseDAOImpl.class.getSimpleName(), method.toString(), offset, limit);
        return find(createQuery().field("method").equal(method)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public List<Analyse> getByMethod(ParserMethod method, String user, int offset, int limit) throws Exception {
        if(Objects.isNull(method) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("method").equal(method),
                query.criteria("user").equal(user)
        );

        //new FindOptions().skip(offset).limit(limit)

        logger.debug("[{}]\t[getByMethod] - method: {}\tuser: {}\toptions:[offset: {}\tlimit: {}]", AnalyseDAOImpl.class.getSimpleName(), method.toString(), user, offset, limit);
        return find(query).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public long countByMethod(ParserMethod method) throws Exception {
        if(Objects.isNull(method))  throw new Exception();

        logger.debug("[{}]\t[countByMethod] - method: {}", AnalyseDAOImpl.class.getSimpleName(), method.toString());
        return find(createQuery().field("method").equal(method)).count();
    }

    @Override
    public long countByMethod(ParserMethod method, String user) throws Exception {
        if(Objects.isNull(method) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("method").equal(method),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[countByMethod] - method: {}\tuser: {}", AnalyseDAOImpl.class.getSimpleName(), method.toString(), user);
        return find(query).count();
    }

    @Override
    public List<Analyse> getByType(ParserType type) throws Exception {
        if(Objects.isNull(type))  throw new Exception();

        logger.debug("[{}]\t[getByType] - type: {}", AnalyseDAOImpl.class.getSimpleName(), type.value());
        return find(createQuery().field("type").equal(type)).asList();
    }

    @Override
    public List<Analyse> getByType(ParserType type, String user) throws Exception {
        if(Objects.isNull(type) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("type").equal(type),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[getByType] - type: {}\tuser: {}", AnalyseDAOImpl.class.getSimpleName(), type.value(), user);
        return find(query).asList();
    }

    @Override
    public List<Analyse> getByType(ParserType type, int offset, int limit) throws Exception {
        if(Objects.isNull(type))  throw new Exception();

        //new FindOptions().skip(offset).limit(limit)

        logger.debug("[{}]\t[getByType] - type: {}\toptions:[offset: {} \tlimit: {}]", AnalyseDAOImpl.class.getSimpleName(), type.value(), offset, limit);
        return find(createQuery().field("type").equal(type)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public List<Analyse> getByType(ParserType type, String user, int offset, int limit) throws Exception {
        if(Objects.isNull(type) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("type").equal(type),
                query.criteria("user").equal(user)
        );

        //new FindOptions().skip(offset).limit(limit)

        logger.debug("[{}]\t[getByType] - type: {}\tuser: {}\toptions:[offset: {} \tlimit: {}]", AnalyseDAOImpl.class.getSimpleName(), type.value(), user, offset, limit);
        return find(query).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public long countByType(ParserType type) throws Exception {
        if(Objects.isNull(type))  throw new Exception();

        logger.debug("[{}]\t[countByType] - type: {}", AnalyseDAOImpl.class.getSimpleName(), type.value());
        return find(createQuery().field("type").equal(type)).count();
    }

    @Override
    public long countByType(ParserType type, String user) throws Exception {
        if(Objects.isNull(type) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("type").equal(type),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[countByType] - type: {}\tuser: {}", AnalyseDAOImpl.class.getSimpleName(), type.value(), user);
        return find(query).count();
    }

    @Override
    public List<Analyse> getByFormat(ParserFormat format) throws Exception {
        if(Objects.isNull(format))  throw new Exception();

        logger.debug("[{}]\t[getByFormat] - format: {}", AnalyseDAOImpl.class.getSimpleName(), format.value());
        return find(createQuery().field("format").equal(format)).asList();
    }

    @Override
    public List<Analyse> getByFormat(ParserFormat format, String user) throws Exception {
        if(Objects.isNull(format) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("format").equal(format),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[getByFormat] - format: {}\tuser: {}", AnalyseDAOImpl.class.getSimpleName(), format.value(), user);
        return find(query).asList();
    }

    @Override
    public List<Analyse> getByFormat(ParserFormat format, int offset, int limit) throws Exception {
        if(Objects.isNull(format))  throw new Exception();

        //new FindOptions().skip(offset).limit(limit)

        logger.debug("[{}]\t[getByFormat] - format: {}\toptions:[offset: {}\tlimit: {}]", AnalyseDAOImpl.class.getSimpleName(), format.value(), offset, limit);
        return find(createQuery().field("format").equal(format)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public List<Analyse> getByFormat(ParserFormat format, String user, int offset, int limit) throws Exception {
        if(Objects.isNull(format) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("format").equal(format),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[getByFormat] - format: {}\tuser: {}\toptions:[offset: {}\tlimit: {}]", AnalyseDAOImpl.class.getSimpleName(), format.value(), user, offset, limit);
        return find(query).asList();
    }

    @Override
    public long countByFormat(ParserFormat format) throws Exception {
        if(Objects.isNull(format))  throw new Exception();

        logger.debug("[{}]\t[countByFormat] - format: {}", AnalyseDAOImpl.class.getSimpleName(), format.value());
        return find(createQuery().field("format").equal(format)).count();
    }

    @Override
    public long countByFormat(ParserFormat format, String user) throws Exception {
        if(Objects.isNull(format) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("format").equal(format),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[countByFormat] - format: {}\tuser: {}", AnalyseDAOImpl.class.getSimpleName(), format.value(), user);
        return find(query).count();
    }

    @Override
    public List<Analyse> getByStatus(Status status) throws Exception {
        if(Objects.isNull(status))  throw new Exception();

        logger.debug("[{}]\t[getByStatus] - status: {}", AnalyseDAOImpl.class.getSimpleName(), status);
        return find(createQuery().field("status").equal(status)).asList();
    }

    @Override
    public List<Analyse> getByStatus(Status status, String user) throws Exception {
        if(Objects.isNull(status) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("status").equal(status),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[getByStatus] - status: {}\tuser: {}", AnalyseDAOImpl.class.getSimpleName(), status, user);
        return find(query).asList();
    }

    @Override
    public List<Analyse> getByStatus(Status status, int offset, int limit) throws Exception {
        if(Objects.isNull(status))  throw new Exception();

        //new FindOptions().skip(offset).limit(limit)

        logger.debug("[{}]\t[getByStatus] - status: {}\toptions:[offset: {}\tlimit: {}]", AnalyseDAOImpl.class.getSimpleName(), status, offset, limit);
        return find(createQuery().field("status").equal(status)).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public List<Analyse> getByStatus(Status status, String user, int offset, int limit) throws Exception {
        if(Objects.isNull(status) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("status").equal(status),
                query.criteria("user").equal(user)
        );

        //new FindOptions().skip(offset).limit(limit)

        logger.debug("[{}]\t[getByStatus] - status: {}\toptions:[offset: {}\tlimit: {}]", AnalyseDAOImpl.class.getSimpleName(), status, offset, limit);
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

        Query<Analyse> query = createQuery();

        query.and(
                query.criteria("status").equal(status),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[countByStatus] - status: {}\tuser: {}", AnalyseDAOImpl.class.getSimpleName(), status, user);
        return find(query).count();
    }

    @Override
    public long getStatusLastMonth(Status status) throws Exception {
        if(Objects.isNull(status))  throw new Exception();

        Query<Analyse> query = createQuery();

        LocalDateTime earlier = LocalDateTime.now().minusMonths(1);
        LocalDateTime start = earlier.with(firstDayOfMonth());
        LocalDateTime finish = earlier.with(lastDayOfMonth());

        query.and(
                query.criteria("status").equal(status),
                query.criteria("timestamp").greaterThanOrEq(start),
                query.criteria("timestamp").lessThanOrEq(finish)
        );

        logger.debug("[{}]\t[countByStatus] - status: {}\tuser: {}", AnalyseDAOImpl.class.getSimpleName(), status);
        return find(query).count();
    }

    @Override
    public long getStatusLastMonth(Status status, String user) throws Exception {
        if(Objects.isNull(status))  throw new Exception();

        Query<Analyse> query = createQuery();

        LocalDateTime earlier = LocalDateTime.now().minusMonths(1);
        LocalDateTime start = earlier.with(firstDayOfMonth());
        LocalDateTime finish = earlier.with(lastDayOfMonth());

        query.and(
                query.criteria("status").equal(status),
                query.criteria("timestamp").greaterThanOrEq(start),
                query.criteria("timestamp").lessThanOrEq(finish),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[countByStatus] - status: {}\tuser: {}", AnalyseDAOImpl.class.getSimpleName(), status);
        return find(query).count();
    }

    @Override
    public long getStatusMonth(Status status) throws Exception {
        if(Objects.isNull(status)) throw new Exception();

        Query<Analyse> query = createQuery();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.with(firstDayOfMonth());
        LocalDateTime finish = now.with(lastDayOfMonth());

        query.and(
                query.criteria("status").equal(status),
                query.criteria("timestamp").greaterThanOrEq(start),
                query.criteria("timestamp").lessThanOrEq(finish)
        );

        logger.debug("[{}]\t[getStatusMonth] - status: {}", AnalyseDAOImpl.class.getSimpleName(), status);
        return find(query).count();
    }

    @Override
    public long getStatusMonth(Status status, String user) throws Exception {
        if(Objects.isNull(status) || Objects.isNull(user))  throw new Exception();

        Query<Analyse> query = createQuery();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.with(firstDayOfMonth());
        LocalDateTime finish = now.with(lastDayOfMonth());

        query.and(
                query.criteria("status").equal(status),
                query.criteria("timestamp").greaterThanOrEq(start),
                query.criteria("timestamp").lessThanOrEq(finish),
                query.criteria("user").equal(user)
        );

        logger.debug("[{}]\t[getStatusMonth] - status: {}\tuser: {}", AnalyseDAOImpl.class.getSimpleName(), status, user);
        return find(query).count();
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
        AggregationOptions options = AggregationOptions.builder().build();

        Iterator<Aggregation> aggregate = getDatastore().createAggregation(Analyse.class)
                .group("status",
                        grouping("total", Accumulator.accumulator("$sum", 1))
                )
                .aggregate(Aggregation.class, AggregationOptions.builder().allowDiskUse(true).batchSize(50).build());

        logger.debug("[{}]\t[getStatusAggregation]", AnalyseDAOImpl.class.getSimpleName());
        return aggregate;
    }

    @Override
    public Iterator<Aggregation> getStatusAggregation(String user) {
        AggregationOptions options = AggregationOptions.builder().build();

        Iterator<Aggregation> aggregate = getDatastore().createAggregation(Analyse.class)
                .match(createQuery().field("user").equal(user))
                .group("status",
                        grouping("total", Accumulator.accumulator("$sum", 1))
                )
                .aggregate(Aggregation.class, AggregationOptions.builder().allowDiskUse(true).batchSize(50).build());

        logger.debug("[{}]\t[getStatusAggregation] - user: {}", AnalyseDAOImpl.class.getSimpleName(), user);
        return aggregate;
    }

    @Override
    public Key<Analyse> insert(Analyse analyse) throws Exception {
        if(Objects.isNull(analyse))  throw new Exception();
        Key<Analyse> result = save(analyse);
        if(Objects.isNull(result))    throw new Exception();

        logger.debug("[{}]\t[insert] - analyse: {}", AnalyseDAOImpl.class.getSimpleName(), analyse.toString());
        return result;
    }

    @Override
    public WriteResult deleteById(String objectId) throws Exception {
        if(Objects.isNull(objectId))    throw new Exception();
        WriteResult writeResult = delete(getById(objectId));
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteById] - objectId: {}", AnalyseDAOImpl.class.getSimpleName(), objectId);
        return writeResult;
    }

    @Override
    public WriteResult deleteByUser(String user) throws Exception {
        if(Objects.isNull(user))    throw new Exception();
        WriteResult writeResult = getDatastore().delete(createQuery().field("user").equal(user));
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteByUser] - user: {}", AnalyseDAOImpl.class.getSimpleName(), user);
        return writeResult;
    }

    @Override
    public WriteResult deleteByMethod(ParserMethod method) throws Exception {
        if(Objects.isNull(method))    throw new Exception();
        WriteResult writeResult = getDatastore().delete(createQuery().field("method").equal(method));
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteByMethod] - method: {}", AnalyseDAOImpl.class.getSimpleName(), method.value());
        return writeResult;
    }

    @Override
    public WriteResult deleteByType(ParserType type) throws Exception {
        if(Objects.isNull(type))    throw new Exception();
        WriteResult writeResult = getDatastore().delete(createQuery().field("type").equal(type));
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteByType] - type: {}", AnalyseDAOImpl.class.getSimpleName(), type.value());
        return writeResult;
    }

    @Override
    public WriteResult deleteByStatus(Status status) throws Exception {
        if(Objects.isNull(status))    throw new Exception();
        WriteResult writeResult = getDatastore().delete(createQuery().field("status").equal(status));
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteByStatus] - status: {}", AnalyseDAOImpl.class.getSimpleName(), status.value());
        return writeResult;
    }
}
