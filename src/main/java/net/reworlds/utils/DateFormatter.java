package net.reworlds.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateFormatter {

    public static final String unknownDate = "неизвестно";
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    /**
     * Позволяет отформатировать дату по паттерну "<code>dd.MM.yyyy HH:mm:ss</code>".
     * В случае, если в строке присутствует 1970, возвращает "<code>неизвестно</code>".
     *
     * @param date дата на форматирование.
     * @return отформатированная дата в виде строки или "<code>неизвестно</code>", в случае,
     * если в строке присутствует 1970.
     */
    public static @NotNull String formatDate(@NotNull Date date) {
        String stringFormat = simpleDateFormat.format(date);
        if (stringFormat.contains("1970")) {
            return unknownDate;
        }
        return stringFormat;
    }


    /**
     * Позволяет отформатировать миллисекунды в строку в формате количества дней, часов, минут и секунд.
     * Возможные возвращаемые значения:<br>
     * "<code>%d дн. %d ч. %d мин. %d сек.</code>"<br>
     * "<code>%d ч. %d мин. %d сек.</code>"<br>
     * "<code>%d мин. %d сек.</code>"<br>
     * "<code>%d сек.</code>"
     *
     * @param millis миллисекунды, которые необходимо отформатировать.
     * @return отформатированная строка.
     */
    public static @NotNull String formatMillis(long millis) {
        return durationToStr(Duration.ofMillis(millis));
    }

    /**
     * Позволяет отформатировать секунды в строку в формате количества дней, часов, минут и секунд.
     * Возможные возвращаемые значения:<br>
     * "<code>%d дн. %d ч. %d мин. %d сек.</code>"<br>
     * "<code>%d ч. %d мин. %d сек.</code>"<br>
     * "<code>%d мин. %d сек.</code>"<br>
     * "<code>%d сек.</code>"
     *
     * @param seconds секунды, которые необходимо отформатировать.
     * @return отформатированная строка.
     */
    public static @NotNull String formatSeconds(long seconds) {
        return durationToStr(Duration.ofSeconds(seconds));
    }

    /**
     * Позволяет отформатировать время в строку в формате количества дней, часов, минут и секунд.
     * Возможные возвращаемые значения:<br>
     * "<code>%d дн. %d ч. %d мин. %d сек.</code>"<br>
     * "<code>%d ч. %d мин. %d сек.</code>"<br>
     * "<code>%d мин. %d сек.</code>"<br>
     * "<code>%d сек.</code>"
     *
     * @param duration значение времени.
     * @return отформатированная строка.
     */
    private static @NotNull String durationToStr(@Nullable Duration duration) {
        return duration == null ? "0 сек."
                : duration.toDaysPart() != 0 ? String.format("%d дн. %d ч. %d мин. %d сек.",
                duration.toDaysPart(), duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart())
                : duration.toHoursPart() != 0 ? String.format("%d ч. %d мин. %d сек.",
                duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart())
                : duration.toMinutesPart() != 0 ? String.format("%d мин. %d сек.", duration.toMinutesPart(),
                duration.toSecondsPart())
                : String.format("%d сек.", duration.toSecondsPart());
    }

    /**
     * Позволяет получить разницу между передаваемой датой и нынешней в днях.
     *
     * @param date строка в формате "<code>dd.MM.yyyy HH:mm:ss</code>".
     * @return разницу в днях или -1, если строка - "<code>неизвестно</code>"
     */
    public static long difference(@NotNull String date) throws ParseException {
        if (unknownDate.equals(date)) {
            return -1;
        }
        Date firstDate = simpleDateFormat.parse(date);
        Date secondDate = new Date();
        return difference(firstDate, secondDate);
    }

    /**
     * Позволяет получить разницу между передаваемыми датами в днях.
     *
     * @param date  строка в формате "<code>dd.MM.yyyy HH:mm:ss</code>".
     * @param date2 строка в формате "<code>dd.MM.yyyy HH:mm:ss</code>".
     * @return разницу в днях или -1, если строка - "<code>неизвестно</code>".
     */
    public static long difference(String date, String date2) throws ParseException {
        if (unknownDate.equals(date) || unknownDate.equals(date2)) {
            return -1;
        }
        Date firstDate = simpleDateFormat.parse(date);
        Date secondDate = simpleDateFormat.parse(date2);
        return difference(firstDate, secondDate);
    }

    /**
     * Позволяет получить разницу между передаваемыми датами в днях.
     *
     * @param date1 объект класса <code>Date</code>.
     * @param date2 объект класса <code>Date</code>.
     * @return разницу в днях или -1, если строка - "<code>неизвестно</code>".
     * @see Date
     */
    public static long difference(Date date1, Date date2) {
        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
