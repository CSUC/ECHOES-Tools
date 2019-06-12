/**
 *
 */
package org.csuc.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author amartinez
 *
 */
public class TimeUtils {

    /**
     *
     * @param inici {@link Instant}
     * @param formatter {@link DateTimeFormatter}
     * @return
     */
    public static String duration(Instant inici, DateTimeFormatter formatter) {
        return LocalTime.MIDNIGHT.plus(Duration.between(inici, Instant.now())).format(formatter);
    }

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
