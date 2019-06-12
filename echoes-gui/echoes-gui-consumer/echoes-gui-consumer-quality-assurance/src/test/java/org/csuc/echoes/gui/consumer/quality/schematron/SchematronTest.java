package org.csuc.echoes.gui.consumer.quality.schematron;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import eu.europeana.corelib.definitions.jibx.PlaceType;
import eu.europeana.corelib.definitions.jibx.RDF;
import org.csuc.client.Client;
import org.csuc.deserialize.JibxUnMarshall;
import org.csuc.entities.quality.QualityDetails;
import org.csuc.entities.quality.edm.Places;
import org.junit.Test;
import org.mongodb.morphia.InsertOptions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SchematronTest {

    @Test
    public void isValid() throws Exception {
        Client client = new Client("localhost", 27017, "echoes");

        RDF rdf = (RDF) new JibxUnMarshall(new FileInputStream("/tmp/a2a_edm.xml"), StandardCharsets.UTF_8, RDF.class).getElement();

        Places p = new Places();

        rdf.getChoiceList().forEach(choice -> {
            if (choice.ifPlace()) {
                PlaceType placeType = choice.getPlace();

                if(Objects.nonNull(placeType.getLat())){
                    p.setLat(placeType.getLat().getLat().toString());
                }

                if(Objects.nonNull(placeType.getLong())){
                    p.setLon(placeType.getLong().getLong().toString());
                }

                if(Objects.nonNull(placeType.getAlt())){
                    p.setAlt(placeType.getAlt().getAlt().toString());
                }

                if(Objects.nonNull(placeType.getPrefLabelList())){
                    p.setPrefLabel(placeType.getPrefLabelList().stream().map(m-> m.getString()).collect(Collectors.toList()));
                }

                if(Objects.nonNull(placeType.getAltLabelList())){
                    p.setAltLabel(placeType.getAltLabelList().stream().map(m-> m.getString()).collect(Collectors.toList()));
                }

                if(Objects.nonNull(placeType.getNoteList())){
                    p.setNote(placeType.getNoteList().stream().map(m-> m.getString()).collect(Collectors.toList()));
                }


            }
        });

        client.getDatastore().save(p, new InsertOptions().bypassDocumentValidation(true));
    }
}