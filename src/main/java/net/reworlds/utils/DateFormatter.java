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
       return durationToStr(Duration.ofMillis(millis));
    }

    public static String formatSeconds(long seconds) {
        return durationToStr(Duration.ofSeconds(seconds));
    }

    private static String durationToStr(Duration duration) {
        if (duration.toDaysPart() != 0) {
            return String.format("%d дн. %02d ч. %02d мин. %02d сек.",
                    duration.toDaysPart(), duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
        }
        if (duration.toHoursPart() != 0) {
            return String.format("%02d ч. %02d мин. %02d сек.",
                    duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
        }
        if (duration.toMinutesPart() != 0) {
            return String.format("%02d мин. %02d сек.",
                    duration.toMinutesPart(), duration.toSecondsPart());
        }
        return String.format("%02d сек.", duration.toSecondsPart());
    }
}
