package org.csuc.echoes.gui.consumer.quality.schematron;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import org.csuc.client.Client;
import org.csuc.entities.quality.QualityDetails;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SchematronTest {

    @Test
    public void isValid() throws Exception {

        Client client = new Client("localhost", 27017, "echoes");




//        Files.walk(Paths.get("/tmp/echoes/echoes-gui-server/transformation/0e61c98c-315c-4a8b-baac-03f10e7e40ab/t11_a"))
//                .filter(Files::isRegularFile)
//                .forEach(f->{
//                    System.out.println(f.getFileName());
//
//                    try {
//                        if(!Schematron.isValid(f.toFile())){
//
//                            QualityDetails qualityDetails = new QualityDetails();
//
//                            qualityDetails.setSchematron(
//                                    Schematron.getSVRLFailedAssert(f.toFile()).stream().map(m-> new org.csuc.entities.quality.Schematron(m.getTest(), m.getText()))
//                                            .collect(Collectors.toList())
//                            );
//
//                            client.getDatastore().save(qualityDetails);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });

        QualityDetails qualityDetails = client.getDatastore().find(QualityDetails.class).field("_id").equal("cafe4a10-c0ef-40f2-9bd0-b05ed200ed35").get();
        System.out.println(qualityDetails);
    }
}