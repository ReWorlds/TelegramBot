package net.reworlds.command;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.ChatPermissions;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.RestrictChatMember;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Getter;
import net.reworlds.Bot;
import net.reworlds.cache.Cache;
import net.reworlds.cache.Cache.Status;
import net.reworlds.cache.Metrics;
import net.reworlds.cache.Player;
import net.reworlds.cache.Release;
import net.reworlds.database.ConnectionPool;
import net.reworlds.utils.DateFormatter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public final class CommandService {
    private final String[] args;
    @Getter
    private final Message message;
    @Getter
    private final String command;

    /**
     * Конструктор объекта, создаваемый для <code>CommandController</code> и хранящий в себе всю необходимую
     * информацию об отправленном сообщении.
     *
     * @param message объект со всей информации об отправленном сообщении.
     */
    public CommandService(@NotNull Message message) {
        this.message = message;
        this.args = message.text().split("\\s");
        this.command = args[0].split("@")[0];
    }

    /**
     * Выполняет отправку ответа на сообщение пользователя без предварительной сборки.
     * Сборка происходит внутри функции и включает в себя ответ (reply) на команду пользователя,
     * режим парсинга <code>HTML</code> и отключение предпросмотра страницы, в случае нахождения ссылки в сообщении.
     *
     * @param text ответ от бота.
     */
    public void execute(@NotNull String text) {
        execute(new SendMessage(message.chat().id(), text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .replyToMessageId(message.messageId()));
    }

    /**
     * Выполняет отправку ответа на сообщение пользователя без предварительной сборки.
     * Сборка происходит внутри функции и включает в себя ответ (reply) на команду пользователя,
     * режим парсинга <code>HTML</code> и отключение предпросмотра страницы, в случае нахождения ссылки в сообщении.
     *
     * @param message сообщение пользователя.
     * @param text    ответ от бота.
     */
    public static void execute(@NotNull Message message, @NotNull String text) {
        execute(new SendMessage(message.chat().id(), text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .replyToMessageId(message.messageId()));
    }

    /**
     * Выполняет отправку подготовленного ответа на сообщение пользователя.
     *
     * @param message - подготовленный ответ для отправки.
     */
    public static void execute(@NotNull SendMessage message) {
        Bot.getBot().execute(message, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {
            }
        });
    }

    @Command(value = "/help", aliases = {"/start"})
    public void help() {
        execute(CommandText.helpMessage);
    }

    @Command("/info")
    public void info() {
        execute(CommandText.infoMessage);
    }

    @Command(value = "/metrics", aliases = {"/online"})
    public void metrics() {
        Metrics metrics = Cache.getMetrics();
        if (metrics.getStatus() == Status.ERROR) {
            execute(CommandText.errorRequest);
            return;
        }
        if (metrics.getStatus() == Status.BROKEN) {
            execute(CommandText.brokenRequestRW);
            return;
        }

        if (metrics.getStatus() == Status.READY) {
            execute(String.format(CommandText.metricsMessage,
                    metrics.getUpdateTime(), metrics.getOnline(), metrics.getTps()));
        } else {
            Bot.getLogger().warn(metrics.getStatus() + " " + metrics.toString());
        }
    }

    @Command(value = "/user", aliases = {"/player"})
    public void user() {
        if (args.length < 2) {
            Message message = this.message;
            String account = Cache.getAccountLink(message.from().id());
            if ("".equals(account)) {
                execute(String.format(CommandText.noUserMessage, command));
                return;
            }
            executeUser(Cache.getPlayer(account));
            return;
        }
        executeUser(Cache.getPlayer(args[1]));
    }

    public void executeUser(Player player) {
        if (player.getStatus() == Status.ERROR) {
            execute(CommandText.errorRequest);
            return;
        }
        if (player.getStatus() == Status.BROKEN) {
            execute(CommandText.brokenRequestRW);
            return;
        }

        if (player.getStatus() == Status.API_ERROR) {
            execute(String.format(CommandText.unknownUserMessage, player.getArg()));
            return;
        }

        execute(player.getAsStringPlayer());
    }

    @Command("/skin")
    public void skin() {
        if (args.length < 2) {
            String account = Cache.getAccountLink(message.from().id());
            if ("".equals(account)) {
                execute(CommandText.noSkinMessage);
                return;
            }
            executeSkin(Cache.getPlayer(account));
            return;
        }
        executeSkin(Cache.getPlayer(args[1]));
    }

    public void executeSkin(Player player) {
        if (player.getStatus() == Status.ERROR) {
            execute(CommandText.errorRequest);
            return;
        }
        if (player.getStatus() == Status.BROKEN) {
            execute(CommandText.brokenRequestRW);
            return;
        }

        if (player.getStatus() == Status.API_ERROR) {
            execute(String.format(CommandText.unknownSkinMessage, player.getArg()));
            return;
        }

        execute(player.getAsStringSkin());
    }

    @Command(value = "/account", aliases = {"/link", "/connect"})
    public void account() {
        User user = message.from();

        if (args.length < 2) {
            execute(String.format(CommandText.noAccountMessage, command));
            return;
        }

        Player player = Cache.getPlayer(args[1]);
        if (player.getName() == null) {
            execute(String.format(CommandText.unknownAccountMessage, args[1]));
            return;
        }

        try (var statement = ConnectionPool.getConnection().prepareStatement(ConnectionPool.setAccountString)) {
            statement.setLong(1, user.id());
            statement.setString(2, args[1]);
            statement.setString(3, args[1]);
            statement.executeUpdate();
            Cache.setAccountLink(user.id(), args[1]);

            execute(CommandText.accountMessage);
        } catch (SQLException e) {
            Bot.getLogger().warn(e, e);
        }
    }

    @Command(value = "/release", aliases = {"/version", "/update"})
    public void release() {
        if (args.length < 2) {
            executeRelease(Cache.getRelease());
            return;
        }
        executeRelease(Cache.getRelease(args[1]));
    }

    public void executeRelease(Release release) {
        if (release.isErrorRequest()) {
            execute(CommandText.errorRequest);
            return;
        }
        if (release.isBrokenRequest()) {
            execute(CommandText.brokenRequestGIT);
            return;
        }

        if (!release.isReleaseExists()) {
            execute(String.format(CommandText.unknownReleaseMessage, release.getArg()));
            return;
        }

        execute(release.getAsString());
    }

    @Command("/coin")
    public void coin() {
        User user = message.from();
        Chat chat = message.chat();
        int time = 0;

        if (!user.id().equals(chat.id())) {
            if (args.length < 2) {
                execute(CommandText.noCoinMessage);
                return;
            }

            try {
                time = Integer.parseInt(args[1]);
                if (time <= 60 || time >= 31622400) {
                    execute(CommandText.unknownCoinMessage);
                    return;
                }
            } catch (NumberFormatException e) {
                execute(CommandText.unknownCoinMessage);
                return;
            }
        }

        int random = ThreadLocalRandom.current().nextInt(2);

        if (random == 1) {
            execute(String.format(CommandText.coinMessage, "Орел!", "Вы победили."));
        } else {
            if (user.id().equals(chat.id())) {
                execute(String.format(CommandText.coinMessage, "Решка!", "Вы проиграли."));
                return;
            }
            execute(String.format(CommandText.coinMessage, "Решка!", "Вы проиграли и будете замучены на " +
                    DateFormatter.formatSeconds(time)));
            RestrictChatMember restrict = new RestrictChatMember(chat.id(), user.id(),
                    new ChatPermissions().canSendMessages(false)
            );
            restrict = restrict.untilDate((int) (System.currentTimeMillis() / 1000L) + time);
            Bot.getBot().execute(restrict);
        }
    }
}
