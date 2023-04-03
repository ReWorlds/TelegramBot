package net.reworlds.utils;

import org.jetbrains.annotations.NotNull;

public final class MessageUtils {
    /**
     * Заменяет последнее совпадение <code>toReplace</code> на <code>replacement</code> в строке <code>string</code>.
     *
     * @param string      Оригинальная строка.
     * @param toReplace   Заменяемая строка.
     * @param replacement Заменяющая строка.
     * @return Измененная строка, где последняя строка <code>toReplace</code> была заменена.
     * на <code>replacement</code> или изначальную строку, если искомая строка отсутствует.
     */
    public static @NotNull String replaceLast(@NotNull String string, @NotNull String toReplace, @NotNull String replacement) {
        int pos = string.lastIndexOf(toReplace);
        return pos > -1 ? string.substring(0, pos) + replacement + string.substring(pos + toReplace.length())
                : string;
    }
}
