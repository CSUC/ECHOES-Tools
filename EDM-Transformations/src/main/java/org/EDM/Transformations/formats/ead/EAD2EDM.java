package org.EDM.Transformations.formats.ead;

import eu.europeana.corelib.definitions.jibx.HasView;
import eu.europeana.corelib.definitions.jibx.RDF;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.xml.transform.stream.StreamSource;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.EDM.Transformations.deserialize.JibxUnMarshall;
import org.EDM.Transformations.serialize.JibxMarshall;

/**
 * @author amartinez
 *
 */
public class EAD2EDM {

    private RDF rdf = null;
    private final OutputStream out;

    public EAD2EDM(String xslt, InputStream ins, Map<String, String> xsltProperties, OutputStream outs) throws IOException, Exception {
        this.out = outs;
        File tempFile = File.createTempFile("Echoes-Tools", ".edm");
        tempFile.deleteOnExit();

        XSLTTransformations xsltTrans = new XSLTTransformations(xslt, new FileOutputStream(tempFile), xsltProperties);
        xsltTrans.transformationsFromSource(new StreamSource(ins));
        
        JibxUnMarshall unmarshall = new JibxUnMarshall(new FileInputStream(tempFile), "UTF-8", RDF.class);  
        rdf = (RDF) unmarshall.getElement();
    }

    public RDF getRDF(){
        return this.rdf;
    }
    
    public void delAdditionalhasViewWebResources() {        
        if (rdf != null) {
            HashSet<String> urisToDelete = new HashSet<>();
            List<RDF.Choice> elements = rdf.getChoiceList();
            // We first collect the resources of edm:hasView to delete and delete them
            elements.forEach(c -> {
                if (c.ifAggregation()) {
                    List<HasView> hasViewList = c.getAggregation().getHasViewList();
                    if (hasViewList != null && hasViewList.size() > 1) {
                        List<HasView> hasViewListToDel = hasViewList.subList(1, hasViewList.size());
                        urisToDelete.addAll(hasViewListToDel.stream().map(
                                h -> h.getResource())
                                .collect(Collectors.toCollection(HashSet<String>::new)));
                        hasViewList.removeAll(hasViewListToDel);
                    }
                }
            });
            // We remove the WebResource(s) which about are in deleted edm:hasView
            rdf.setChoiceList(elements.parallelStream().filter(e -> !e.ifWebResource() || !urisToDelete.contains(e.getWebResource().getAbout()))
                    .collect(Collectors.toCollection(ArrayList<RDF.Choice>::new)));
        }
    }

    public void marshal(Charset encoding, boolean alone) {
        if (Objects.nonNull(this) && !Objects.equals(this, new RDF())) {
            JibxMarshall.marshall(rdf, encoding.toString(), alone, out, RDF.class);
        }
    }

}
