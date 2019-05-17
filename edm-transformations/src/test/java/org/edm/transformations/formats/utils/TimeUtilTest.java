package org.edm.transformations.formats.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.text.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeUtilTest {

    private static Logger logger = LogManager.getLogger(TimeUtil.class);

    private AtomicInteger total = new AtomicInteger(0);
    private AtomicInteger validLocalDate = new AtomicInteger(0);
    private AtomicInteger invalidLocalDate = new AtomicInteger(0);
    private AtomicInteger validRoman = new AtomicInteger(0);

    private NumberFormat nf = NumberFormat.getInstance();

    @Test
    public void format() throws IOException {
//        Files.readAllLines(Paths.get(getClass().getClassLoader().getResource("utils/dates").getFile())).stream().forEach(s->{
//            if(RomanNumeral.isRoman(s)) validRoman.getAndIncrement();
//            else{
//                try{
//                    TimeUtil.format(s);
//                    validLocalDate.getAndIncrement();
//                }catch (Exception e) {
//                    invalidLocalDate.getAndIncrement();
//                }
//            }
//            total.getAndIncrement();
//        });
//
//        logger.info("Total                          : {}", nf.format(total.get()));
//        logger.info("Total valid format date        : {}", nf.format(validLocalDate.get()));
//        logger.info("Total invalid format date      : {}", nf.format(invalidLocalDate.get()));
//        logger.info("Total valid format Roman       : {}", nf.format(validRoman.get()));
//        logger.info("Total valid                    : {}    => {}%", nf.format(validLocalDate.get()+validRoman.get()), Math.round((validLocalDate.get()+validRoman.get()) * 100 / total.get() * 100.0) / 100.0);
    }
}