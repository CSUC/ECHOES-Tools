package org.EDM.Transformations.formats.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

/**
 *
 * @author amartinez
 *
 */
public class TimeUtil {

    private static Logger logger = LogManager.getLogger(TimeUtil.class);

    /**
     *
     * {@link DateTimeFormatter}: Y
     *
     */
    public static DateTimeFormatter YEAR = new DateTimeFormatterBuilder().appendPattern("y")
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter();

    /**
     *
     * {@link DateTimeFormatter}: MM-y
     *
     */
    public static DateTimeFormatter MONTHYEAR = new DateTimeFormatterBuilder().appendPattern("MM-y")
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter();

    /**
     *
     * {@link DateTimeFormatter}: dd-MM-y
     *
     */
    public static DateTimeFormatter DAYMONTHYEAR = new DateTimeFormatterBuilder().appendPattern("dd-MM-y")
            .parseStrict().toFormatter();

    /**
     *
     * Accept formats
     *
     *
     */
    private static DateTimeFormatter[] formatters = {
            new DateTimeFormatterBuilder().appendPattern("y")
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("'['y']'")
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("'{'y'}'")
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("''y''")
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("\"y\"")
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("'#'y")
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("y-M")
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("M-y")
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("y/M")
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("M/y")
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("y-M-d")
                    .parseStrict()
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("y/M/d")
                    .parseStrict()
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("M-d-y")
                    .parseStrict()
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("M/d/y")
                    .parseStrict()
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("d-M-y")
                    .parseStrict()
                    .toFormatter(),
            new DateTimeFormatterBuilder().appendPattern("d/M/y")
                    .parseStrict()
                    .toFormatter()
    };

    // delimiters

    private char[] BRACES = {'{' , '}'};
    private char[] BRACKETS = {'[' , ']'};
    private char HASHTAG = '#';
    private char QUOTATIONMARK = '\'';
    private char DOUBLEQUOTATIONMARK = '\"';

    /**
     *
     * @param input
     * @return
     */
    public static String format(String input){
        input = StringUtils.deleteWhitespace(input);
        for(DateTimeFormatter formatter : formatters) {
            if(input.length() <= 4 || input.matches("^[{'\"#\\[]")){
                try {
                    logger.debug("old: {}   new:   {}", input, LocalDate.parse(input, formatter).format(YEAR));
                    return LocalDate.parse(input, formatter).format(YEAR);
                } catch (DateTimeParseException e) {}
            }else{
                try {
                    logger.debug("old: {}   new:   {}", input, LocalDate.parse(input, formatter).format(DAYMONTHYEAR));
                    return LocalDate.parse(input, formatter).format(DAYMONTHYEAR);
                } catch (DateTimeParseException e) {}

                try {
                    logger.debug("old: {}   new:   {}", input, LocalDate.parse(input, formatter).format(MONTHYEAR));
                    return LocalDate.parse(input, formatter).format(MONTHYEAR);
                } catch (DateTimeParseException e) {}
            }
        }
        throw new DateTimeParseException(MessageFormat.format("Input {0} could not be parsed", input), input, 0);
    }

    /**
     *
     * @param input
     * @param format
     * @return
     */
    public static String format(String input, DateTimeFormatter format){
        for(DateTimeFormatter formatter : formatters) {
            try {
                LocalDate ld = LocalDate.parse(input, formatter);
                logger.debug("old: {}   new:   {}", input, ld.format(format));
                return ld.format(format);
            } catch (DateTimeParseException e) {}
        }
        throw new DateTimeParseException(MessageFormat.format("Input {0} could not be parsed by {1}", input, format.getClass().getSimpleName()), input, 0);
    }

}
