/**
 * 
 */
package org.Recollect.Core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.Recollect.Core.deserialize.JaxbUnmarshal;
import org.Recollect.Core.serialize.JaxbMarshal;
import org.Recollect.Core.transformation.Transformations;
import org.Recollect.Core.util.StatusCollection;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.openarchives.oai._2.RecordType;
import org.openarchives.oai._2.StatusType;
import org.w3c.dom.Node;


/**
 * @author amartinez
 *
 */
public class DownloadListRecordData extends StatusCollection{

	private static Logger logger = LogManager.getLogger(DownloadListRecordData.class);
	
	private Class<?> classType;
	private Path path;
	private String xslt;
	private Map<String,String> xsltProperties;
	
	private OutputStream out;
	
	public DownloadListRecordData(Path path, Class<?> classType) throws Exception {
		super();		
		this.classType = classType;
		this.path = path;
		
		if(Objects.isNull(classType)) throw new Exception("classType must not be null");
		if(Objects.isNull(path)) throw new Exception("path must not be null");	
	}
	
	public DownloadListRecordData(Class<?> classType) throws Exception {
		super();		
		this.classType = classType;
		if(Objects.isNull(classType)) throw new Exception("classType must not be null");
	}
	
	public DownloadListRecordData() throws Exception {
		super();
	}
	
	public DownloadListRecordData(Path path) throws Exception {
		super();
		this.path = path;
		if(Objects.isNull(path)) throw new Exception("path must not be null");
	}
	
	public DownloadListRecordData(Path path, String xslt) throws Exception {
		super();
		this.path = path;
		this.xslt = xslt;
		if(Objects.isNull(path)) throw new Exception("path must not be null");
		if(Objects.isNull(xslt)) throw new Exception("xslt must not be null");
	}
	
	public DownloadListRecordData(Path path, String xslt, Map<String,String> xsltProperties) throws Exception {
		super();
		this.path = path;
		this.xslt = xslt;
		this.xsltProperties = xsltProperties;
		if(Objects.isNull(path)) throw new Exception("path must not be null");
		if(Objects.isNull(xslt)) throw new Exception("xslt must not be null");
		if(Objects.isNull(xsltProperties)) throw new Exception("xsltProperties must not be null");
	}
	
	public DownloadListRecordData(String xslt, Map<String,String> xsltProperties) throws Exception {
		super();
		this.xslt = xslt;
		this.xsltProperties = xsltProperties;
		if(Objects.isNull(xslt)) throw new Exception("xslt must not be null");
		if(Objects.isNull(xsltProperties)) throw new Exception("xsltProperties must not be null");
	}
	
	public DownloadListRecordData(String xslt) throws Exception {
		super();
		this.xslt = xslt;
		if(Objects.isNull(xslt)) throw new Exception("xslt must not be null");
	}
	
	/**
	 * 
	 * @param records
	 */
	public void executeWithJaxb(Iterator<RecordType> records) {		
		if(Objects.nonNull(records)) {
			Objects.requireNonNull(classType, "classType must not be null");
			records.forEachRemaining(record->{
				recordJaxb(record);
			});		
		}
	}
	
	/**
	 * 
	 * @param record
	 */
	public void executeWithJaxb(RecordType record) {
		Objects.requireNonNull(classType, "classType must not be null");
		recordJaxb(record);
	}
	
	
	/**
	 * 
	 * @param records
	 */
	public void executeWithNode(Iterator<RecordType> records) {
		if(Objects.nonNull(records)) {
			records.forEachRemaining(record->{
				recordNode(record);
			});
		}
	}
	
	/**
	 * 
	 * @param record
	 * @throws Exception
	 */
	public void executeWithNode(RecordType record) throws Exception{
		recordNode(record);
	}
	
	
	/**
	 * 
	 * @param record
	 */
	private void recordJaxb(RecordType record) {
		try {
			JAXBElement<?> jaxbElement =  
					(JAXBElement<?>) new JaxbUnmarshal((Node) record.getMetadata().getAny(), classType).getJaxbElement();
			
		Path file = Paths.get(path + File.separator + StringUtils.replaceAll(record.getHeader().getIdentifier(), "/", "_") + ".xml");
			
			if(!Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
				if(Objects.nonNull(path))	out = new FileOutputStream(file.toFile());
				else out = IoBuilder.forLogger(DownloadListRecordData.class).setLevel(Level.INFO).buildOutputStream();
				
				JaxbMarshal jaxbMarshal = new JaxbMarshal(jaxbElement, classType, out);
				
				jaxbMarshal.marshal(
						"http://www.openarchives.org/OAI/2.0/oai_dc.xsd",
						true,
						true,
						StandardCharsets.UTF_8.toString());
				
				totalDownloadRecord.incrementAndGet();
			}else {
				totalFileAlreadyExistsRecordAndReplace.incrementAndGet();
				throw new IOException(path.toString());
			}
		}catch(Exception e){
			logger.error(e);
		}
		totalReadRecord.incrementAndGet();
	}
	
	/**
	 * 
	 * @param record
	 */
	private void recordNode(RecordType record) {
		if(Objects.nonNull(record)) {
			if(Objects.nonNull(record.getHeader().getStatus()) && record.getHeader().getStatus().equals(StatusType.DELETED)) {
				logger.debug(String.format("%s %s", record.getHeader().getIdentifier(), StatusType.DELETED));
				totalDeletedRecord.incrementAndGet();
				deletedRecord.add(record.getHeader().getIdentifier());
			}else {
				try {
					String result = nodeToString((Node) record.getMetadata().getAny());							
					if(Objects.nonNull(result)) {
						Path file = Paths.get(path + File.separator + StringUtils.replaceAll(record.getHeader().getIdentifier(), "/", "_") + ".xml");						
						if(Objects.nonNull(path)) 	out = new FileOutputStream(file.toFile());
						else out = IoBuilder.forLogger(DownloadListRecordData.class).setLevel(Level.INFO).buildOutputStream();
						
						if(Objects.nonNull(xslt)) {
							Transformations tansformation;
							if(Objects.nonNull(xsltProperties)) {
								tansformation = new Transformations(xslt, out, xsltProperties);
								tansformation.addProperty("identifier", record.getHeader().getIdentifier());								
							}else 	tansformation = new Transformations(xslt, out);							
		    				tansformation.transformationsFromString(result);
						}else	out.write(result.getBytes());
							
	    				if(Objects.nonNull(path))	logger.debug(String.format("File save %s", file));	
	    				totalDownloadRecord.incrementAndGet();
	    				
					}			
				}catch(Exception e) {
					logger.error(String.format("Identifier %s Message %s", record.getHeader().getIdentifier(), e));
				}
			}
			totalReadRecord.incrementAndGet();
		}
	}
	
	private String nodeToString(Node node) {		
		try {			
		    StringWriter sw = new StringWriter();
		    Transformer t = TransformerFactory.newInstance().newTransformer();
		    t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		    t.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.toString());
		    t.setOutputProperty(OutputKeys.INDENT, "yes");
		    t.transform(new DOMSource(node), new StreamResult(sw));
		    return sw.toString();
		} catch (TransformerException te) {
			logger.error(te);
		    return null;
		}	
	}
	
	public String getStatus() {
		return String.format("TotalReadRecords:%s TotalDownloadRecords: %s TotalDeletedRecord: %s TotalFileAlreadyExistsRecordAndReplace: %s\nlistDeletedRecord: %s",
			totalReadRecord.get(), totalDownloadRecord.get(), totalDeletedRecord.getAndIncrement(), totalFileAlreadyExistsRecordAndReplace.get(), deletedRecord);
	}
}
