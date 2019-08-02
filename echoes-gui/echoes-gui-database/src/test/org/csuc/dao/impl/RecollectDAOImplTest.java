package org.csuc.dao.impl;

import org.csuc.client.Client;
import org.csuc.dao.RecollectDAO;
import org.csuc.entities.Recollect;
import org.csuc.utils.Status;
import org.junit.Test;

public class RecollectDAOImplTest {

    @Test
    public void test() throws Exception {
        RecollectDAO recollectDAO = new RecollectDAOImpl(Recollect.class, new Client("localhost", 27017, "echoes").getDatastore());


        System.out.println("getStatusMonth: " + recollectDAO.getStatusMonth(Status.ERROR, "github|32936334"));
        System.out.println("getStatusLastMonth: " + recollectDAO.getStatusLastMonth(Status.ERROR, "github|32936334"));
    }

}