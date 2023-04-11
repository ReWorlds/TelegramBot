package net.reworlds;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import net.reworlds.cache.Cache;
import net.reworlds.command.CommandDispatcher;
import net.reworlds.command.CommandService;
import net.reworlds.command.CommandText;
import net.reworlds.config.JSONManager;
import net.reworlds.database.ConnectionPool;
import net.reworlds.utils.DateFormatter;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpClient;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Statuxia
 */

public final class Bot {
    private static final int launchTime = (int) (System.currentTimeMillis() / 1000L);
    private static final long revolutionWorldsChatId = -1001642226305L;
    private static final String token;
    @Getter
    private static final HttpClient client = HttpClient.newHttpClient();
    @Getter
    private static final JSONObject json;
    @Getter
    private static TelegramBot bot;

    static {
        // log4j time
        System.setProperty("current.date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

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
    private static final Logger logger = Logger.getLogger(Bot.class);

    public static void main(String[] args) {
        bot = new TelegramBot(token);

        // Подключение коллектора устаревших данных из кэша
        Cache.collector();

        // Инициализация подключения к БД.
        ConnectionPool.initConnection();

        logger.info("Launch Time > " + DateFormatter.formatDate(new Date()));

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update == null) {
                    continue;
                }

                Message message = update.message();
                // Игнорируем все, что не является сообщением или было отправлено до запуска бота
                if (message == null || message.date() < launchTime) {
                    continue;
                }

                // Отправка правил пользователю, который вошел в telegram чат RevolutionWorlds
                // return'а нет, потому что если newChatMembers() != null, text() == null
                if (revolutionWorldsChatId == message.chat().id() && message.newChatMembers() != null) {
                    for (User member : message.newChatMembers()) {
                        CommandService.execute(new SendMessage(member.id(), CommandText.rulesMessage)
                                .parseMode(ParseMode.HTML)
                                .disableWebPagePreview(true));
                    }
                }

                if (message.text() == null) {
                    continue;
                }

                var dispatcher = new CommandDispatcher(new CommandService(update.message()));
                try {
                    dispatcher.executeCommand();
                } catch (InvocationTargetException | IllegalAccessException e) {
                    logger.warn(e, e);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
