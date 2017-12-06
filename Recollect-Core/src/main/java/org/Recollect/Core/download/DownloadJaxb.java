/**
 * 
 */
package org.Recollect.Core.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import javax.xml.bind.JAXBElement;

import org.EDM.Transformations.formats.a2a.A2A2EDM;
import org.EDM.Transformations.formats.dc.DC2EDM;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import nl.mindbus.a2a.A2AType;

/**
 * @author amartinez
 *
 */
public class DownloadJaxb implements Download {

	private static Logger logger = LogManager.getLogger(DownloadJaxb.class);

	private Class<?>[] classType;
	private RecordType record;

    /**
     *
     * @param classType
     * @param record
     */
	DownloadJaxb(Class<?>[] classType, RecordType record){
        if(Objects.isNull(classType)) throw new IllegalArgumentException("Class<?>[] requires");
        if(Objects.isNull(record)) throw new IllegalArgumentException("RecordType requires");

		this.classType = classType;
		this.record = record;
	}


    @Override
    public void execute(Map<String, String> properties) {
        JAXBElement<?> element = (JAXBElement<?>) record.getMetadata().getAny();

        logger.info(String.format("Donwload item (%s) identifier %s",
                element.getDeclaredType().getSimpleName(), record.getHeader().getIdentifier()));

        if (element.getDeclaredType().equals(A2AType.class)) {
            new A2A2EDM(record.getHeader().getIdentifier(), (A2AType) element.getValue(), properties)
                    .creation(StandardCharsets.UTF_8, true, IoBuilder.forLogger(DownloadJaxb.class).setLevel(Level.INFO).buildOutputStream());

        } else if (element.getDeclaredType().equals(OaiDcType.class)) {
            new DC2EDM(record.getHeader().getIdentifier(), (OaiDcType) element.getValue(), properties)
                    .creation(StandardCharsets.UTF_8, true, IoBuilder.forLogger(DownloadJaxb.class).setLevel(Level.INFO).buildOutputStream());
        } else
            logger.info(String.format("%s Unknow MetadataType", record.getHeader().getIdentifier()));
    }

    @Override
    public void execute(Path outs, Map<String, String> properties) {
        Path filename = Paths.get(outs + File.separator
                + StringUtils.replaceAll(record.getHeader().getIdentifier(), "[^a-zA-Z0-9.-]", "_") + ".xml");

	      if(Files.exists(outs, LinkOption.NOFOLLOW_LINKS)){
            try {
                JAXBElement<?> element = (JAXBElement<?>) record.getMetadata().getAny();

                logger.info(String.format("Donwload item (%s) identifier %s",
                        element.getDeclaredType().getSimpleName(), record.getHeader().getIdentifier()));

                if (element.getDeclaredType().equals(A2AType.class)) {
                    new A2A2EDM(record.getHeader().getIdentifier(), (A2AType) element.getValue(), properties)
                            .creation(StandardCharsets.UTF_8, true, new FileOutputStream(filename.toFile()));

                } else if (element.getDeclaredType().equals(OaiDcType.class)) {
                    new DC2EDM(record.getHeader().getIdentifier(), (OaiDcType) element.getValue(), properties)
                            .creation(StandardCharsets.UTF_8, true, new FileOutputStream(filename.toFile()));
                } else
                    logger.info(String.format("%s Unknow MetadataType", record.getHeader().getIdentifier()));
            } catch (FileNotFoundException e) {
                logger.error(e);
            }
        }
    }
}
