package org.csuc.dao.impl;

import org.csuc.client.Client;
import org.csuc.dao.AnalyseDAO;
import org.csuc.dao.AnalyseErrorDAO;
import org.csuc.entities.Analyse;
import org.csuc.entities.AnalyseError;
import org.csuc.utils.Status;
import org.junit.Test;

public class AnalyseDAOImplTest {

    @Test
    public void test() throws Exception {
        AnalyseDAO analyseDAO = new AnalyseDAOImpl(Analyse.class, new Client("localhost", 27017, "echoes").getDatastore());
        AnalyseErrorDAO analyseError = new AnalyseErrorDAOImpl(AnalyseError.class, new Client("localhost", 27017, "echoes").getDatastore());

//        System.out.println("getStatusMonth END: " + analyseDAO.getStatusMonth(Status.END, "github|32936334"));
//        System.out.println("getStatusLastMonth END: " + analyseDAO.getStatusLastMonth(Status.END, "github|32936334"));

        System.out.println(analyseError.getByReference("bcd493a0-1094-4415-8b79-9ebc445fd251"));
    }

}