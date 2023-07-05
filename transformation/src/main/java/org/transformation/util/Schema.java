package org.transformation.util;

import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.csuc.custom.multirecords.Metadata;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Schema {

    public static javax.xml.validation.Schema addSchema(Class[] classType) throws SAXException {
        List<Source> list = new ArrayList<>();

        Stream.of(classType).forEach(type -> {
            if (Objects.equals(type, A2AType.class))
                list.add(new StreamSource(A2AType.class.getClassLoader().getResource("A2AAllInOne_v.1.7.xsd").toExternalForm()));
            if (Objects.equals(type, OaiDcType.class)) {
                list.add(new StreamSource(OaiDcType.class.getClassLoader().getResource("oai_dc.xsd").toExternalForm()));
                list.add(new StreamSource(OaiDcType.class.getClassLoader().getResource("simpledc20021212.xsd").toExternalForm()));
                list.add(new StreamSource(OaiDcType.class.getClassLoader().getResource("xml.xsd").toExternalForm()));
            }
            if (Objects.equals(type, Ead.class))
                list.add(new StreamSource(Ead.class.getClassLoader().getResource("apeEAD.xsd").toExternalForm()));
            if (Objects.equals(type, Memorix.class))
                list.add(new StreamSource(Memorix.class.getClassLoader().getResource("MRX-API-ANY.xsd").toExternalForm()));
            if (Objects.equals(type, OAIPMHtype.class))
                list.add(new StreamSource(OAIPMHtype.class.getClassLoader().getResource("OAI-PMH.xsd").toExternalForm()));
            if (Objects.equals(type, Metadata.class))
                list.add(new StreamSource(Metadata.class.getClassLoader().getResource("multirecord.xsd").toExternalForm()));
        });

        Source[] schemaFiles = list.toArray(new Source[list.size()]);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return sf.newSchema(schemaFiles);
    }
}
