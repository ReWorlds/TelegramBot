package net.reworlds;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.Getter;
import net.reworlds.cache.Cache;
import net.reworlds.controller.CommandController;
import net.reworlds.config.JSONManager;
import net.reworlds.dispatcher.CommandDispatcher;
import net.reworlds.service.ServiceCommands;
import net.reworlds.utils.DateFormatter;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Statuxia
 */

public class Bot {
    private static final int launchTime = (int) (System.currentTimeMillis() / 1000L);
    private static final String token;
    static {
        // log4j time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.setProperty("current.date", dateFormat.format(new Date()));

        // bot token
        try {
            token = JSONManager.of(System.getProperty("user.dir") + "/config/config.json")
                    .getJsonObject().getString("bot-token");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    private static final Logger logger = Logger.getLogger(Bot.class);

    public static void main(String[] args) {
        logger.info("Launch Time > " + DateFormatter.formatDate(new Date()));
        TelegramBot bot = new TelegramBot(token);
        Cache.collector();

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update == null || update.message() == null || update.message().text() == null ||
                        update.message().date() < launchTime) {
                    continue;
                }

                String[] text = update.message().text().split("\\s");
                String command = text[0].split("@")[0];

                CommandDispatcher dispatcher = new CommandDispatcher(new CommandController(new ServiceCommands(bot, update, text)));
                try {
                    dispatcher.executeCommand(command, update);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    logger.warn(e);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }





}
