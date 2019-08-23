package org.csuc.utils;

import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;

import java.time.*;
import java.util.Date;

/**
 * @author amartinez
 */
public class LocalDateTimeConverter extends TypeConverter implements SimpleValueConverter {

    public LocalDateTimeConverter() {
        // TODO: Add other date/time supported classes here
        // Other java.time classes: LocalDate.class, LocalTime.class
        // Arrays: LocalDateTime[].class, etc
        super(LocalDateTime.class, LocalDate.class, LocalTime.class);
    }

    @Override
    public Object decode(Class<?> targetClass, Object fromDBObject, MappedField optionalExtraInfo) {
        if (fromDBObject == null) {
            return null;
        }

        if (fromDBObject instanceof Date) {
            return ((Date) fromDBObject).toInstant().atZone(ZoneOffset.systemDefault()).toLocalDateTime();
        }

        if (fromDBObject instanceof LocalDateTime) {
            return fromDBObject;
        }

        if (fromDBObject instanceof LocalDate) {
            return fromDBObject;
        }

        if (fromDBObject instanceof LocalTime) {
            return fromDBObject;
        }

        // TODO: decode other types

        throw new IllegalArgumentException(String.format("Cannot decode object of class: %s", fromDBObject.getClass().getName()));
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        }

        if (value instanceof Date) {
            return value;
        }

        if (value instanceof LocalDateTime) {
            ZonedDateTime zoned = ((LocalDateTime) value).atZone(ZoneOffset.systemDefault());
            return Date.from(zoned.toInstant());
        }

        if (value instanceof LocalDate) {
            Instant instant = ((LocalDate) value).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

            return Date.from(instant);
        }

        // TODO: encode other types

        throw new IllegalArgumentException(String.format("Cannot encode object of class: %s", value.getClass().getName()));
    }
}
