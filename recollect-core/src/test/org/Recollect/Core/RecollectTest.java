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
import org.openarchives.oai._2.IdentifyType;
import org.openarchives.oai._2.MetadataFormatType;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class RecollectTest {

    private static Logger logger = LogManager.getLogger(RecollectTest.class);

    private OAIClient oaiClient;

    @Before
    public void setUp() throws Exception {
        String url = "https://webservices.picturae.com/a2a/20a181d4-c896-489f-9d16-20a3b7306b15";
        oaiClient = new HttpOAIClient(url);
    }

    @Test
    public void identify() throws Exception {
        Recollect recollect = new Recollect(oaiClient);
        if(recollect.isOAI()){
            IdentifyType identifyType = recollect.identify();
            assertNotNull(identifyType);
            assertEquals(Arrays.asList().isEmpty(), recollect.getExceptionList().isEmpty());
        }else{
            logger.error(recollect.gethandleEventErrors());
        }
    }

    @Test
    public void listMetadataFormats() {
        Recollect recollect = new Recollect(oaiClient);
        Iterator<MetadataFormatType> metadataFormatTypeIterator = recollect.listMetadataFormats();

        assertNotNull(metadataFormatTypeIterator);
        logger.info(metadataFormatTypeIterator);
    }


    @Test
    public void getRecord() {
    }

    @Test
    public void listRecords() throws Exception {
        String url = "http://calaix.gencat.cat/oai/request";
        Recollect recollect = new Recollect(new HttpOAIClient(url));

        if(recollect.isOAI()){
            ListRecordsParameters listRecordsParameters = new ListRecordsParameters();
            listRecordsParameters.withSetSpec("com_10687_13");
            listRecordsParameters.withMetadataPrefix("oai_dc");
            Iterator<RecordType> recordTypeIterator = recollect.listRecords(listRecordsParameters, new Class[]{OAIPMHtype.class, OaiDcType.class});
            assertNotNull(recordTypeIterator);


            recordTypeIterator.forEachRemaining(consumer->{
                String identifier = consumer.getHeader().getIdentifier();
                assertNotNull(identifier);

                logger.info("{} - {}", identifier, (Objects.nonNull(consumer.getHeader().getStatus()) ? consumer.getHeader().getStatus().value() : null));
            });
        }else   logger.error(recollect.gethandleEventErrors());

        if(!recollect.getExceptionList().isEmpty()) logger.error(recollect.getExceptionList());
    }

    @Test
    public void listIdentifiers() {
    }

    @Test
    public void listSets() {
    }

}