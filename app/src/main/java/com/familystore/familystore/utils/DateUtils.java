package com.familystore.familystore.utils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date operations, mainly converstion between timestamp and date
 */
public class DateUtils {
    public static String getDateStrFromEpochMilli(long epochMilli) {
        if (epochMilli < 0) {
            return "";
        }
        Instant instant = Instant.ofEpochMilli(epochMilli);
        OffsetDateTime dateTime = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    }
}
