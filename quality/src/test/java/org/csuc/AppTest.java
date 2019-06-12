package org.csuc;

import com.typesafe.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.format.Json;
import org.csuc.quality.Quality;
import org.csuc.step.Content;
import org.csuc.step.Schema;
import org.csuc.step.Schematron;
import org.csuc.typesafe.QualityConfig;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private Config qualityConfig = new QualityConfig(Paths.get("/home/amartinez/projectes/ECHOES/ECHOES-Tools/quality/src/main/resources/quality.defaults.conf")).getQualityConfig();
    private Logger logger = LogManager.getLogger(getClass().getSimpleName());

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws Exception {
//        String input = "/home/amartinez/Descargas/a2a_edm.xml";
//        String input = "/home/amartinez/Descargas/ff5ba553-7e9f-41e8-adf8-c727ec5c6f70";
//        String input = "/home/amartinez/Descargas/adhesiusub";
//        String input = "/home/amartinez/Descargas/AllSets2019-06-02";
        String input = "/tmp/asd";

        try {

            Quality quality = new Quality(
                    new Json(
                            new Schema(
                                    new Schematron(
                                            new Content(Paths.get("/home/amartinez/projectes/ECHOES/ECHOES-Tools/quality/src/main/resources/quality.defaults.conf")))))
            );
//
            quality.getFormatInterface().execute(Paths.get(input));

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


//        IntStream.rangeClosed(0,10000).parallel().forEach(logger::info);
    }


}
