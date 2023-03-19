package net.reworlds.controller;

import net.reworlds.TelegramBot;
import net.reworlds.dispatcher.CommandDispatcher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;

@Component
public class UpdateController {
    private TelegramController telegramBot;

    public void registerBot(TelegramController telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null || update.getMessage() == null || update.getMessage().getText() == null ||
                update.getMessage().getDate() < telegramBot.getLaunchTime()) {
            return;
        }

        String[] text = update.getMessage().getText().split("\\s");
        String command = text[0].split("@")[0];
        CommandDispatcher dispatcher = new CommandDispatcher(new CommandController(new ServiceCommands(telegramBot, update, text)));
        try {
            TelegramBot.getLogger().info(update.getMessage().getFrom().getUserName() + " " + update.getMessage().getText());
            dispatcher.executeCommand(command);
        } catch (InvocationTargetException | IllegalAccessException e) {
            TelegramBot.getLogger().warn(e);
        }
    }
}