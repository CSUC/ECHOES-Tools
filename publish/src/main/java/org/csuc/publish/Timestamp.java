package org.csuc.publish;

import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.core.Quad;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Timestamp {

    private Path path;
    private Set<String> subjects = new HashSet<>();

    public Timestamp(Path path) throws IOException {
        this.path = path;
        read();
    }

    /**
     *
     * @throws IOException
     */
    private void read() throws IOException {
        Files.walk(path)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    Model m = ModelFactory.createDefaultModel() ;
                    m.read(path.toString());

                    StmtIterator iter = m.listStatements();
                    while (iter.hasNext()) {
                        Statement stmt      = iter.nextStatement();  // get next statement
                        Resource subject   = stmt.getSubject();      // get the subject
                        subjects.add(subject.toString());
                    }
                });
    }

    /**
     *
     * @param vg
     * @param predicate
     * @return
     */
    public Stream<Quad> createTimestamp(String vg, String predicate, String object){
        LocalDateTime timestamp = LocalDateTime.now();

        return subjects
                .stream()
                .map(s ->
                        Quad.create(
                                NodeFactory.createURI(vg),
                                //NodeFactory.createURI("vg:example"),
                                NodeFactory.createURI(s),
                                NodeFactory.createURI(predicate),
                                //NodeFactory.createURI("http://purl.org/dc/terms/created"),
                                //NodeFactory.createURI("https://www.w3.org/TR/owl-time/#time:Instant"),
                                NodeFactory.createLiteral(object)
//                                NodeFactory.createLiteral(timestamp.toString())
                        )
                );
//                .forEach(quad -> {
//                    try {
//                        RDFDataMgr.writeQuads(new FileOutputStream("/tmp/subjects", true), Collections.singleton(quad).iterator());
//                    } catch (FileNotFoundException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
    }
}
