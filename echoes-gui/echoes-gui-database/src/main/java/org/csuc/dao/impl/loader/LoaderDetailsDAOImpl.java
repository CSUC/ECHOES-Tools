package org.csuc.dao.impl.loader;

import com.mongodb.WriteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.csuc.dao.loader.LoaderDAO;
import org.csuc.dao.loader.LoaderDetailsDAO;
import org.csuc.entities.loader.Loader;
import org.csuc.entities.loader.LoaderDetails;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;

import java.util.List;
import java.util.Objects;


/**
 * @author amartinez
 */
public class LoaderDetailsDAOImpl extends BasicDAO<LoaderDetails, ObjectId> implements LoaderDetailsDAO {

    private static Logger logger = LogManager.getLogger(LoaderDetailsDAOImpl.class);

    public LoaderDetailsDAOImpl(Class<LoaderDetails> entityClass, Datastore ds) {
        super(entityClass, ds);
    }


    @Override
    public LoaderDetails getById(String objectId) throws Exception {
        LoaderDetails loaderDetails = createQuery().field("_id").equal(objectId).get();
        if(Objects.isNull(loaderDetails))  throw new Exception();

        logger.debug("[{}]\t[getById] - objectId: {}", LoaderDetailsDAOImpl.class.getSimpleName(), objectId);
        return loaderDetails;
    }

    @Override
    public Key<LoaderDetails> insert(LoaderDetails loaderDetails) throws Exception {
        if(Objects.isNull(loaderDetails))  throw new Exception();
        Key<LoaderDetails> result = save(loaderDetails);
        if(Objects.isNull(result))    throw new Exception();

        logger.debug("[{}]\t[insert] - quality: {}", LoaderDetailsDAOImpl.class.getSimpleName(), loaderDetails.toString());
        return result;
    }

    @Override
    public WriteResult deleteById(String objectId) throws Exception {
        if(Objects.isNull(objectId))    throw new Exception();
        WriteResult writeResult = delete(getById(objectId));
        if(Objects.isNull(writeResult))    throw new Exception();

        logger.debug("[{}]\t[deleteById] - objectId: {}", LoaderDetailsDAOImpl.class.getSimpleName(), objectId);
        return writeResult;
    }

    @Override
    public List<LoaderDetails> getErrorsById(String objectId, int offset, int limit, String orderby) throws Exception {
        if(Objects.isNull(objectId))  throw new Exception();

        logger.debug("[{}]\t[getByUser] - user: {}\toption:[offset: {}\tlimit: {}\torder: {}]", LoaderDetailsDAOImpl.class.getSimpleName(), objectId, offset, limit, orderby);

        LoaderDAO loaderDAO = new LoaderDAOImpl(Loader.class, getDatastore());

        Loader loader = loaderDAO.getById(objectId);

        Query<LoaderDetails> query = createQuery();

        query.and(
                query.criteria("loader").equal(loader),
                query.criteria("status-code").notEqual(200)
        );

        return (Objects.nonNull(orderby))
                ? query.order(orderby).asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit))
                : query.asList(new FindOptions().skip(offset > 0 ? ( ( offset - 1 ) * limit ) : 0 ).limit(limit));
    }

    @Override
    public long countErrorsById(String objectId) throws Exception {
        if(Objects.isNull(objectId))  throw new Exception();

        logger.debug("[{}]\t[countErrorsById] - objectId: {}", LoaderDetailsDAOImpl.class.getSimpleName(), objectId);

        LoaderDAO loaderDAO = new LoaderDAOImpl(Loader.class, getDatastore());

        Loader loader = loaderDAO.getById(objectId);

        Query<LoaderDetails> query = createQuery();

        query.and(
                query.criteria("loader").equal(loader),
                query.criteria("status-code").notEqual(200)
        );

        return query.count();
    }
}
