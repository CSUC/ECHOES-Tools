package org.transformation.factory;

import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.core.HDFS;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.util.FormatType;
import org.edm.transformations.formats.EDM;
import org.edm.transformations.formats.a2a.A2A2EDM;
import org.edm.transformations.formats.dc.DC2EDM;
import org.edm.transformations.formats.ead.EAD2EDM;
import org.edm.transformations.formats.memorix.MEMORIX2EDM;
import org.edm.transformations.formats.utils.SchemaType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import javax.xml.bind.JAXBIntrospector;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TransformationFile implements Transformation {

    private Logger logger = LogManager.getLogger(getClass());

    private Class<?>[] classType;
    private Path input;

    private List<Throwable> throwables = new ArrayList<>();

    public TransformationFile(Path input, Class<?>[] classType) {
        this.classType = classType;
        this.input = input;
    }

    @Override
    public void console(SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException {
        Files.walk(input)
                .filter(Files::isRegularFile)
                .forEach(f -> {
                    try {
                        JaxbUnmarshal jaxbUnmarshal = new JaxbUnmarshal(f.toFile(), classType);

                        if (Objects.nonNull(jaxbUnmarshal.getObject())) {
                            Object object = JAXBIntrospector.getValue(jaxbUnmarshal.getObject());

                            if (!Objects.equals(schemaType.getType(), object.getClass()))
                                throw new Exception(String.format("File \"%s\" is not a %s valid schema", f, schemaType.getType()));

                            String uuid = UUID.randomUUID().toString();

                            EDM edm = null;

                            if (Objects.equals(A2AType.class, object.getClass()))
                                edm = new A2A2EDM(uuid, (A2AType) object, arguments);

                            if (Objects.equals(OaiDcType.class, object.getClass()))
                                edm = new DC2EDM(uuid, (OaiDcType) object, arguments);

                            if (Objects.equals(Memorix.class, object.getClass()))
                                new MEMORIX2EDM(uuid, (Memorix) object, arguments).transformation(IoBuilder.forLogger(getClass()).setLevel(Level.INFO).buildOutputStream(), arguments);

                            if (Objects.equals(Ead.class, object.getClass()))
                                new EAD2EDM(uuid, (Ead) object, arguments).transformation(IoBuilder.forLogger(getClass()).setLevel(Level.INFO).buildOutputStream(), arguments);

                            edm.creation(StandardCharsets.UTF_8, true, IoBuilder.forLogger(getClass()).setLevel(Level.INFO).buildOutputStream(), formatType);
                        }
                    } catch (Exception e) {
                        throwables.add(e);
                        logger.error(e.getMessage());
                    }
                });
    }

    @Override
    public void path(Path out, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException {
        Files.walk(input)
                .filter(Files::isRegularFile)
                .forEach(f -> {
                    try {
                        JaxbUnmarshal jaxbUnmarshal = new JaxbUnmarshal(f.toFile(), classType);

                        if (Objects.nonNull(jaxbUnmarshal.getObject())) {
                            Object object = JAXBIntrospector.getValue(jaxbUnmarshal.getObject());

                            if (!Objects.equals(schemaType.getType(), object.getClass()))
                                throw new Exception(String.format("File \"%s\" is not a %s valid schema", f, schemaType.getType()));

                            String uuid = UUID.randomUUID().toString();

                            File filename = new File(String.format("%s/%s.%s", out, uuid, formatType.extensions().stream().findFirst().get()));

                            EDM edm = null;

                            if (Objects.equals(A2AType.class, object.getClass()))
                                edm = new A2A2EDM(uuid, (A2AType) object, arguments);

                            if (Objects.equals(OaiDcType.class, object.getClass()))
                                edm = new DC2EDM(uuid, (OaiDcType) object, arguments);

                            if (Objects.equals(Memorix.class, object.getClass()))
                                new MEMORIX2EDM(uuid, (Memorix) object, arguments).transformation(new FileOutputStream(filename), arguments);

                            if (Objects.equals(Ead.class, object.getClass()))
                                new EAD2EDM(uuid, (Ead) object, arguments).transformation(new FileOutputStream(filename), arguments);

                            edm.creation(StandardCharsets.UTF_8, true, new FileOutputStream(filename), formatType);
                        }
                    } catch (Exception e) {
                        throwables.add(e);
                        logger.error(e.getMessage());
                    }
                });
    }

    @Override
    public void hdfs(String hdfsuri, String hdfuser, String hdfshome, org.apache.hadoop.fs.Path path, SchemaType schemaType, Map<String, String> arguments, FormatType formatType) throws IOException {
        HDFS hdfs = new HDFS(hdfsuri, hdfuser, hdfshome);

        Files.walk(input)
                .filter(Files::isRegularFile)
                .forEach(f -> {
                    try {
                        JaxbUnmarshal jaxbUnmarshal = new JaxbUnmarshal(f.toFile(), classType);

                        if (Objects.nonNull(jaxbUnmarshal.getObject())) {
                            Object object = JAXBIntrospector.getValue(jaxbUnmarshal.getObject());

                            if (!Objects.equals(schemaType.getType(), object.getClass()))
                                throw new Exception(String.format("File \"%s\" is not a %s valid schema", f, schemaType.getType()));

                            String uuid = UUID.randomUUID().toString();

                            String filename = String.format("%s.%s", uuid, formatType.extensions().stream().findFirst().get());

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                            EDM edm = null;

                            if (Objects.equals(A2AType.class, object.getClass()))
                                edm = new A2A2EDM(uuid, (A2AType) object, arguments);

                            if (Objects.equals(OaiDcType.class, object.getClass()))
                                edm = new DC2EDM(uuid, (OaiDcType) object, arguments);

                            if (Objects.equals(Memorix.class, object.getClass()))
                                new MEMORIX2EDM(uuid, (Memorix) object, arguments).transformation(byteArrayOutputStream, arguments);

                            if (Objects.equals(Ead.class, object.getClass()))
                                new EAD2EDM(uuid, (Ead) object, arguments).transformation(byteArrayOutputStream, arguments);

                            edm.creation(StandardCharsets.UTF_8, true, byteArrayOutputStream, formatType);

                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            InputStream inputStream = new ByteArrayInputStream(bytes);
                            HDFS.write(hdfs.getFileSystem(), new org.apache.hadoop.fs.Path(path, filename), inputStream, true);
                        }
                    } catch (Exception e) {
                        throwables.add(e);
                        logger.error(e.getMessage());
                    }
                });
    }

    @Override
    public List<Throwable> getExceptions() {
        return throwables.isEmpty() ? null : throwables;
    }
}
