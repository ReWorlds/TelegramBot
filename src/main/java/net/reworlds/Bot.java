package net.reworlds;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Getter;
import net.reworlds.cache.Cache;
import net.reworlds.config.CommandText;
import net.reworlds.config.JSONManager;
import net.reworlds.controller.CommandController;
import net.reworlds.dispatcher.CommandDispatcher;
import net.reworlds.service.ServiceCommands;
import net.reworlds.utils.DateFormatter;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpClient;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Statuxia
 */

public class Bot {
    private static final int launchTime = (int) (System.currentTimeMillis() / 1000L);
    @Getter
    private static final JSONObject json;
    private static final String token;
    private static final long revolutionWorldsChatId = -1001642226305L;

    static {
        // log4j time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.setProperty("current.date", dateFormat.format(new Date()));

        // bot token
        try {
            json = JSONManager.of(System.getProperty("user.dir") + "/config/config.json")
                    .getJsonObject();
            token = json.getString("bot-token");
            json.remove("bot-token");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    private static final HttpClient client = HttpClient.newHttpClient();

    @Getter
    private static final Logger logger = Logger.getLogger(Bot.class);

    public static void main(String[] args) {
        logger.info("Launch Time > " + DateFormatter.formatDate(new Date()));
        TelegramBot bot = new TelegramBot(token);
        Cache.collector();


        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update == null) {
                    continue;
                }

                Message message = update.message();
                if (message == null || message.date() < launchTime) {
                    continue;
                }

                if (revolutionWorldsChatId == message.chat().id() && message.newChatMembers() != null) {
                    for (User member : message.newChatMembers()) {
                        var sendMessage = new SendMessage(member.id(), CommandText.rulesMessage);
                        sendMessage.parseMode(ParseMode.HTML);
                        sendMessage.disableWebPagePreview(true);
                        bot.execute(sendMessage, new Callback<SendMessage, SendResponse>() {
                            @Override
                            public void onResponse(SendMessage request, SendResponse response) {

                            }

                            @Override
                            public void onFailure(SendMessage request, IOException e) {

                            }
                        });
                    }
                }

                if (message.text() == null) {
                    continue;
                }

                String[] text = message.text().split("\\s");
                String command = text[0].split("@")[0];

                CommandDispatcher dispatcher = new CommandDispatcher(new CommandController(new ServiceCommands(bot, update, text)));
                try {
                    dispatcher.executeCommand(command, update);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    logger.warn(e, e);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }
}
