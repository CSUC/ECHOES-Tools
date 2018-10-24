/**
 * 
 */
package org.csuc.Parser.Core.util;

import java.time.Duration;
import java.time.Instant;
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
}
