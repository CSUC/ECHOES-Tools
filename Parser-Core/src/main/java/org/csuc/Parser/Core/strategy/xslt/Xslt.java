package org.csuc.Parser.Core.strategy.xslt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.jaxb.JaxbUnmarshal;
import org.csuc.Parser.Core.strategy.ParserMethod;
import org.csuc.Parser.Core.util.xml.Result;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author amartinez
 */
public class Xslt implements ParserMethod {

    private static Logger logger = LogManager.getLogger(Xslt.class);

    private Transformer transformer;

    private List<File> files = new ArrayList<>();

    /**
     *
     * Load saxon (xslt 2.0) and transforming a Document {@link org.dom4j.Document} with XSLT
     *
     *
     * @throws TransformerException
     * @throws IOException
     */
    public Xslt() throws TransformerException, IOException {
        logger.info(String.format("Method: %s", getClass().getSimpleName()));
        ClassLoader classLoader = getClass().getClassLoader();
        File xsl = new File(classLoader.getResource("count.xsl").getFile());
        transformer =  new net.sf.saxon.TransformerFactoryImpl().newTransformer(new StreamSource(xsl));
    }

    @Override
    public void parser(String fileOrPath) {
        try {
            File tmp = Files.createTempFile("Parser-Core-tmp-xsl-", ".xml").toFile();

            transformer.transform(new StreamSource(new FileInputStream(fileOrPath)),
                    new StreamResult(new FileOutputStream(tmp)));
            files.add(tmp);
        } catch (TransformerConfigurationException e) {
           logger.error(e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (TransformerException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public void parser(URL url) {
        try {
            File tmp = Files.createTempFile("Parser-Core-tmp-xsl-", ".xml").toFile();
            transformer.transform(new StreamSource(url.openStream()),
                    new StreamResult(new FileOutputStream(tmp)));
        } catch (TransformerException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public void createXML(OutputStream outs) {
        files.forEach(f->{
            Result result = (Result) new JaxbUnmarshal(f, new Class[]{Result.class}).getObject();
            logger.info(result != null);
            result.getNamespaceResult().getNamespaces().forEach(n->{
                System.out.println(n.getUri());
            });
        });




    }

    @Override
    public void createJSON(OutputStream outs) {

    }

}
