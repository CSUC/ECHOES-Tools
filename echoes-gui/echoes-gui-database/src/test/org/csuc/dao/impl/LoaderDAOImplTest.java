package org.csuc.dao.impl;

import org.csuc.client.Client;
import org.csuc.dao.impl.loader.LoaderDAOImpl;
import org.csuc.dao.loader.LoaderDAO;
import org.csuc.entities.loader.Loader;
import org.csuc.utils.Status;
import org.junit.Test;

public class LoaderDAOImplTest {

    @Test
    public void getById() {
    }

    @Test
    public void insert() throws Exception {
        LoaderDAO loaderDAO = new LoaderDAOImpl(Loader.class, new Client("localhost", 27017, "echoes").getDatastore());

        Loader loader = new Loader();

        loader.setContentType("application/xml");
        loader.setEndpoint("http://localhost:8080/bigdata/sparql");
        loader.setContextUri("quad");
        loader.setUser("github|32936334");
        loader.setStatus(Status.END);
        loader.setUuid("sdadadadadada");
        loader.setSize(10);
        loader.setTotal(10);

        loaderDAO.insert(loader);

    }

    @Test
    public void deleteById() {
    }
}