package org.csuc.dao.impl;

import com.mongodb.AggregationOptions;
import org.csuc.dao.AnalyseDAO;
import org.csuc.dao.DashboardDAO;
import org.csuc.dao.RecollectDAO;
import org.csuc.entities.Analyse;
import org.csuc.entities.Recollect;
import org.csuc.utils.Aggregation;
import org.csuc.utils.Status;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.aggregation.AggregationPipeline;
import org.mongodb.morphia.query.Query;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.mongodb.morphia.aggregation.Group.grouping;
import static org.mongodb.morphia.aggregation.Projection.projection;

/**
 * @author amartinez
 */
public class DashboardDAOImpl implements DashboardDAO {

    private AnalyseDAO analyseDAO;
    private RecollectDAO recollectDAO;

    public DashboardDAOImpl(Datastore ds){
        analyseDAO = new AnalyseDAOImpl(Analyse.class, ds);
        recollectDAO = new RecollectDAOImpl(Recollect.class, ds);
    }

    @Override
    public long getStatus(Status status) throws Exception {
        return LongStream.of(analyseDAO.countByStatus(status), recollectDAO.countByStatus(status)).sum();
    }

    @Override
    public long getStatus(Status status, String user) throws Exception {
        return LongStream.of(analyseDAO.countByStatus(status, user), recollectDAO.countByStatus(status, user)).sum();
    }

    @Override
    public Object getDashboardRecollect(String user) throws Exception {
        LocalDateTime localDate = LocalDateTime.now();

        Query<Recollect> q1 = recollectDAO.getDatastore().createQuery(Recollect.class);
        Query<Recollect> q2 = recollectDAO.getDatastore().createQuery(Recollect.class);
        q1
                .and(
                        q1.criteria("user").equal(user),
                        q1.criteria("status").equal(Status.END),
                        q1.criteria("timestamp").greaterThanOrEq(localDate.with(firstDayOfMonth())),
                        q1.criteria("timestamp").lessThanOrEq(localDate.with(lastDayOfMonth())));

        q2.criteria("month").equals(localDate.getMonthValue());

        AggregationPipeline aggregationPipeline =
                recollectDAO.getDatastore().createAggregation(Recollect.class)
                        .match(q1)
                        .project(
                                projection("month", projection("$month", "timestamp" )),
                                projection("day", projection("$dayOfMonth", "timestamp" ))
                        )
                        .match(q2)
                        .group(
                                "day", grouping("total", Accumulator.accumulator("$sum", 1))
                        )
                ;

        Iterator<Aggregation> iterator = aggregationPipeline.aggregate(Aggregation.class, AggregationOptions.builder().allowDiskUse(true).batchSize(50).build());

        Map<Integer, Long> map = new HashMap<>();

        iterator.forEachRemaining(c-> map.putIfAbsent(Integer.parseInt(c.get_id()), c.getTotal()));

        IntStream.rangeClosed(localDate.with(firstDayOfMonth()).getDayOfMonth(), localDate.with(lastDayOfMonth()).getDayOfMonth()).forEach(i-> map.putIfAbsent(i, new Long(0)));

        Map result = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return result;
    }

    @Override
    public Object getDashboardAnalyse(String user) throws Exception {
        LocalDateTime localDate = LocalDateTime.now();

        Query<Analyse> q1 = analyseDAO.getDatastore().createQuery(Analyse.class);
        Query<Analyse> q2 = analyseDAO.getDatastore().createQuery(Analyse.class);
        q1
                .and(
                        q1.criteria("user").equal(user),
                        q1.criteria("status").equal(Status.END),
                        q1.criteria("timestamp").greaterThanOrEq(localDate.with(firstDayOfMonth())),
                        q1.criteria("timestamp").lessThanOrEq(localDate.with(lastDayOfMonth())));

        q2.criteria("month").equals(localDate.getMonthValue());

        AggregationPipeline aggregationPipeline =
                analyseDAO.getDatastore().createAggregation(Analyse.class)
                        .match(q1)
                        .project(
                                projection("month", projection("$month", "timestamp" )),
                                projection("day", projection("$dayOfMonth", "timestamp" ))
                        )
                        .match(q2)
                        .group(
                                "day", grouping("total", Accumulator.accumulator("$sum", 1))
                        )
                ;

        Iterator<Aggregation> iterator = aggregationPipeline.aggregate(Aggregation.class, AggregationOptions.builder().allowDiskUse(true).batchSize(50).build());

        Map<Integer, Long> map = new HashMap<>();

        iterator.forEachRemaining(c-> map.putIfAbsent(Integer.parseInt(c.get_id()), c.getTotal()));

        IntStream.rangeClosed(localDate.with(firstDayOfMonth()).getDayOfMonth(), localDate.with(lastDayOfMonth()).getDayOfMonth()).forEach(i-> map.putIfAbsent(i, new Long(0)));

        Map result = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return result;
    }

    @Override
    public long getStatusLastMonth(Status status) throws Exception {
        return  LongStream.of(analyseDAO.getStatusLastMonth(status), recollectDAO.getStatusLastMonth(status)).sum();
    }

    @Override
    public long getStatusLastMonth(Status status, String user) throws Exception {
        return LongStream.of(analyseDAO.getStatusLastMonth(status, user), recollectDAO.getStatusLastMonth(status, user)).sum();
    }

    @Override
    public int getStatusLastMonthIncrease(Status status) throws Exception {
        double y1 = getStatusMonth(status);
        double y2 = getStatusLastMonth(status);

        return (y1 == 0 && y2 == 0) ? 0 : ( (y2 == 0) ? 100 : (int) Math.round(((y1-y2)/y2)*100));
        //return  (y2 == 0) ? 100 :  (int) Math.round(((y1-y2)/y2)*100);
    }

    @Override
    public int getStatusLastMonthIncrease(Status status, String user) throws Exception {
        double y1 = getStatusMonth(status, user);
        double y2 = getStatusLastMonth(status, user);

        return (y1 == 0 && y2 == 0) ? 0 : ( (y2 == 0) ? 100 : (int) Math.round(((y1-y2)/y2)*100));
        //return  (y2 == 0) ? 100 :  (int) Math.round(((y1-y2)/y2)*100);
    }

    @Override
    public long getStatusMonth(Status status) throws Exception {
        return LongStream.of(recollectDAO.getStatusMonth(status), analyseDAO.getStatusMonth(status)).sum();
    }

    @Override
    public long getStatusMonth(Status status, String user) throws Exception {
        return LongStream.of(recollectDAO.getStatusMonth(status, user), analyseDAO.getStatusMonth(status, user)).sum();
    }

    @Override
    public long getStatusLastDay(Status status) {
        return 0;
    }

    @Override
    public long getStatusLastDay(Status status, String user) {
        return 0;
    }

    @Override
    public long getStatusLastYear(Status status) {
        return 0;
    }

    @Override
    public long getStatusLastYear(Status status, String user) {
        return 0;
    }
}
