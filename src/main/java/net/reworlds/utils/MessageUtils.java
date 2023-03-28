package net.reworlds.utils;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public class MessageUtils {
    public static SendMessage buildMessage(Update update, String text) {
        var message = update.message();
        var sendMessage = new SendMessage(message.chat().id(), text);
        sendMessage.parseMode(ParseMode.HTML);
        sendMessage.disableWebPagePreview(true);
        sendMessage.replyToMessageId(update.message().messageId());
        return sendMessage;
    }

    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length());
        } else {
            return string;
        }
    }
}
