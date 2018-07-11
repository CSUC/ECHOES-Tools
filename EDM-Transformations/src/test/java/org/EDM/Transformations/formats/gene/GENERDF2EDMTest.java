package org.EDM.Transformations.formats.gene;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.FactoryEDM;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.csuc.deserialize.JaxbUnmarshal;
import org.csuc.deserialize.JibxUnMarshall;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

public class GENERDF2EDMTest {

    private File xml;
    private File tmp;

    private EDM gene;

    @Before
    public void setUp() throws Exception {
        xml = new File(getClass().getClassLoader().getResource("gene/gene_diba_merge.xml").getFile());
        tmp = Files.createTempFile("generdf_edm", ".xml").toFile();
        assertTrue(xml.exists());

        JaxbUnmarshal jxb = new JaxbUnmarshal(xml, new Class[] { org.w3._1999._02._22_rdf_syntax_ns_.RDF.class });
        assertNotNull(jxb.getObject());
        assertTrue(jxb.isValidating());

        gene = FactoryEDM.createFactory(new GENERDF2EDM((org.w3._1999._02._22_rdf_syntax_ns_.RDF) jxb.getObject(), properties()));
        assertNotNull(gene);

        //tmp.deleteOnExit();
    }

    @Test
    public void testArqueologiaDiba() throws IOException {
        File xmlDiba = new File(getClass().getClassLoader().getResource("gene/diba_arqueologia.xml").getFile());
        File tmpDiba = Files.createTempFile("generdf_diba_arqueologia_edm", ".xml").toFile();
        assertTrue(xmlDiba.exists());

        JaxbUnmarshal jxb = new JaxbUnmarshal(xmlDiba, new Class[] { org.w3._1999._02._22_rdf_syntax_ns_.RDF.class });
        assertNotNull(jxb.getObject());
        assertTrue(jxb.isValidating());

        EDM arqueologia = FactoryEDM.createFactory(new GENERDF2EDM((org.w3._1999._02._22_rdf_syntax_ns_.RDF) jxb.getObject(), properties()));
        assertNotNull(arqueologia);
        FileOutputStream outs = new FileOutputStream(tmpDiba);
        arqueologia.creation(UTF_8, true, outs);
    }

    @Test
    public void testArqueologiaGene() throws IOException {
        File xmlGene = new File(getClass().getClassLoader().getResource("gene/gene_arqueologia.xml").getFile());
        File tmpGene = Files.createTempFile("generdf_gene_arqueologia_edm", ".xml").toFile();
        assertTrue(xmlGene.exists());

        JaxbUnmarshal jxb = new JaxbUnmarshal(xmlGene, new Class[] { org.w3._1999._02._22_rdf_syntax_ns_.RDF.class });
        assertNotNull(jxb.getObject());
        assertTrue(jxb.isValidating());

        EDM arqueologia = FactoryEDM.createFactory(new GENERDF2EDM((org.w3._1999._02._22_rdf_syntax_ns_.RDF) jxb.getObject(), properties()));
        assertNotNull(arqueologia);
        arqueologia.creation();
        FileOutputStream outs = new FileOutputStream(tmpGene);
        arqueologia.creation(UTF_8, true, outs);
    }


    @Test
    public void transformation() throws Exception {
        XSLTTransformations transformations = null;
        try{
            transformations = gene.transformation(null);
            assertNull(transformations);
        }catch(Exception e){}
    }


    @Test
    public void transformation1() throws Exception {
        XSLTTransformations transformations = null;
        try{
            transformations = gene.transformation(null, null, null);
            assertNull(transformations);
        }catch(Exception e){}
    }


    @Test
    public void creation() throws Exception {
        gene.creation();
    }


    @Test
    public void creation1() throws Exception {
        StringWriter writer = new StringWriter();
        gene.creation(UTF_8, true, writer);
        assertTrue(!writer.toString().isEmpty());
    }


    @Test
    public void creation2() throws Exception {
        FileOutputStream outs = new FileOutputStream(tmp);
        gene.creation(UTF_8, true, outs);

        int b  = new FileInputStream(tmp).read();
        assertNotEquals(-1, b);
    }


    @Test
    public void validateSchema() throws Exception {
        StringWriter writer = new StringWriter();
        gene.creation(UTF_8, true, writer);

        Reader reader = new StringReader(writer.toString());
        JibxUnMarshall jibx = gene.validateSchema(reader, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }


    @Test
    public void validateSchema1() throws Exception {
        StringWriter writer = new StringWriter();
        gene.creation(UTF_8, true, writer);

        Reader reader = new StringReader(writer.toString());
        JibxUnMarshall jibx = gene.validateSchema(reader, "name", RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }


    @Test
    public void validateSchema2() throws Exception {
        FileOutputStream outs = new FileOutputStream(tmp);
        gene.creation(UTF_8, true, outs);

        JibxUnMarshall jibx = gene.validateSchema(new FileInputStream(tmp), UTF_8, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }


    @Test
    public void validateSchema3() throws Exception {
        FileOutputStream outs = new FileOutputStream(tmp);
        gene.creation(UTF_8, true, outs);

        JibxUnMarshall jibx = gene.validateSchema(new FileInputStream(tmp), "name", UTF_8, RDF.class);

        assertNotNull(jibx);
        assertNotNull(jibx.getElement());
        assertNull(jibx.getError());
    }

    @Test
    public void modify() throws Exception {

    }

    private Map<String, String> properties() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("edmType", "TEXT");
        properties.put("provider", "GENE");
        properties.put("dataProvider", "DIBA i GENE");
        properties.put("language", "ca_ES");
        properties.put("rights", "Generalitat de Catalunya");

        return properties;
    }
}