package org.csuc.analyse.core.strategy.xslt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.analyse.core.strategy.ParserMethod;
import org.csuc.analyse.core.util.xml.Namespace;
import org.csuc.analyse.core.util.xml.Node;
import org.csuc.analyse.core.util.xml.Result;
import org.csuc.core.HDFS;
import org.csuc.deserialize.JaxbUnmarshal;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        logger.debug(String.format("Method: %s", getClass().getSimpleName()));
        ClassLoader classLoader = getClass().getClassLoader();

        String xsl = classLoader.getResource("count.xsl").toExternalForm();
        transformer =  new net.sf.saxon.TransformerFactoryImpl().newTransformer(new StreamSource(xsl));
    }

    @Override
    public void parser(String fileOrPath) throws Exception{
        File tmp = Files.createTempFile("analyse-core-tmp-xsl-", ".xml").toFile();

        transformer.transform(new StreamSource(new FileInputStream(fileOrPath)),
                new StreamResult(new FileOutputStream(tmp)));
        files.add(tmp);
    }

    @Override
    public void parser(URL url) throws Exception{
        File tmp = Files.createTempFile("analyse-core-tmp-xsl-", ".xml").toFile();
        transformer.transform(new StreamSource(url.openStream()),
                new StreamResult(new FileOutputStream(tmp)));
        files.add(tmp);
    }

    @Override
    public void createXML(OutputStream outs) {
        try{
            Result result = new Result();

            files.forEach((File f) ->{
                Result jxb = (Result) new JaxbUnmarshal(f, new Class[]{Result.class}).getObject();

                jxb.getNamespaceResult().getNamespaces().forEach((Namespace n) ->{
                    long count = result.getNamespaceResult().getNamespaces().stream()
                            .filter(p-> Objects.equals(n.getPrefix(), p.getPrefix()) && Objects.equals(n.getUri(), p.getUri())).count();
                    if(count == 0){
                        result.getNamespaceResult().getNamespaces().add(n);
                    }
                });

                jxb.getNodeResult().getNode().forEach((Node n) ->{
                    long count = result.getNodeResult().getNode().stream().filter(p-> Objects.equals(n.getName(), p.getName()) && Objects.equals(n.getPath(), p.getPath())).count();
                    if(count == 0){
                        result.getNodeResult().getNode().add(n);
                    }else{
                        result.getNodeResult().getNode().stream().filter(p-> Objects.equals(n.getName(), p.getName()) && Objects.equals(n.getPath(), p.getPath()))
                                .forEach((Node n2) -> n2.setCount(n2.getCount() + n.getCount()));
                    }
                });
            });

            try {
                JAXBContext jc = JAXBContext.newInstance(Result.class);

                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(result, outs);
            } catch (JAXBException e) {
                logger.error(e);
            }

        }finally {
            files.forEach(f->{
                f.deleteOnExit();
                logger.info(String.format("deleted temporal file %s", f.getName()));
            });
        }
    }

    @Override
    public void createHDFS_XML(FileSystem fileSystem, Path dest) throws IOException {
        StringWriter stringWriter = new StringWriter();

        try{
            Result result = new Result();

            files.forEach((File f) ->{
                Result jxb = (Result) new JaxbUnmarshal(f, new Class[]{Result.class}).getObject();

                jxb.getNamespaceResult().getNamespaces().forEach((Namespace n) ->{
                    long count = result.getNamespaceResult().getNamespaces().stream()
                            .filter(p-> Objects.equals(n.getPrefix(), p.getPrefix()) && Objects.equals(n.getUri(), p.getUri())).count();
                    if(count == 0){
                        result.getNamespaceResult().getNamespaces().add(n);
                    }
                });

                jxb.getNodeResult().getNode().forEach((Node n) ->{
                    long count = result.getNodeResult().getNode().stream().filter(p-> Objects.equals(n.getName(), p.getName()) && Objects.equals(n.getPath(), p.getPath())).count();
                    if(count == 0){
                        result.getNodeResult().getNode().add(n);
                    }else{
                        result.getNodeResult().getNode().stream().filter(p-> Objects.equals(n.getName(), p.getName()) && Objects.equals(n.getPath(), p.getPath()))
                                .forEach((Node n2) -> n2.setCount(n2.getCount() + n.getCount()));
                    }
                });
            });

            try {
                JAXBContext jc = JAXBContext.newInstance(Result.class);

                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(result, stringWriter);

                HDFS.write(fileSystem, dest, new ByteArrayInputStream(stringWriter.toString().getBytes(StandardCharsets.UTF_8)), true);
            } catch (JAXBException e) {
                logger.error(e);
            }

        }finally {
            files.forEach(f->{
                f.deleteOnExit();
                logger.info(String.format("deleted temporal file %s", f.getName()));
            });
        }
    }

    @Override
    public void createJSON(OutputStream outs) {
        try{
            Result result = new Result();

            files.forEach((File f) ->{
                Result jxb = (Result) new JaxbUnmarshal(f, new Class[]{Result.class}).getObject();

                jxb.getNamespaceResult().getNamespaces().forEach((Namespace n) ->{
                    long count = result.getNamespaceResult().getNamespaces().stream()
                            .filter(p-> Objects.equals(n.getPrefix(), p.getPrefix()) && Objects.equals(n.getUri(), p.getUri())).count();
                    if(count == 0){
                        result.getNamespaceResult().getNamespaces().add(n);
                    }
                });

                jxb.getNodeResult().getNode().forEach((Node n) ->{
                    long count = result.getNodeResult().getNode().stream().filter(p-> Objects.equals(n.getName(), p.getName()) && Objects.equals(n.getPath(), p.getPath())).count();
                    if(count == 0){
                        result.getNodeResult().getNode().add(n);
                    }else{
                        result.getNodeResult().getNode().stream().filter(p-> Objects.equals(n.getName(), p.getName()) && Objects.equals(n.getPath(), p.getPath()))
                                .forEach((Node n2) -> n2.setCount(n2.getCount() + n.getCount()));
                    }
                });
            });

            try {
                new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValue(outs, result);
            } catch (IOException e) {
                logger.error(e);
            }
        }finally {
            files.forEach(f->{
                f.deleteOnExit();
                logger.info(String.format("deleted temporal file %s", f.getName()));
            });
        }
    }

    @Override
    public void createHDFS_JSON(FileSystem fileSystem, Path dest) {
        try{
            Result result = new Result();

            files.forEach((File f) ->{
                Result jxb = (Result) new JaxbUnmarshal(f, new Class[]{Result.class}).getObject();

                jxb.getNamespaceResult().getNamespaces().forEach((Namespace n) ->{
                    long count = result.getNamespaceResult().getNamespaces().stream()
                            .filter(p-> Objects.equals(n.getPrefix(), p.getPrefix()) && Objects.equals(n.getUri(), p.getUri())).count();
                    if(count == 0){
                        result.getNamespaceResult().getNamespaces().add(n);
                    }
                });

                jxb.getNodeResult().getNode().forEach((Node n) ->{
                    long count = result.getNodeResult().getNode().stream().filter(p-> Objects.equals(n.getName(), p.getName()) && Objects.equals(n.getPath(), p.getPath())).count();
                    if(count == 0){
                        result.getNodeResult().getNode().add(n);
                    }else{
                        result.getNodeResult().getNode().stream().filter(p-> Objects.equals(n.getName(), p.getName()) && Objects.equals(n.getPath(), p.getPath()))
                                .forEach((Node n2) -> n2.setCount(n2.getCount() + n.getCount()));
                    }
                });
            });

            try {
                ByteArrayInputStream byteArrayInputStream =
                        new ByteArrayInputStream(
                                new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsBytes(result));

                HDFS.write(fileSystem, dest, byteArrayInputStream, true);
            } catch (IOException e) {
                logger.error(e);
            }
        }finally {
            files.forEach(f->{
                f.deleteOnExit();
                logger.info(String.format("deleted temporal file %s", f.getName()));
            });
        }
    }
}
