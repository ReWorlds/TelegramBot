package net.reworlds.controller;

import net.reworlds.dispatcher.CommandDispatcher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

@Component
public class UpdateController {
    private TelegramController telegramBot;

    public void registerBot(TelegramController telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            Logger.getLogger("UpdateController.processUpdate")
                    .warning("Received update is null");
            return;
        }

        if (update.getMessage() == null) {
            Logger.getLogger("UpdateController.processUpdate")
                    .warning("Unsupported message type is received: " + update);
            return;
        }

        if (update.getMessage().getDate() < telegramBot.getLaunchTime()) {
            return;
        }

        String[] text = update.getMessage().getText().split("\\s");
        String command = text[0].split("@")[0];
        CommandDispatcher dispatcher = new CommandDispatcher(new CommandController(new ServiceCommands(telegramBot, update, text)));
        try {

            dispatcher.executeCommand(command);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
//        SendMessage s = MessageUtils.buildMessage(update, "1");
//        telegramBot.sendMessage(s);
        // Something message.
    }
}