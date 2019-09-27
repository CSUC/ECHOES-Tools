package org.csuc.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europeana.corelib.definitions.jibx.AgentType;
import eu.europeana.corelib.definitions.jibx.RDF;
import org.csuc.dao.entity.edm.Agent;
import org.csuc.deserialize.JibxUnMarshall;
import org.csuc.typesafe.QualityConfig;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class ContentTest {

    @Test
    public void quality() throws IOException {
        String data = "{\"about\":\"Agent:Person:33a1a9f4-ce82-4c55-9e34-fdf147a254d8\",\"altLabelList\":[],\"biographicalInformationList\":[],\"dateList\":[],\"gender\":{\"string\":\"Vrouw\"},\"hasMetList\":[],\"hasPartList\":[],\"identifierList\":[],\"isPartOfList\":[],\"isRelatedToList\":[{\"resource\":{\"resource\":\"Concept:other:Vermeld\"},\"string\":\"\"}],\"nameList\":[],\"noteList\":[],\"placeOfBirthList\":[],\"placeOfDeathList\":[],\"prefLabelList\":[{\"string\":\"Stijntje\"}],\"professionOrOccupationList\":[],\"sameAList\":[]}";

        ObjectMapper objectMapper = new ObjectMapper();

        AgentType agent = objectMapper.readValue(data, AgentType.class);


    }


    @Test
    public void quality2() throws IOException {
        JibxUnMarshall jibxUnMarshall = new JibxUnMarshall(
                new FileInputStream("/home/amartinez/Descargas/ff5ba553-7e9f-41e8-adf8-c727ec5c6f70/0a12aa3f-7523-4d72-b287-8560396761f9.rdf"),
                StandardCharsets.UTF_8,
                RDF.class
        );

        QualityConfig config = new QualityConfig(Paths.get("/home/amartinez/Escritorio/quality.conf"));

        RDF rdf = (RDF) jibxUnMarshall.getElement();

        rdf.getChoiceList().forEach(choice -> {
            if (choice.ifAgent()) {
                try {
                    Agent agent = new org.csuc.step.content.Agent(config.getQualityConfig().getConfig("\"edm:Agent\"")).quality(choice.getAgent());

                    System.out.println(agent);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });

    }
}
