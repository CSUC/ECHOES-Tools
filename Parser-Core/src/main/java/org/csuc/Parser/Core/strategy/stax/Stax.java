package org.csuc.Parser.Core.strategy.stax;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.strategy.ParserMethod;

import javax.xml.stream.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author amartinez
 */
public class Stax implements ParserMethod {

    private Logger logger = LogManager.getLogger(Stax.class);

    private XMLInputFactory factory;

    public Stax(){
        logger.info(String.format("Method: %s", getClass().getSimpleName()));
        factory = XMLInputFactory.newInstance();
    }

    @Override
    public void parser(String fileOrPath) {
        try {
            XMLStreamReader reader =
                    factory.createXMLStreamReader(new FileInputStream(fileOrPath));

            //
            // Parse the XML
            //
            while(reader.hasNext()){
                printEvent(reader);
                reader.next();
            }

            //
            // Close the reader
            //
            reader.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void parser(URL url) {

    }

    @Override
    public void createXML(OutputStream outs) {

    }

    @Override
    public void createJSON(OutputStream outs) {

    }

    private static void printEvent(XMLStreamReader xmlr) {
        System.out.print("EVENT:["+xmlr.getLocation().getLineNumber()+"]["+
                xmlr.getLocation().getColumnNumber()+"] ");

        System.out.print(" [");

        switch (xmlr.getEventType()) {

            case XMLStreamConstants.START_ELEMENT:
                System.out.print("<");
                printName(xmlr);
                printNamespaces(xmlr);
                printAttributes(xmlr);
                System.out.print(">");
                break;

            case XMLStreamConstants.END_ELEMENT:
                System.out.print("</");
                printName(xmlr);
                System.out.print(">");
                break;

            case XMLStreamConstants.SPACE:

            case XMLStreamConstants.CHARACTERS:
                int start = xmlr.getTextStart();
                int length = xmlr.getTextLength();
                System.out.print(new String(xmlr.getTextCharacters(),
                        start,
                        length));
                break;

            case XMLStreamConstants.PROCESSING_INSTRUCTION:
                System.out.print("<?");
                if (xmlr.hasText())
                    System.out.print(xmlr.getText());
                System.out.print("?>");
                break;

            case XMLStreamConstants.CDATA:
                System.out.print("<![CDATA[");
                start = xmlr.getTextStart();
                length = xmlr.getTextLength();
                System.out.print(new String(xmlr.getTextCharacters(),
                        start,
                        length));
                System.out.print("]]>");
                break;

            case XMLStreamConstants.COMMENT:
                System.out.print("<!--");
                if (xmlr.hasText())
                    System.out.print(xmlr.getText());
                System.out.print("-->");
                break;

            case XMLStreamConstants.ENTITY_REFERENCE:
                System.out.print(xmlr.getLocalName()+"=");
                if (xmlr.hasText())
                    System.out.print("["+xmlr.getText()+"]");
                break;

            case XMLStreamConstants.START_DOCUMENT:
                System.out.print("<?xml");
                System.out.print(" version='"+xmlr.getVersion()+"'");
                System.out.print(" encoding='"+xmlr.getCharacterEncodingScheme()+"'");
                if (xmlr.isStandalone())
                    System.out.print(" standalone='yes'");
                else
                    System.out.print(" standalone='no'");
                System.out.print("?>");
                break;

        }
        System.out.println("]");
    }

    private static void printName(XMLStreamReader xmlr){
        if(xmlr.hasName()){
            String prefix = xmlr.getPrefix();
            String uri = xmlr.getNamespaceURI();
            String localName = xmlr.getLocalName();
            printName(prefix,uri,localName);
        }
    }

    private static void printName(String prefix,
                                  String uri,
                                  String localName) {
        if (uri != null && !("".equals(uri)) ) System.out.print("['"+uri+"']:");
        if (prefix != null) System.out.print(prefix+":");
        if (localName != null) System.out.print(localName);
    }

    private static void printAttributes(XMLStreamReader xmlr){
        for (int i=0; i < xmlr.getAttributeCount(); i++) {
            printAttribute(xmlr,i);
        }
    }

    private static void printAttribute(XMLStreamReader xmlr, int index) {
        String prefix = xmlr.getAttributePrefix(index);
        String namespace = xmlr.getAttributeNamespace(index);
        String localName = xmlr.getAttributeLocalName(index);
        String value = xmlr.getAttributeValue(index);
        System.out.print(" ");
        printName(prefix,namespace,localName);
        System.out.print("='"+value+"'");
    }

    private static void printNamespaces(XMLStreamReader xmlr){
        for (int i=0; i < xmlr.getNamespaceCount(); i++) {
            printNamespace(xmlr,i);
        }
    }

    private static void printNamespace(XMLStreamReader xmlr, int index) {
        String prefix = xmlr.getNamespacePrefix(index);
        String uri = xmlr.getNamespaceURI(index);
        System.out.print(" ");
        if (prefix == null)
            System.out.print("xmlns='"+uri+"'");
        else
            System.out.print("xmlns:"+prefix+"='"+uri+"'");
    }
}
