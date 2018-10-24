package org.csuc.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author amartinez
 */
public class Time {

    /**
     *
     * @param inici {@link LocalDateTime}
     * @param formatter {@link DateTimeFormatter}
     * @return
     */
    public static String duration(LocalDateTime inici, DateTimeFormatter formatter) {
        return LocalTime.MIDNIGHT.plus(Duration.between(inici, LocalDateTime.now())).format(formatter);
    }
}
