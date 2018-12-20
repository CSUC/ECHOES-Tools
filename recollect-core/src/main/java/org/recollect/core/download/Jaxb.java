package org.recollect.core.download;

import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.a2a.A2A2EDM;
import org.EDM.Transformations.formats.dc.DC2EDM;
import org.EDM.Transformations.formats.ead.EAD2EDM;
import org.EDM.Transformations.formats.memorix.MEMORIX2EDM;
import org.EDM.Transformations.formats.utils.SchemaType;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.core.HDFS;
import org.csuc.util.FormatType;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

/**
 * @author amartinez
 */
public class Jaxb implements Download {

    private static Logger logger = LogManager.getLogger(Jaxb.class);

    private RecordType record;
    private Object object;
    private SchemaType schemaType;


    public Jaxb(RecordType record, Object object, SchemaType shSchemaType) {
        this.record = record;
        this.object = object;
        this.schemaType = shSchemaType;
    }

    @Override
    public void execute(Map<String, String> properties) throws Exception {
        EDM edm = getMetadataSchema(IoBuilder.forLogger(Jaxb.class).setLevel(Level.INFO).buildOutputStream(), properties);

        if (Objects.nonNull(edm))
            edm.creation(StandardCharsets.UTF_8, true, IoBuilder.forLogger(Jaxb.class).setLevel(Level.INFO).buildOutputStream());

        logger.info(String.format("Donwload item (%s) identifier %s",
                object.getClass().getSimpleName(), record.getHeader().getIdentifier()));
    }

    @Override
    public void execute(Map<String, String> properties, FormatType formatType) throws Exception {
        EDM edm = getMetadataSchema(IoBuilder.forLogger(Jaxb.class).setLevel(Level.INFO).buildOutputStream(), properties);

        if (Objects.nonNull(edm))
            edm.creation(StandardCharsets.UTF_8, true, IoBuilder.forLogger(Jaxb.class).setLevel(Level.INFO).buildOutputStream(), formatType);

        logger.info(String.format("Donwload item (%s) identifier %s",
                object.getClass().getSimpleName(), record.getHeader().getIdentifier()));
    }

    @Override
    public void execute(Path outs, Map<String, String> properties) throws Exception {
        Path filename = Paths.get(outs + File.separator
                + StringUtils.replaceAll(record.getHeader().getIdentifier(), "[^a-zA-Z0-9.-]", "_") + ".rdf");

        if (Files.exists(outs, LinkOption.NOFOLLOW_LINKS)) {
            EDM edm = getMetadataSchema(new FileOutputStream(filename.toFile()), properties);

            if (Objects.nonNull(edm))
                edm.creation(StandardCharsets.UTF_8, true, new FileOutputStream(filename.toFile()));

            logger.info(String.format("Donwload item (%s) identifier %s",
                    object.getClass().getSimpleName(), record.getHeader().getIdentifier()));
        }
    }

    @Override
    public void execute(Path outs, Map<String, String> properties, FormatType formatType) throws Exception {
        Path filename = Paths.get(outs + File.separator
                + StringUtils.replaceAll(record.getHeader().getIdentifier(), "[^a-zA-Z0-9.-]", "_") + "." + formatType.extensions().stream().findFirst().get());

        if (Files.exists(outs, LinkOption.NOFOLLOW_LINKS)) {

            EDM edm = getMetadataSchema(new FileOutputStream(filename.toFile()), properties);

            if (Objects.nonNull(edm)){
                edm.creation(StandardCharsets.UTF_8, true, new FileOutputStream(filename.toFile()), formatType);
            }

            logger.info(String.format("Donwload item (%s) identifier %s",
                    object.getClass().getSimpleName(), record.getHeader().getIdentifier()));
        }
    }

    @Override
    public void execute(FileSystem fileSystem, org.apache.hadoop.fs.Path dest, Map<String, String> properties) throws Exception {
        String filename = StringUtils.replaceAll(record.getHeader().getIdentifier(), "[^a-zA-Z0-9.-]", "_") + ".rdf";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        EDM edm = getMetadataSchema(byteArrayOutputStream, properties);

        if (Objects.nonNull(edm)){
            edm.creation(StandardCharsets.UTF_8, true, byteArrayOutputStream);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            HDFS.write(fileSystem, new org.apache.hadoop.fs.Path(dest, filename), inputStream, true);
        }

        logger.info(String.format("Donwload item (%s) identifier %s",
                object.getClass().getSimpleName(), record.getHeader().getIdentifier()));
    }

    @Override
    public void execute(FileSystem fileSystem, org.apache.hadoop.fs.Path dest, Map<String, String> properties, FormatType formatType) throws Exception {
        String filename = StringUtils.replaceAll(record.getHeader().getIdentifier(), "[^a-zA-Z0-9.-]", "_") + "." + formatType.extensions().stream().findFirst().get();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        EDM edm = getMetadataSchema(byteArrayOutputStream, properties);

        if (Objects.nonNull(edm)){
            edm.creation(StandardCharsets.UTF_8, true, byteArrayOutputStream, formatType);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            HDFS.write(fileSystem, new org.apache.hadoop.fs.Path(dest, filename), inputStream, true);
        }

        logger.info(String.format("Donwload item (%s) identifier %s",
                object.getClass().getSimpleName(), record.getHeader().getIdentifier()));
    }

    /**
     *
     *
     * @param outputStream
     * @param properties
     * @return
     * @throws Exception
     */
    private EDM getMetadataSchema(OutputStream outputStream, Map<String, String> properties) throws Exception {
        EDM edm = null;

        switch (schemaType){
            case A2A:
                edm = new A2A2EDM(record.getHeader().getIdentifier(), (A2AType) object, properties);
                break;
            case DC:
                edm = new DC2EDM(record.getHeader().getIdentifier(), (OaiDcType) object, properties);
                break;
            case MEMORIX:
                new MEMORIX2EDM(record.getHeader().getIdentifier(), (Memorix) object, properties).transformation(outputStream, properties);
                break;
            case EAD:
                new EAD2EDM(record.getHeader().getIdentifier(), (Ead) object, properties).transformation(outputStream, properties);
                break;
            default:
                logger.info(String.format("%s Unknow MetadataType", record.getHeader().getIdentifier()));
                break;
        }
        return edm;
    }
}
