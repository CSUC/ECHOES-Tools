package org.Recollect.Core.download;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import nl.memorix_maior.api.rest._3.Memorix;
import org.EDM.Transformations.formats.memorix.MEMORIX2EDM;
import org.EDM.Transformations.formats.utils.SchemaType;
import org.Recollect.Core.cli.Main;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.deserialize.JaxbUnmarshal;
import org.junit.Test;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DownloadJaxbTest {

    @Test
    public void execute() throws IOException {
        URL url = new URL("http://api.memorix-maior.nl/collectiebeheer/oai-pmh/key/7f040f84-cd4e-11df-a055-9b2671262fcb/tenant/lei?verb=ListRecords&metadataPrefix=oai_dc&set=monumenten");
        OAIPMHtype oai = (OAIPMHtype) new JaxbUnmarshal(url, new Class[]{OAIPMHtype.class, OaiDcType.class, Memorix.class}).getObject();

        assertNotNull(oai);

        RecordType recordType = oai.getListRecords().getRecord().stream().findFirst().get();

            if(recordType.getAbout().stream().findFirst().get().getAny().getClass().equals(SchemaType.MEMORIX.getType())){
               JAXBElement<Memorix> memorix =
                       new JAXBElement(new QName(Memorix.class.getSimpleName()), Memorix.class, recordType.getAbout().stream().findFirst().get().getAny());

                try {
                    new MEMORIX2EDM(recordType.getHeader().getIdentifier(), memorix.getValue(), properties()).transformation(System.out, properties());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("ERROR");
            }
//        String text = IOUtils.toString(Main.class.getClassLoader().getResourceAsStream("memorix/memorix.xslt"),"UTF-8");
//
////        File xsl = new File(getClass().getClassLoader().getResource("memorix/memorix.xslt").toString());
//
//        assertNotNull(text);
//
//        System.out.println(text);
////
//        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("memorix/memorix.xslt");
//        String content = IOUtils.toString(inputStream, Charsets.UTF_8);
//
//        System.out.println(content);
    }


    private Map<String, String> properties() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("edmType", "IMAGE");
        properties.put("provider", "provider");
        properties.put("dataProvider", "dataProvider");
        properties.put("language", "language");
        properties.put("rights", "rights");
        properties.put("set", "set");

        return properties;
    }
}