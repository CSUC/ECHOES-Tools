package org.edm.transformations.formats.rdf;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.vocabulary.DC_11;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

public class RDFTest {

    private File input_dc;

    @Before
    public void setUp() throws Exception {
        input_dc = new File(getClass().getClassLoader().getResource("rdf/dc.rdf").getFile());
    }

    @Test
    public void test() throws Exception {
        OntModelSpec s = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
        OntModel m = ModelFactory.createOntologyModel(s);

        try (InputStream in = new FileInputStream(input_dc)) {
            RDFParser.create()
                    .source(in)
                    .lang(RDFLanguages.RDFXML)
                    .errorHandler(ErrorHandlerFactory.errorHandlerSimple())
                    .base(null)
                    .parse(m);
        }

        if(!m.getNsPrefixMap().containsValue(DC_11.NS))
            throw new Exception(String.format("%s not RDF-DC Type %s", input_dc, DC_11.getURI()));

        System.out.println(m.getNsPrefixMap());

        try {
            m.listSubjects().forEachRemaining(resource -> {
                System.out.println(String.format("%s", resource.getId().toString()));
                Set<Statement> setProperties = resource.listProperties().toSet();

                if (setProperties.stream().noneMatch(f -> f.getPredicate().getNameSpace().equals(DC_11.NS))) {
                    System.err.println("Object not include " + DC_11.NS + " metadata");
                } else {
                    setProperties.stream().filter(f -> f.getPredicate().getNameSpace().equals(DC_11.NS))
                            .forEach(statement -> System.out.println(String.format("\t%s: %s", statement.getPredicate().getNameSpace(), statement.asTriple().toString())));
                }
            });
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}