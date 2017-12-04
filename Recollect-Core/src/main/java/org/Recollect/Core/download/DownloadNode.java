package org.Recollect.Core.download;

import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.Recollect.Core.util.Garbage;
import org.Recollect.Core.util.StatusCollection;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2.StatusType;
import org.w3c.dom.Node;

import javax.xml.transform.dom.DOMSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author amartinez
 *
 */
public class DownloadNode extends StatusCollection {

	private static Logger logger = LogManager.getLogger(DownloadNode.class);

	private Path path;
	private String xslt;
	private Map<String, String> xsltProperties;

    private int garbage = 500;

	private AtomicInteger iter = new AtomicInteger(0);

	public DownloadNode(Path path, String xslt) throws Exception {
		super();
		this.path = path;
		this.xslt = xslt;

		if (Objects.isNull(path))
			throw new Exception("path must not be null");
		if (Objects.isNull(xslt))
			throw new Exception("xslt must not be null");
	}

	public DownloadNode(Path path, String xslt, Map<String, String> xsltProperties) throws Exception {
		super();
		if (Objects.isNull(path))
			throw new Exception("path must not be null");
		if (Objects.isNull(xslt))
			throw new Exception("xslt must not be null");
		if (Objects.isNull(xsltProperties))
			throw new Exception("xsltProperties must not be null");

		this.path = path;
		this.xslt = xslt;
		this.xsltProperties = xsltProperties;
	}

	public DownloadNode(String xslt, Map<String, String> xsltProperties) throws Exception {
		super();
		if (Objects.isNull(xslt))
			throw new Exception("xslt must not be null");
		if (Objects.isNull(xsltProperties))
			throw new Exception("xsltProperties must not be null");

		this.xslt = xslt;
		this.xsltProperties = xsltProperties;
	}

	public DownloadNode(String xslt) throws Exception {
		super();
		if (Objects.isNull(xslt))
			throw new Exception("xslt must not be null");

		this.xslt = xslt;
	}

	/**
	 * 
	 * @param records
	 */
	public void execute(Iterator<RecordType> records) {
		if (Objects.nonNull(records)) {
			records.forEachRemaining(record -> {				
				if (iter.incrementAndGet() % garbage == 0)
					Garbage.gc();
				else
					recordNode(record);
			});
		}
	}

	/**
	 * 
	 * @param record
	 * @throws Exception
	 */
	public void execute(RecordType record) throws Exception {
		recordNode(record);
	}

	/**
	 * 
	 * @param record
	 */
	private void recordNode(RecordType record) {
		if (Objects.nonNull(record)) {
			if (Objects.nonNull(record.getHeader().getStatus())
					&& record.getHeader().getStatus().equals(StatusType.DELETED)) {
				logger.debug(String.format("%s %s", record.getHeader().getIdentifier(), StatusType.DELETED));
				totalDeletedRecord.incrementAndGet();
				deletedRecord.add(record.getHeader().getIdentifier());
			} else {
				try {
                    Path file = Paths.get(path + File.separator
							+ StringUtils.replaceAll(record.getHeader().getIdentifier(), "/", "_") + ".xml");
                    OutputStream out;
                    if (Objects.nonNull(path))
						out = new FileOutputStream(file.toFile());
					else
						out = IoBuilder.forLogger(DownloadNode.class).setLevel(Level.INFO).buildOutputStream();

					XSLTTransformations transformation;
					if (Objects.nonNull(xsltProperties)) {
                           transformation = new XSLTTransformations(xslt, out, xsltProperties);
                           transformation.addProperty("identifier", record.getHeader().getIdentifier());
					} else
                        transformation = new XSLTTransformations(xslt, out);
                    transformation.transformationsFromSource(new DOMSource((Node) record.getMetadata().getAny()));

					if (Objects.nonNull(path))
						logger.debug(String.format("File save %s", file));
					totalDownloadRecord.incrementAndGet();
				} catch (Exception e) {
					logger.error(String.format("Identifier %s Message %s", record.getHeader().getIdentifier(), e));
				}
			}
			totalReadRecord.incrementAndGet();
		}
	}

	public String getStatus() {
		return String.format(
				"TotalReadRecords:%s TotalDownloadRecords: %s TotalDeletedRecord: %s TotalFileAlreadyExistsRecordAndReplace: %s\nlistDeletedRecord: %s",
				totalReadRecord.get(), totalDownloadRecord.get(), totalDeletedRecord.getAndIncrement(),
				totalFileAlreadyExistsRecordAndReplace.get(), deletedRecord);
	}
}
