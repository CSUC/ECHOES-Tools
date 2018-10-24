package org.csuc.deserialize;

import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.junit.Before;
import org.junit.Test;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import java.io.File;

import static org.junit.Assert.*;

public class JaxbUnmarshalTest {

    private File A2A;
    private File DC;
    private File EAD;
    private File MEMORIX;

    @Before
    public void setUp() {
        ClassLoader classLoader = getClass().getClassLoader();
        A2A = new File(classLoader.getResource("a2a.xml").getFile());
        DC = new File(classLoader.getResource("dc.xml").getFile());
        EAD = new File(classLoader.getResource("ead.xml").getFile());
        MEMORIX = new File(classLoader.getResource("memorix.xml").getFile());
    }

    @Test
    public void getObject() {
        assertNotNull(new JaxbUnmarshal(A2A, new Class[]{A2AType.class}).getObject());
        assertNotNull(new JaxbUnmarshal(DC, new Class[]{OaiDcType.class}).getObject());
        assertNotNull(new JaxbUnmarshal(EAD, new Class[]{Ead.class}).getObject());
        assertNotNull(new JaxbUnmarshal(MEMORIX, new Class[]{Memorix.class}).getObject());

        assertNull(new JaxbUnmarshal(DC, new Class[]{A2AType.class}).getObject());
        assertNull(new JaxbUnmarshal(A2A, new Class[]{OaiDcType.class}).getObject());
    }

    @Test
    public void isValidating() {
        assertTrue(new JaxbUnmarshal(A2A, new Class[]{A2AType.class}).isValidating());
        assertTrue(new JaxbUnmarshal(DC, new Class[]{OaiDcType.class}).isValidating());
        assertTrue(new JaxbUnmarshal(EAD, new Class[]{Ead.class}).isValidating());
        assertTrue(new JaxbUnmarshal(MEMORIX, new Class[]{Memorix.class}).isValidating());


        assertFalse(new JaxbUnmarshal(DC, new Class[]{A2AType.class}).isValidating());
        assertFalse(new JaxbUnmarshal(EAD, new Class[]{A2AType.class}).isValidating());
        assertFalse(new JaxbUnmarshal(MEMORIX, new Class[]{A2AType.class}).isValidating());
    }
}