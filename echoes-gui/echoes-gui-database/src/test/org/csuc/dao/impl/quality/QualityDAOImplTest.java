package org.csuc.dao.impl.quality;

import org.csuc.client.Client;
import org.csuc.dao.quality.QualityDAO;
import org.csuc.dao.quality.QualityDetailsDAO;
import org.csuc.entities.quality.Quality;
import org.csuc.entities.quality.QualityDetails;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class QualityDAOImplTest {

    @Test
    public void test() throws Exception {
        Client client = new Client("localhost", 27017, "echoes");



        QualityDetailsDAO qualityDetailsDAO = new QualityDetailsDAOImpl(QualityDetails.class, client.getDatastore());

        List<QualityDetails> qualityDetails = qualityDetailsDAO.getErrorsById("6167d29d-0773-43fa-9671-82b9dd152924", 0, 50 , null);

        qualityDetails.forEach(d->{
            System.out.println(d);
        });


        System.out.println(qualityDetailsDAO.countErrorsById("6167d29d-0773-43fa-9671-82b9dd152924"));

    }

}