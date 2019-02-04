package org.csuc.analyse.core.strategy.sax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.analyse.core.strategy.ParserMethod;
import org.csuc.analyse.core.util.xml.Result;
import org.csuc.core.HDFS;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author amartinez
 */
public class Sax implements ParserMethod {

    private Logger logger = LogManager.getLogger(Sax.class);

    private FragmentContentHandler contentHandler;
    private SAXParser parser;

    public Sax() throws Exception{
        logger.debug(String.format("Method: %s", getClass().getSimpleName()));
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);

        parser = factory.newSAXParser();

        XMLReader xr = parser.getXMLReader();

        contentHandler = new FragmentContentHandler(xr);
    }

    @Override
    public void parser(String fileOrPath) throws Exception {
        parser.parse(new FileInputStream(fileOrPath), contentHandler);
    }

    @Override
    public void parser(URL url) throws Exception{
        parser.parse(url.toString(), contentHandler);
    }

    @Override
    public void createXML(OutputStream outs) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Result.class);

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createObject(), outs);
        } catch (JAXBException e) {
            logger.error(e);
        }
    }

    @Override
    public void createHDFS_XML(FileSystem fileSystem, Path dest) throws IOException {
        StringWriter stringWriter = new StringWriter();

        try {
            JAXBContext jc = JAXBContext.newInstance(Result.class);

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(createObject(), stringWriter);

            HDFS.write(fileSystem, dest, new ByteArrayInputStream(stringWriter.toString().getBytes(StandardCharsets.UTF_8)), true);
        } catch (JAXBException e) {
            logger.error(e);
        }
    }

    @Override
    public void createJSON(OutputStream outs) {
        try {
            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValue(outs, createObject());
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public void createHDFS_JSON(FileSystem fileSystem, Path dest) {
        try {
            ByteArrayInputStream byteArrayInputStream =
                    new ByteArrayInputStream(
                            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsBytes(createObject()));

            HDFS.write(fileSystem, dest, byteArrayInputStream, true);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private Result createObject(){
        Result result = new Result();
        contentHandler.getMapValues().forEach((String xpath, String value) -> {
            org.csuc.analyse.core.util.xml.Node node = new org.csuc.analyse.core.util.xml.Node();

            Integer c = contentHandler.getMapCount().get(xpath).get();
            node.setCount(c);
            node.setName(value);
            node.setPath(xpath);

            result.getNodeResult().getNode().add(node);
        });

        contentHandler.getNamespaces().forEach((String uri, String prefix)->{
            org.csuc.analyse.core.util.xml.Namespace namespace = new org.csuc.analyse.core.util.xml.Namespace();

            namespace.setUri(uri);
            namespace.setPrefix(prefix);

            result.getNamespaceResult().getNamespaces().add(namespace);
        });
        return result;
    }

}