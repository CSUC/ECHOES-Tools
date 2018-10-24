/**
 * 
 */
package org.Recollect.Core.util;

import java.text.ParseException;
import java.util.Date;


/**
 * @author amartinez
 *
 */
public interface DateProvider {
	 String format(Date date, Granularity granularity);
	 Date parse (String date, Granularity granularity) throws ParseException;
	 Date parse(String date) throws ParseException;
	 String format(Date date);
	 Date now();
}
