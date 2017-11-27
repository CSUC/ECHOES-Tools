/**
 * 
 */
package org.Recollect.Core.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.xml.bind.JAXBElement;

import org.EDM.Transformations.formats.a2a.A2A2EDM;
import org.EDM.Transformations.formats.dc.DC2EDM;
import org.Recollect.Core.util.StatusCollection;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2.StatusType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import nl.mindbus.a2a.A2AType;

/**
 * @author amartinez
 *
 */
public class DownloadJaxb extends StatusCollection {

	private static Logger logger = LogManager.getLogger(DownloadJaxb.class);

	private Class<?>[] classType;
	private Path path;

	private OutputStream out;

	public DownloadJaxb(Path path, Class<?>[] classType) throws Exception {
		super();
		if (Objects.isNull(classType))
			throw new Exception("classType must not be null");
		if (Objects.isNull(path))
			throw new Exception("path must not be null");

		this.classType = classType;
		this.path = path;
	}

	public DownloadJaxb(Class<?>[] classType) throws Exception {
		super();
		if (Objects.isNull(classType))
			throw new Exception("classType must not be null");

		this.classType = classType;
	}

	/**
	 * 
	 * @param records
	 */
	public void execute(Iterator<RecordType> records) {
		if (Objects.nonNull(records)) {
			Objects.requireNonNull(classType, "classType must not be null");
			records.forEachRemaining(record -> {
				recordJaxb(record, null);
			});
		}
	}

	public void execute(RecordType record) {
		recordJaxb(record, null);
	}

	/**
	 * 
	 * @param records
	 */
	public void execute(Iterator<RecordType> records, Map<String, String> properties) {
		if (Objects.nonNull(records)) {
			Objects.requireNonNull(classType, "classType must not be null");
			records.forEachRemaining(record -> {
				recordJaxb(record, properties);
			});
		}
	}

	public void execute(RecordType record, Map<String, String> properties) {
		recordJaxb(record, properties);
	}

	private void recordJaxb(RecordType record, Map<String, String> properties) {
		if (Objects.nonNull(record)) {
			if (Objects.nonNull(record.getHeader().getStatus())
					&& record.getHeader().getStatus().equals(StatusType.DELETED)) {
				logger.debug(String.format("%s %s", record.getHeader().getIdentifier(), StatusType.DELETED));
				totalDeletedRecord.incrementAndGet();
				deletedRecord.add(record.getHeader().getIdentifier());
			} else {
				try {
					if (record.getMetadata().getAny() instanceof JAXBElement<?>) {
						JAXBElement<?> element = (JAXBElement<?>) record.getMetadata().getAny();
						
						setOutputStream(record);
						if (element.getDeclaredType().equals(A2AType.class)) {
							new A2A2EDM(record.getHeader().getIdentifier(), (A2AType) element.getValue(), properties,
									out).marshal(StandardCharsets.UTF_8, true);
							totalDownloadRecord.incrementAndGet();
						} else if (element.getDeclaredType().equals(OaiDcType.class)) {
							new DC2EDM(record.getHeader().getIdentifier(), (OaiDcType) element.getValue(), properties,
									out).marshal(StandardCharsets.UTF_8, true);
						} else
							logger.info(String.format("%s Unknow MetadataType", record.getHeader().getIdentifier()));
					}
				} catch (FileNotFoundException exception) {
					logger.error(
							String.format("Identifier %s Message %s", record.getHeader().getIdentifier(), exception));
				}
				totalReadRecord.incrementAndGet();
			}
		}
	}

	private void setOutputStream(RecordType record) throws FileNotFoundException {
		Path file = Paths.get(
				path + File.separator + StringUtils.replaceAll(record.getHeader().getIdentifier(), "/", "_") + ".xml");
		if (Objects.nonNull(path))
			out = new FileOutputStream(file.toFile());
		else
			out = IoBuilder.forLogger(DownloadJaxb.class).setLevel(Level.INFO).buildOutputStream();
	}

	public String getStatus() {
		return String.format(
				"TotalReadRecords:%s TotalDownloadRecords: %s TotalDeletedRecord: %s TotalFileAlreadyExistsRecordAndReplace: %s\nlistDeletedRecord: %s",
				totalReadRecord.get(), totalDownloadRecord.get(), totalDeletedRecord.getAndIncrement(),
				totalFileAlreadyExistsRecordAndReplace.get(), deletedRecord);
	}
}
