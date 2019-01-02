package org.csuc.service.recollect;

import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.*;

public class RecollectTest {

    @Inject
    private Client client;

    @Test
    public void getRecollectById() throws Exception {


        RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, new Client("localhost", 27017, "echoes").getDatastore());
        System.out.print(recollectDAO.getById("79a7602d-eb99-46b5-abea-710ca49b5a5e"));
    }
}