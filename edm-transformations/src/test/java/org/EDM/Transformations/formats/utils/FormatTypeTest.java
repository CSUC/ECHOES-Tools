package org.EDM.Transformations.formats.utils;

import org.junit.Test;

import java.util.EnumSet;

public class FormatTypeTest {

    @Test
    public void lang() {
        EnumSet.allOf(FormatType.class)
                .forEach(formatType -> {
                    System.out.println(formatType.lang());
                });
    }

    @Test
    public void extensions() {
        EnumSet.allOf(FormatType.class)
                .forEach(formatType -> {
                    System.out.println(formatType.extensions());
                });
    }
}