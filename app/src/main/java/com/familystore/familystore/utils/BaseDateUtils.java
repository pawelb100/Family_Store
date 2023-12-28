package com.familystore.familystore.utils;

import android.text.format.DateUtils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date operations, mainly conversion between timestamp and date
 */
public class BaseDateUtils {
    public static String getDateStrFromEpochMilli(long epochMilli) {
        if (epochMilli < 0) {
            return "";
        }
        Instant instant = Instant.ofEpochMilli(epochMilli);
        OffsetDateTime dateTime = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
    }

    public static String getTimeDifferenceString(long fromMilli, long toMilli) {
        CharSequence result = DateUtils
                .getRelativeTimeSpanString(fromMilli, toMilli, DateUtils.DAY_IN_MILLIS);
        return result.toString();
    }
}
