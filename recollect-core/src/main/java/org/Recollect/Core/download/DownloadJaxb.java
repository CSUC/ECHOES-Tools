/**
 * 
 */
package org.Recollect.Core.download;

import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.a2a.A2A2EDM;
import org.EDM.Transformations.formats.dc.DC2EDM;
import org.EDM.Transformations.formats.ead.EAD2EDM;
import org.EDM.Transformations.formats.memorix.MEMORIX2EDM;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.util.FormatType;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import javax.xml.bind.JAXBElement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

/**
 * @author amartinez
 *
 */
public class DownloadJaxb implements Download {

	private static Logger logger = LogManager.getLogger(DownloadJaxb.class);

	private Class<?>[] classType;
	private RecordType record;
    private Object object;

    /**
     *
     * @param classType
     * @param record
     */
	DownloadJaxb(Class<?>[] classType, RecordType record, Object object){
        if(Objects.isNull(classType)) throw new IllegalArgumentException("Class<?>[] requires");
        if(Objects.isNull(record)) throw new IllegalArgumentException("RecordType requires");

		this.classType = classType;
		this.record = record;
		this.object = object;
	}

    @Override
    public void execute(Map<String, String> properties) throws Exception {
        JAXBElement<?> element = (JAXBElement<?>) object;

        EDM edm = getMetadataSchema(IoBuilder.forLogger(DownloadJaxb.class).setLevel(Level.INFO).buildOutputStream(), properties, element);

        if (Objects.nonNull(edm))
            edm.creation(StandardCharsets.UTF_8, true, IoBuilder.forLogger(DownloadJaxb.class).setLevel(Level.INFO).buildOutputStream());

        logger.info(String.format("Donwload item (%s) identifier %s",
                element.getDeclaredType().getSimpleName(), record.getHeader().getIdentifier()));
    }

    @Override
    public void execute(Map<String, String> properties, FormatType formatType) throws Exception {
        JAXBElement<?> element = (JAXBElement<?>) object;

        EDM edm = getMetadataSchema(IoBuilder.forLogger(DownloadJaxb.class).setLevel(Level.INFO).buildOutputStream(), properties, element);

        if (Objects.nonNull(edm))
            edm.creation(StandardCharsets.UTF_8, true, IoBuilder.forLogger(DownloadJaxb.class).setLevel(Level.INFO).buildOutputStream(), formatType);

        logger.info(String.format("Donwload item (%s) identifier %s",
                element.getDeclaredType().getSimpleName(), record.getHeader().getIdentifier()));
    }

    @Override
    public void execute(Path outs, Map<String, String> properties) throws Exception {
        Path filename = Paths.get(outs + File.separator
                + StringUtils.replaceAll(record.getHeader().getIdentifier(), "[^a-zA-Z0-9.-]", "_") + ".rdf");

        if (Files.exists(outs, LinkOption.NOFOLLOW_LINKS)) {
            JAXBElement<?> element = (JAXBElement<?>) object;

            EDM edm = getMetadataSchema(new FileOutputStream(filename.toFile()), properties, element);

            if (Objects.nonNull(edm))
                    edm.creation(StandardCharsets.UTF_8, true, new FileOutputStream(filename.toFile()));

            logger.info(String.format("Donwload item (%s) identifier %s",
                    element.getDeclaredType().getSimpleName(), record.getHeader().getIdentifier()));
        }
    }

    @Override
    public void execute(Path outs, Map<String, String> properties, FormatType formatType) throws Exception {
        Path filename = Paths.get(outs + File.separator
                + StringUtils.replaceAll(record.getHeader().getIdentifier(), "[^a-zA-Z0-9.-]", "_") + "." + formatType.extensions().stream().findFirst().get());

        if (Files.exists(outs, LinkOption.NOFOLLOW_LINKS)) {
            JAXBElement<?> element = (JAXBElement<?>) object;

            EDM edm = getMetadataSchema(new FileOutputStream(filename.toFile()), properties, element);

            if (Objects.nonNull(edm)){
                edm.creation(StandardCharsets.UTF_8, true, new FileOutputStream(filename.toFile()), formatType);
            }


            logger.info(String.format("Donwload item (%s) identifier %s",
                    element.getDeclaredType().getSimpleName(), record.getHeader().getIdentifier()));
        }
    }

    private EDM getMetadataSchema(OutputStream outputStream, Map<String, String> properties, JAXBElement<?> element) throws Exception {
        EDM edm = null;

        if (element.getDeclaredType().equals(A2AType.class))
            edm = new A2A2EDM(record.getHeader().getIdentifier(), (A2AType) element.getValue(), properties);
        else if (element.getDeclaredType().equals(OaiDcType.class))
            new DC2EDM(record.getHeader().getIdentifier(), (OaiDcType) element.getValue(), properties);
        else if (element.getDeclaredType().equals(Memorix.class)) {
            new MEMORIX2EDM(record.getHeader().getIdentifier(), (Memorix) element.getValue(), properties).transformation(outputStream, properties);
        } else if (element.getDeclaredType().equals(Ead.class)) {
            new EAD2EDM(record.getHeader().getIdentifier(), (Ead) element.getValue(), properties).transformation(outputStream, properties);
        } else logger.info(String.format("%s Unknow MetadataType", record.getHeader().getIdentifier()));

        return edm;
    }
}
