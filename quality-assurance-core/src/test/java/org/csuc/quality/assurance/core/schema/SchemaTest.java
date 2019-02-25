package org.csuc.quality.assurance.core.schema;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SchemaTest {

    @Test
    public void validate() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File edm = new File(classLoader.getResource("edm.xml").getFile());

        assertNotNull(edm);
        assertTrue(Files.exists(Paths.get(edm.toURI()), LinkOption.NOFOLLOW_LINKS));

        Schema schema = new Schema(new FileInputStream(edm), RDF.class);
        assertTrue(schema.isValid());
    }
}