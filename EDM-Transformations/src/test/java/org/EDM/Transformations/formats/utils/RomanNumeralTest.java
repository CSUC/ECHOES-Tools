package org.EDM.Transformations.formats.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class RomanNumeralTest {

    private static Logger logger = LogManager.getLogger(RomanNumeralTest.class);

    @Test
    public void testRomanNumeralTest() throws IOException {
//        File tmp = Files.createTempFile("romanNumeral", ".txt").toFile();
//
//        try{
//            Files.write(Paths.get(tmp.toURI()), (Iterable<String>)IntStream.rangeClosed(1,2000).mapToObj(m-> RomanNumeral.toRoman(m))::iterator);
//        }catch ( Exception e){
//            e.printStackTrace();
//        }
//
//        try {
//            Files.readAllLines(Paths.get(tmp.toURI())).forEach(s-> logger.info("{}: {}", s, RomanNumeral.toNumerical(s)));
//            Files.readAllLines(Paths.get(tmp.toURI())).forEach(s->  assertEquals(s, RomanNumeral.format(s)));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        tmp.deleteOnExit();
    }
}