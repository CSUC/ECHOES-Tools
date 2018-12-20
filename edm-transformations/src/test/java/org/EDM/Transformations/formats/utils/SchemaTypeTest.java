package org.EDM.Transformations.formats.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class SchemaTypeTest {

    @Test
    public void convert() {
        assertEquals(SchemaType.A2A, SchemaType.convert("A2A"));
        assertEquals(SchemaType.DC, SchemaType.convert("DC"));
        assertEquals(SchemaType.MEMORIX, SchemaType.convert("MEMORIX"));
    }
}