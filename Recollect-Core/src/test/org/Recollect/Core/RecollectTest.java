package org.Recollect.Core;

import nl.mindbus.a2a.A2AType;
import org.Recollect.Core.client.HttpOAIClient;
import org.Recollect.Core.client.OAIClient;
import org.Recollect.Core.handler.ListRecordHandler;
import org.Recollect.Core.parameters.ListRecordsParameters;
import org.Recollect.Core.util.ItemIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.serialize.JaxbMarshal;
import org.junit.Before;
import org.junit.Test;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class RecollectTest {

    private static Logger logger = LogManager.getLogger(RecollectTest.class);



    @Test
    public void identify() throws Exception {
//        OAIClient oaiClient = new HttpOAIClient(valid);
//        Recollect recollect = new Recollect(oaiClient);
//
//        recollect.identify();
    }

    @Test
    public void listMetadataFormats() {
    }

    @Test
    public void listMetadataFormats1() {
    }

    @Test
    public void getRecord() {
    }

    @Test
    public void listRecords() throws Exception {

        Arrays.asList("http://calaix.gencat.cat/oai/request").forEach(value->{
            try {
                OAIClient oaiClient = oaiClient = new HttpOAIClient(value);
                Recollect recollect = new Recollect(oaiClient);

                logger.info(recollect.isOAI());
                if(!recollect.isOAI())  logger.error(recollect.gethandleEventErrors());

                ListRecordsParameters listRecordsParameters = new ListRecordsParameters();
                listRecordsParameters.withMetadataPrefix("asd");
                listRecordsParameters.withSetSpec("asd");

                recollect.listRecords(listRecordsParameters, new Class[]{OAIPMHtype.class, OaiDcType.class}).forEachRemaining(record -> {
                    System.out.println(record.getHeader().getIdentifier());
                });

            } catch (Exception e) {
                logger.error(e);
            }
        });

//        Arrays.asList("http://calaix.gencat.cat").forEach(value->{
//            try {
//                OAIClient oaiClient = oaiClient = new HttpOAIClient(value);
//                Recollect recollect = new Recollect(oaiClient);
//
//                logger.info(recollect.isOAI());
//                if(!recollect.isOAI())  logger.error(recollect.gethandleEventErrors());
//            } catch (Exception e) {
//                logger.error(e);
//            }
//        });








        //        ListRecordsParameters listRecordsParameters = new ListRecordsParameters();
//        listRecordsParameters.withMetadataPrefix("oai_dc");
//        listRecordsParameters.withSetSpec("com_10687_13");
//
//        //recollect.listRecords(listRecordsParameters, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class});
//
//        ListRecordHandler listRecordHandler =
//                new ListRecordHandler(oaiClient, listRecordsParameters, new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class});
//
//
//        asStream(new ItemIterator<RecordType>(listRecordHandler)).forEach(consmer->{
//           System.out.println(consmer.getHeader().getIdentifier());
//        });



    }

    @Test
    public void listIdentifiers() {
    }

    @Test
    public void listSets() {
    }


}