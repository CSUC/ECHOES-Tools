package org.Recollect.Core.download;

import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.openarchives.oai._2.RecordType;
import org.w3c.dom.Node;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * @author amartinez
 *
 */
public class DownloadNode implements Download {

	private static Logger logger = LogManager.getLogger(DownloadNode.class);

	private String xslt;
    private RecordType record;

    /**
     *
     * @param xslt
     * @param record
     */
	DownloadNode(String xslt, RecordType record){
        if(Objects.isNull(record)) throw new IllegalArgumentException("RecordType requires");
		this.xslt = xslt;
		this.record = record;
	}


    @Override
    public void execute(Map<String,String> properties) {
	    if(Objects.nonNull(xslt)){
            try {
                XSLTTransformations transformation =
                        new XSLTTransformations(xslt, IoBuilder.forLogger(DownloadNode.class).setLevel(Level.INFO).buildOutputStream(), properties);

                transformation.transformationsFromSource(new DOMSource((Node) record.getMetadata().getAny()));

                if(!transformation.getErrorListener().getErrors().isEmpty()) transformation.getErrorListener().getErrors().forEach(logger::error);
            } catch (Exception e) {
                logger.error(e);
            }
        }else{
	        try {
                logger.info(NodeToString((Node) record.getMetadata().getAny()));
            }catch(Exception e){
	            logger.error(e);
            }
        }
    }

    @Override
    public void execute(Path outs, Map<String, String> properties) {
        if(Files.exists(outs, LinkOption.NOFOLLOW_LINKS)){
            Path filename = Paths.get(
                    outs + File.separator + StringUtils.replaceAll(record.getHeader().getIdentifier(), "[^a-zA-Z0-9.-]", "_") + ".xml");

            if(Objects.nonNull(xslt)){
                try {
                    XSLTTransformations transformation = new XSLTTransformations(xslt, new FileOutputStream(filename.toFile()), properties);
                    transformation.transformationsFromSource(new DOMSource((Node) record.getMetadata().getAny()));

                    if(!transformation.getErrorListener().getErrors().isEmpty()) transformation.getErrorListener().getErrors().forEach(logger::error);
                } catch (Exception e) {
                    logger.error(e);
                }
            }else{
                try {
                    String result = NodeToString((Node) record.getMetadata().getAny());

                    try (PrintWriter p = new PrintWriter(new FileOutputStream(filename.toFile()))) {
                        p.println(result);
                    } catch (FileNotFoundException e) {
                        logger.error(e);
                    }
                }catch(Exception e){
                    logger.error(e);
                }
            }
        }
    }

    /**
     *
     * @param node
     * @return
     * @throws TransformerException
     */
    private String NodeToString(Node node) throws TransformerException {
            StringWriter sw = new StringWriter();
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            t.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.toString());
            t.setOutputProperty(OutputKeys.INDENT, "no");
            t.transform(new DOMSource(node), new StreamResult(sw));

            return sw.toString();
    }

}
