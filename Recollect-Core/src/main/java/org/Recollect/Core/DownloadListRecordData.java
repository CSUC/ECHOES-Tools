/**
 * 
 */
package org.Recollect.Core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	
	public DownloadListRecordData(Path path, Class<?> classType) {
		super();		
		this.classType = classType;
		this.path = path;
		Objects.requireNonNull(classType, "classType must not be null");
		Objects.requireNonNull(path, "path must not be null");		
	}
	
	public DownloadListRecordData(Path path) {
		super();
		this.path = path;
		Objects.requireNonNull(path, "records must not be null");
		Objects.requireNonNull(GlobalAttributes.XSLT, "xslt must not be null");
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
				FileOutputStream out = new FileOutputStream(file.toFile());
				
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
			}else {
				try {
					String result = nodeToString((Node) record.getMetadata().getAny());							
					if(Objects.nonNull(result)) {
						Path file = Paths.get(path + File.separator + StringUtils.replaceAll(record.getHeader().getIdentifier(), "/", "_") + ".xml");
						
						Transformations tansformation = new Transformations(GlobalAttributes.XSLT, file);
	    					
						Map<String,String> parameters = new HashMap<String,String>();
						parameters.put("identifier", record.getHeader().getIdentifier());
						parameters.put("edmType", "IMAGE");
						parameters.put("dataProvider", "Erfgoed");
						
	    				tansformation.transformationsFromString(result, parameters);
							
						logger.debug(String.format("File save %s", file));	
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
		    t.setOutputProperty(OutputKeys.INDENT, "no");
		    t.transform(new DOMSource(node), new StreamResult(sw));
		    return sw.toString();
		} catch (TransformerException te) {
			logger.error(te);
		    return null;
		}	
	}
	
	public String getStatus() {
		return String.format("TotalReadRecords:%s TotalDownloadRecords: %s TotalDeletedRecord: %s TotalFileAlreadyExistsRecordAndReplace: %s",
				totalReadRecord.get(), totalDownloadRecord.get(), totalDeletedRecord.getAndIncrement(), totalFileAlreadyExistsRecordAndReplace.get());
	}
}
