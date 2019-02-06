package org.csuc.service.recollect;

import org.csuc.client.Client;
import org.csuc.dao.AnalyseDAO;
import org.csuc.dao.AnalyseErrorDAO;
import org.csuc.dao.RecollectDAO;
import org.csuc.dao.impl.AnalyseDAOImpl;
import org.csuc.dao.impl.AnalyseErrorDAOImpl;
import org.csuc.dao.impl.RecollectDAOImpl;
import org.csuc.entities.Analyse;
import org.csuc.entities.AnalyseError;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.*;

public class RecollectTest {

    @Test
    public void getRecollectById() throws Exception {
//        RecollectDAO recollectDAO = new RecollectDAOImpl(org.csuc.entities.Recollect.class, new Client("localhost", 27017, "echoes").getDatastore());
//        System.out.print(recollectDAO.getById("79a7602d-eb99-46b5-abea-710ca49b5a5e"));

        AnalyseErrorDAO analyseErrorDAO = new AnalyseErrorDAOImpl(AnalyseError.class, new Client("localhost", 27017, "echoes").getDatastore());
        AnalyseDAO analyseDAO = new AnalyseDAOImpl(Analyse.class, new Client("localhost", 27017, "echoes").getDatastore());

//        AnalyseError parser = analyseErrorDAO.getByReference("e8e120fb-3243-4d88-86ac-766a3db4f2d8");
        Analyse analyse = analyseDAO.getById("e8e120fb-3243-4d88-86ac-766a3db4f2d8");

        System.out.println(analyse);

        System.out.println(analyseErrorDAO.getByReference(analyse));
        System.out.println(analyseErrorDAO.getByReference("e8e120fb-3243-4d88-86ac-766a3db4f2d8"));

    }
}