package net.reworlds.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateFormatter {

    public static final String unknownDate = "неизвестно";
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static String formatDate(Date date) {
        String stringFormat = simpleDateFormat.format(date);
        if (stringFormat.contains("1970")) {
            return unknownDate;
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
            return String.format("%d дн. %d ч. %d мин. %d сек.",
                    duration.toDaysPart(), duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
        }
        if (duration.toHoursPart() != 0) {
            return String.format("%d ч. %d мин. %d сек.",
                    duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
        }
        if (duration.toMinutesPart() != 0) {
            return String.format("%d мин. %d сек.",
                    duration.toMinutesPart(), duration.toSecondsPart());
        }
        return String.format("%d сек.", duration.toSecondsPart());
    }


    public static long difference(String date) throws ParseException {
        if (unknownDate.equals(date)) {
            return 8;
        }
        Date firstDate = simpleDateFormat.parse(date);
        Date secondDate = new Date();
        return difference(firstDate, secondDate);
    }

    public static long difference(String date, String date2) throws ParseException {
        if (unknownDate.equals(date) || unknownDate.equals(date2)) {
            return 8;
        }
        Date firstDate = simpleDateFormat.parse(date);
        Date secondDate = simpleDateFormat.parse(date2);
        return difference(firstDate, secondDate);
    }

    public static long difference(Date date1, Date date2) {
        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
