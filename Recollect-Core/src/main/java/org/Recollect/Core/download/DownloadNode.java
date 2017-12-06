package org.Recollect.Core.download;

import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.openarchives.oai._2.RecordType;
import org.w3c.dom.Node;

import javax.xml.transform.dom.DOMSource;
import java.io.File;
import java.io.FileOutputStream;
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
	    if(Objects.isNull(xslt)) throw new IllegalArgumentException("xslt requires");
        if(Objects.isNull(record)) throw new IllegalArgumentException("RecordType requires");
		this.xslt = xslt;
		this.record = record;
	}


    @Override
    public void execute(Map<String,String> properties) {
        try {
	        XSLTTransformations transformation =
                new XSLTTransformations(xslt, IoBuilder.forLogger(DownloadNode.class).setLevel(Level.INFO).buildOutputStream(), properties);

            transformation.transformationsFromSource(new DOMSource((Node) record.getMetadata().getAny()));

            if(!transformation.getErrorListener().getErrors().isEmpty()) transformation.getErrorListener().getErrors().forEach(logger::error);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void execute(Path outs, Map<String, String> properties) {
        if(Files.exists(outs, LinkOption.NOFOLLOW_LINKS)){
            try {
                Path filename = Paths.get(
                        outs + File.separator + StringUtils.replaceAll(record.getHeader().getIdentifier(), "[^a-zA-Z0-9.-]", "_") + ".xml");

                XSLTTransformations transformation = new XSLTTransformations(xslt, new FileOutputStream(filename.toFile()), properties);
                transformation.transformationsFromSource(new DOMSource((Node) record.getMetadata().getAny()));

                if(!transformation.getErrorListener().getErrors().isEmpty()) transformation.getErrorListener().getErrors().forEach(logger::error);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }
}
