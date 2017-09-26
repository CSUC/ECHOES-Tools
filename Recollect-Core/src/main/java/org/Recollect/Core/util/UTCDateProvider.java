/**
 * 
 */
package org.Recollect.Core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;



/**
 * @author amartinez
 *
 */
public class UTCDateProvider implements DateProvider {

	@Override
    public String format(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(date);
    }

    @Override
    public Date now() {
        return new Date();
    }

    @Override
    public String format(Date date, Granularity granularity) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        if (granularity == Granularity.Day)
            format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(date);
    }

    @Override
    public Date parse(String date, Granularity granularity) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        if (granularity == Granularity.Day)
            format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.parse(date);
    }

    @Override
    public Date parse(String string) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return format.parse(string);
        } catch (ParseException e) {
            format = new SimpleDateFormat("yyyy-MM-dd");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return format.parse(string);
        }
    }

}
