package org.csuc.Validation.Core.cli;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.bson.Document;
import org.csuc.Validation.Core.schematron.Schematron;
import org.csuc.client.Client;
import org.csuc.entities.validation.SCH;
import org.csuc.entities.validation.Schema;
import org.csuc.entities.validation.Validation;
import org.csuc.entities.validation.ValidationDetails;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AppTest {

    @Test
    public void main() throws Exception {
        Client client = new Client("localhost", 27017, "echoes");


        String file = "/home/amartinez/Baixades/72e106a7-a554-41df-8c1b-f92227eb6dbd/t11_a/0ef3bd2e-8188-4d70-9837-672e6afefa3f.rdf";

        ValidationDetails validationDetails = new ValidationDetails();
        Validation validation = new Validation();

        Schema schema = new Schema();
        schema.setValid(true);
        schema.setMessage(null);

        validationDetails.setSchema(schema);

        SCH sch = new SCH();

        Schematron schematron = new Schematron(new File(file));
        sch.setValid(schematron.isValid());

        sch.setMessage(schematron.getSVRLFailedAssert().toDocument());
        validationDetails.setSch(sch);

//        validationDetails.setUuid("0ef3bd2e-8188-4d70-9837-672e6afefa3f");
//
//
//        client.getDatastore().save(validation);
//
//        validationDetails.setValidation(validation);
//
//        client.getDatastore().save(validationDetails);
    }
}