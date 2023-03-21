package net.reworlds.service;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.ChatPermissions;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.RestrictChatMember;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import net.reworlds.cache.Cache;
import net.reworlds.cache.Metrics;
import net.reworlds.cache.Player;
import net.reworlds.config.CommandText;
import net.reworlds.utils.DateFormatter;
import net.reworlds.utils.MessageUtils;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class ServiceCommands {
    private final TelegramBot bot;
    private final Update update;
    private final String[] args;

    public ServiceCommands(TelegramBot bot, Update update, String... args) {
        this.bot = bot;
        this.update = update;
        this.args = args;
    }

    private void execute(SendMessage request) {
        bot.execute(request, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {

            }

            @Override
            public void onFailure(SendMessage request, IOException e) {

            }
        });
    }

    public void help() {
        execute(MessageUtils.buildMessage(update, CommandText.helpMessage));
    }

    public void info() {
        execute(MessageUtils.buildMessage(update, CommandText.infoMessage));
    }

    public void metrics() {
        Metrics metrics = Cache.getMetrics();
        execute(MessageUtils.buildMessage(update, String.format(CommandText.metricsMessage, metrics.getUpdateTime(), metrics.getOnline(), metrics.getTps())));
    }

    public void user() {
        if (args.length < 2) {
            execute(MessageUtils.buildMessage(update, CommandText.noUserMessage));
            return;
        }

        Player player = Cache.getPlayer(args[1]);
        if (player.getName() == null) {
            execute(MessageUtils.buildMessage(update, String.format(CommandText.unknownUserMessage, args[1])));
            return;
        }

        execute(MessageUtils.buildMessage(update, player.getAsString()));
    }

    public void skin() {
        if (args.length < 2) {
            execute(MessageUtils.buildMessage(update, CommandText.noSkinMessage));
            return;
        }

        Player player = Cache.getPlayer(args[1]);
        if (player.getName() == null) {
            execute(MessageUtils.buildMessage(update, String.format(CommandText.unknownSkinMessage, args[1])));
            return;
        }
        execute(MessageUtils.buildMessage(update, String.format(CommandText.skinMessage, player.getRank(), player.getName(), player.getId(),
                player.getDiscordID(), player.getUUID())));

    }

    public void coin() {
        User user = update.message().from();
        Chat chat = update.message().chat();
        int time = 0;
        if (!user.id().equals(chat.id())) {
            if (args.length < 2) {
                execute(MessageUtils.buildMessage(update, CommandText.noCoinMessage));
                return;
            }

            try {
                time = Integer.parseInt(args[1]);
                if (time <= 60 || time >= 31622400) {
                    execute(MessageUtils.buildMessage(update, CommandText.unknownCoinMessage));
                    return;
                }
            } catch (NumberFormatException e) {
                execute(MessageUtils.buildMessage(update, CommandText.unknownCoinMessage));
                return;
            }
        }

        int random = ThreadLocalRandom.current().nextInt(2);

        if (random == 1) {
            execute(MessageUtils.buildMessage(update, String.format(CommandText.coinMessage,
                    "Орел!", "Вы победили.")));
        } else {
            if (user.id().equals(chat.id())) {
                execute(MessageUtils.buildMessage(update, String.format(CommandText.coinMessage,
                        "Решка!", "Вы проиграли.")));
                return;
            }
            execute(MessageUtils.buildMessage(update, String.format(CommandText.coinMessage,
                    "Решка!", "Вы проиграли и будете замучены на " + DateFormatter.formatSeconds(time))));
            RestrictChatMember restrict = new RestrictChatMember(
                    chat.id(),
                    user.id(),
                    new ChatPermissions().canSendMessages(false)
            );
            restrict = restrict.untilDate((int) (System.currentTimeMillis() / 1000L) + time);
            bot.execute(restrict);
        }
    }
}
