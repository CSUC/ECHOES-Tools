package org.edm.transformations.formats.utils;

import org.csuc.util.FormatType;
import org.junit.Test;

import java.util.EnumSet;

import static org.junit.Assert.assertEquals;

public class FormatTypeTest {

    @Test
    public void lang() {
        EnumSet.allOf(FormatType.class)
                .forEach(formatType -> {
                    System.out.println(formatType.lang());
                });
        assertEquals(FormatType.RDFXML, FormatType.convert("RDFXML"));
    }

    @Test
    public void extensions() {
        EnumSet.allOf(FormatType.class)
                .forEach(formatType -> {
                    System.out.println(formatType.extensions());
                });
    }
}