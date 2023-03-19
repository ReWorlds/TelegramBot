package net.reworlds.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class DateFormatter {
    public static String formatDate(Date date) {
        String pattern = "dd.MM.yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String stringFormat = simpleDateFormat.format(date);
        if (stringFormat.contains("1970")) {
            return "неизвестно";
        }
        return stringFormat;
    }

    public static String formatMillis(long millis) {
        Duration duration = Duration.ofMillis(millis);
        return String.format("%d дн. %02d ч. %02d мин. %02d сек.",
                duration.toDaysPart(), duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
    }
}
