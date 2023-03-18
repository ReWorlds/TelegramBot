package net.reworlds.controller;

import net.reworlds.cache.Cache;
import net.reworlds.cache.Metrics;
import net.reworlds.cache.Player;
import net.reworlds.config.CommandText;
import net.reworlds.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class ServiceCommands implements Service {
    private final TelegramController telegramBot;
    private final Update update;
    private final String[] args;

    public ServiceCommands(TelegramController telegramBot, Update update, String... args) {
        this.telegramBot = telegramBot;
        this.update = update;
        this.args = args;
    }

    @Override
    public void help() {
        telegramBot.sendMessage(MessageUtils.buildMessage(update, CommandText.helpMessage));
    }

    @Override
    public void info() {
        telegramBot.sendMessage(MessageUtils.buildMessage(update, CommandText.infoMessage));
    }

    @Override
    public void metrics() {
        String message = CommandText.metricsMessage;
        Metrics metrics = Cache.getMetrics();
        message = message.replace("$update-time", metrics.getUpdateTime().toString());
        message = message.replace("$online", "" + metrics.getOnline());
        message = message.replace("$tps", metrics.getTps().toString());

        telegramBot.sendMessage(MessageUtils.buildMessage(update, message));
    }

    @Override
    public void player() {
        if (args.length < 2) {
            telegramBot.sendMessage(MessageUtils.buildMessage(update, CommandText.noPlayerMessage));
            return;
        }

        Player player = Cache.getPlayer(args[1], Player.Type.NAME);
        if (player.getName() == null) {
            String message = CommandText.unknownPlayerMessage;
            message = message.replace("$player_name", args[1]);
            telegramBot.sendMessage(MessageUtils.buildMessage(update, message));
            return;
        }
        String message = CommandText.playerMessage;
        message = message.replace("$rank", player.getRank());
        message = message.replace("$player_name", player.getName());
        message = message.replace("$id", "" + player.getId());
        message = message.replace("$discord_id", "" + player.getDiscordID());
        message = message.replace("$uuid", player.getUUID());
        message = message.replace("$first_seen", player.getFirstSeen());
        message = message.replace("$last_seen", player.getLastSeen());
        message = message.replace("$playtime", player.getPlayTime());
        if (player.isOnline()) {
            message = message.replace("Оффлайн", "Онлайн");
        }
        telegramBot.sendMessage(MessageUtils.buildMessage(update, message));
    }

    @Override
    public void id() {
        if (args.length < 2) {
            telegramBot.sendMessage(MessageUtils.buildMessage(update, CommandText.noIDMessage));
            return;
        }

        Player player = Cache.getPlayer(args[1], Player.Type.ID);
        if (player.getName() == null) {
            String message = CommandText.unknownIDMessage;
            message = message.replace("$id", args[1]);
            telegramBot.sendMessage(MessageUtils.buildMessage(update, message));
            return;
        }
        String message = CommandText.playerMessage;
        message = message.replace("$rank", player.getRank());
        message = message.replace("$player_name", player.getName());
        message = message.replace("$id", "" + player.getId());
        message = message.replace("$discord_id", "" + player.getDiscordID());
        message = message.replace("$uuid", player.getUUID());
        message = message.replace("$first_seen", player.getFirstSeen());
        message = message.replace("$last_seen", player.getLastSeen());
        message = message.replace("$playtime", player.getPlayTime());
        if (player.isOnline()) {
            message = message.replace("Оффлайн", "Онлайн");
        }
        telegramBot.sendMessage(MessageUtils.buildMessage(update, message));
    }

    @Override
    public void skin() {
        if (args.length < 2) {
            telegramBot.sendMessage(MessageUtils.buildMessage(update, CommandText.noSkinMessage));
            return;
        }

        Player player = Cache.getPlayer(args[1], Player.Type.ALL);
        if (player.getName() == null) {
            String message = CommandText.unknownSkinMessage;
            message = message.replace("$id", args[1]);
            telegramBot.sendMessage(MessageUtils.buildMessage(update, message));
            return;
        }
        String message = CommandText.skinMessage;
        message = message.replace("$rank", player.getRank());
        message = message.replace("$player_name", player.getName());
        message = message.replace("$id", "" + player.getId());
        message = message.replace("$discord_id", "" + player.getDiscordID());
        message = message.replace("$uuid", player.getUUID());
        telegramBot.sendMessage(MessageUtils.buildMessage(update, message));

    }

    @Override
    public void punish() {
        if (args.length < 2) {
            telegramBot.sendMessage(MessageUtils.buildMessage(update, CommandText.noPunishMessage));
            return;
        }

        Player player = Cache.getPlayer(args[1], Player.Type.ALL);
        if (player.getName() == null) {
            String message = CommandText.unknownPunishMessage;
            message = message.replace("$id", args[1]);
            telegramBot.sendMessage(MessageUtils.buildMessage(update, message));
            return;
        }
        String message = CommandText.punishMessage;
        message = message.replace("$rank", player.getRank());
        message = message.replace("$player_name", player.getName());
        message = message.replace("$id", "" + player.getId());
        message = message.replace("$discord_id", "" + player.getDiscordID());
        message = message.replace("$uuid", player.getUUID());

        if (player.getBans().size() > 0) {
            String[] banMessage = message.split("\n └ Этот игрок может играть на сервере.");

            List<Player.Ban> bans = player.getBans();
            for (int i = 0; i < bans.size(); i++) {
                Player.Ban ban = bans.get(i);
                if (i + 1 == bans.size()) {
                    banMessage[0] += "\n ├ Модератор: " + ban.getModerator() +
                            "\n ├ Причина: " + ban.getReason() +
                            "\n ├ Выдан: " + ban.getIssued() +
                            "\n └ Истечет: через " + ban.getLeft();
                    message = banMessage[0] + banMessage[1];
                    break;
                }
                banMessage[0] += "\n ├ Модератор: " + ban.getModerator() +
                        "\n ├ Причина: " + ban.getReason() +
                        "\n ├ Выдан: " + ban.getIssued() +
                        "\n ├ Истечет: через " + ban.getLeft();
            }
        }

        if (player.getMutes().size() > 0) {
            message = message.replace("\n └ Этот игрок может разговаривать в игровом чате.", "");
            List<Player.Mute> mutes = player.getMutes();
            for (int i = 0; i < mutes.size(); i++) {
                Player.Mute mute = mutes.get(i);
                if (i + 1 == mutes.size()) {
                    message += "\n ├ Модератор: " + mute.getModerator() +
                            "\n ├ Причина: " + mute.getReason() +
                            "\n ├ Выдан: " + mute.getIssued() +
                            "\n └ Истечет: через " + mute.getLeft();
                    break;
                }
                message += "\n ├ Модератор: " + mute.getModerator() +
                        "\n ├ Причина: " + mute.getReason() +
                        "\n ├ Выдан: " + mute.getIssued() +
                        "\n ├ Истечет: через " + mute.getLeft();
            }
        }
        telegramBot.sendMessage(MessageUtils.buildMessage(update, message));
    }

    @Override
    public void stats() {
        if (args.length < 2) {
            telegramBot.sendMessage(MessageUtils.buildMessage(update, CommandText.noStatsMessage));
            return;
        }

        Player player = Cache.getPlayer(args[1], Player.Type.ALL);
        if (player.getName() == null) {
            String message = CommandText.unknownStatsMessage;
            message = message.replace("$id", args[1]);
            telegramBot.sendMessage(MessageUtils.buildMessage(update, message));
            return;
        }
        String message = CommandText.statsMessage;
        message = message.replace("$rank", player.getRank());
        message = message.replace("$player_name", player.getName());
        message = message.replace("$id", "" + player.getId());
        message = message.replace("$discord_id", "" + player.getDiscordID());
        message = message.replace("$uuid", player.getUUID());

        message = message.replace("$deaths", "" + player.getStats().getDeaths());
        message = message.replace("$kills", "" + player.getStats().getKills());
        message = message.replace("$mob_kills", "" + player.getStats().getMobKills());
        message = message.replace("$broken_blocks", "" + player.getStats().getBrokenBlocks());
        message = message.replace("$placedBlocks", "" + player.getStats().getPlacedBlocks());
        message = message.replace("$advancements", "" + player.getStats().getAdvancements());
        telegramBot.sendMessage(MessageUtils.buildMessage(update, message));

    }
}
