package org.EDM.Transformations.formats.ead;


import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import eu.europeana.corelib.definitions.jibx.HasView;
import eu.europeana.corelib.definitions.jibx.RDF;
import eu.europeana.corelib.definitions.jibx.ResourceType;
import isbn._1_931666_22_9.Ead;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.deserialize.JibxUnMarshall;

/**
 * @author amartinez
 *
 */
public class EAD2EDM implements EDM {

    private static Logger logger = LogManager.getLogger(EAD2EDM.class);

    private Ead type;
    private String identifier;

    private Map<String, String> properties;

    /**
     *
     * @param identifier
     * @param type
     * @param properties
     */
    public EAD2EDM(String identifier, Ead type, Map<String, String> properties) {
        this.identifier = identifier;
        this.type = type;
        this.properties = properties;
    }

    @Override
    public XSLTTransformations transformation(OutputStream out, Map<String, String> xsltProperties) throws Exception {
        String xsl = getClass().getClassLoader().getResource("ead/ead2edm.xsl").toExternalForm();
        return new XSLTTransformations(xsl, out, xsltProperties);
    }

    @Override
    public XSLTTransformations transformation(String xslt, OutputStream out, Map<String, String> xsltProperties) throws Exception {
        return new XSLTTransformations(xslt, out, xsltProperties);
    }

    @Override
    public XSLTTransformations transformation(String xslt) throws Exception {
        return new XSLTTransformations(xslt, IoBuilder.forLogger(EAD2EDM.class).setLevel(Level.INFO).buildOutputStream(), properties);
    }

    @Override
    public void creation() {
        throw new IllegalArgumentException("creation is not valid for EAD2EDM!");
    }

    @Override
    public void creation(Charset encoding, boolean alone, OutputStream outs) {
        throw new IllegalArgumentException("creation is not valid for EAD2EDM!");
    }

    @Override
    public void creation(Charset encoding, boolean alone, Writer writer) {
        throw new IllegalArgumentException("creation is not valid for EAD2EDM!");
    }

    @Override
    public JibxUnMarshall validateSchema(InputStream ins, Charset enc, Class<?> classType) {
        return new JibxUnMarshall(ins, enc, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(InputStream ins, String name, Charset enc, Class<?> classType) {
        return new JibxUnMarshall(ins, name, enc, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(Reader rdr, Class<?> classType) {
        return new JibxUnMarshall(rdr, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(Reader rdr, String name, Class<?> classType) {
        return new JibxUnMarshall(rdr, name, classType);
    }

    @Override
    public void modify(RDF rdf) {
        if(Objects.nonNull(rdf)){
            HashSet<String> urisToDelete = new HashSet<>();
            List<RDF.Choice> elements = rdf.getChoiceList();
            // We first collect the resources of edm:hasView to delete and delete them
            elements.forEach(c -> {
                if (c.ifAggregation()) {
                    List<HasView> hasViewList = c.getAggregation().getHasViewList();
                    if (hasViewList != null && hasViewList.size() > 1) {
                        List<HasView> hasViewListToDel = hasViewList.subList(1, hasViewList.size());
                        urisToDelete.addAll(hasViewListToDel.stream().map(ResourceType::getResource)
                                .collect(Collectors.toCollection(HashSet::new)));
                        hasViewList.removeAll(hasViewListToDel);
                    }
                }
            });
            // We remove the WebResource(s) which about are in deleted edm:hasView
            rdf.setChoiceList(elements.parallelStream().filter(e -> !e.ifWebResource() || !urisToDelete.contains(e.getWebResource().getAbout()))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
    }

}
