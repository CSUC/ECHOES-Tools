package org.Recollect.Core.download;

import org.EDM.Transformations.formats.utils.FormatType;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.openarchives.oai._2.RecordType;
import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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
    private DOMSource domSource;

    /**
     *
     * @param xslt
     * @param record
     */
	DownloadNode(String xslt, RecordType record, DOMSource domSource){
        if(Objects.isNull(record)) throw new IllegalArgumentException("RecordType requires");
		this.xslt = xslt;
		this.record = record;
		this.domSource = domSource;
	}


    @Override
    public void execute(Map<String,String> properties) throws Exception {
        if (Objects.nonNull(xslt)) {
            XSLTTransformations transformation =
                    new XSLTTransformations(xslt, IoBuilder.forLogger(DownloadNode.class).setLevel(Level.INFO).buildOutputStream(), properties);

            transformation.transformationsFromSource(domSource);

            if (!transformation.getErrorListener().getErrors().isEmpty())
                transformation.getErrorListener().getErrors().forEach(logger::error);
        } else {
            logger.info(NodeToString((Node) record.getMetadata().getAny()));
        }
    }

    @Override
    public void execute(Map<String, String> properties, FormatType formatType) throws Exception {
        execute(properties);
    }

    @Override
    public void execute(Path outs, Map<String, String> properties) throws Exception {
        if (Files.exists(outs, LinkOption.NOFOLLOW_LINKS)) {
            Path filename = Paths.get(
                    outs + File.separator + StringUtils.replaceAll(record.getHeader().getIdentifier(), "[^a-zA-Z0-9.-]", "_") + ".xml");

            if (Objects.nonNull(xslt)) {
                XSLTTransformations transformation = new XSLTTransformations(xslt, new FileOutputStream(filename.toFile()), properties);
                transformation.transformationsFromSource(domSource);

                if (!transformation.getErrorListener().getErrors().isEmpty())
                    transformation.getErrorListener().getErrors().forEach(logger::error);
            } else {
                String result = NodeToString((Node) record.getMetadata().getAny());

                try (PrintWriter p = new PrintWriter(new FileOutputStream(filename.toFile()))) {
                    p.println(result);
                } catch (FileNotFoundException e) {
                    logger.error(e);
                }
            }
        }
    }

    @Override
    public void execute(Path outs, Map<String, String> properties, FormatType formatType) throws Exception {
        execute(outs, properties);
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
