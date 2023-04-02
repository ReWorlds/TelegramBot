package net.reworlds.cache;

import lombok.Getter;
import net.reworlds.Bot;
import net.reworlds.config.CommandText;
import net.reworlds.utils.DateFormatter;
import net.reworlds.utils.RequestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Player extends Cache.Oldable {
    @Getter
    private final String arg;
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private String UUID;
    @Getter
    private long discordID;
    @Getter
    private String rank;
    @Getter
    private String firstSeen;
    @Getter
    private String lastSeen;
    @Getter
    private String playTime;
    @Getter
    private String online;
    @Getter
    private final List<Punishment> bans = new ArrayList<>();
    @Getter
    private final List<Punishment> mutes = new ArrayList<>();
    @Getter
    private Statistic stats;
    @Getter
    private String active = "";
    @Getter
    private String asStringPlayer;
    @Getter
    private String asStringSkin;
    @Getter
    private boolean brokenRequest = false;
    @Getter
    private boolean userExists = false;

    public Player(String player) {
        arg = player;

        JSONObject json = RequestUtils.getJSON("https://api.reworlds.net/player/" + player);
        if (json == null) {
            brokenRequest = true;
            return;
        }
        if (json.has("error-code")) {
            json = RequestUtils.getJSON("https://api.reworlds.net/id/" + player);
            if (json == null) {
                brokenRequest = true;
                return;
            }
            if (json.has("error-code")) {
                return;
            }
        }

        this.requestTime = (int) (System.currentTimeMillis() / 1000L);

        id = json.getInt("id");
        name = json.getString("name");
        UUID = json.getString("uuid");
        StringBuilder sb = new StringBuilder(json.getString("rank"));
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        rank = sb.toString();
        discordID = json.getLong("discord-id");
        firstSeen = DateFormatter.formatDate(new Date(json.getLong("first-seen")));
        lastSeen = DateFormatter.formatDate(new Date(json.getLong("last-seen")));
        playTime = DateFormatter.formatMillis(json.getLong("play-time"));
        if (json.getBoolean("online")) {
            online = "Онлайн";
        } else {
            online = "Оффлайн";
        }


        if (!DateFormatter.unknownDate.equals(lastSeen)) {
            try {
                active = DateFormatter.difference(lastSeen) <= 7 ? "\uD83D\uDD25" : "";
            } catch (ParseException e) {
                Bot.getLogger().warn(e, e);
            }
        }


        stats = new Statistic(json.getJSONObject("statistic"));

        var array = json.getJSONArray("bans");
        for (int i = 0; i < array.length(); i++) {
            bans.add(new Punishment(array.getJSONObject(i)));
        }

        array = json.getJSONArray("mutes");
        for (int i = 0; i < array.length(); i++) {
            mutes.add(new Punishment(array.getJSONObject(i)));
        }

        asStringPlayer = String.format(CommandText.userMessage,
                rank, name, id, discordID, UUID, firstSeen, lastSeen, playTime, online,
                stats.deaths, stats.kills, stats.mobKills, stats.brokenBlocks, stats.placedBlocks, stats.advancements,
                Punishment.toString(bans), Punishment.toString(mutes), active);
        asStringSkin = String.format(CommandText.skinMessage, rank, name, id, discordID, UUID);
        userExists = true;
    }

    public static class Punishment {
        @Getter
        private final String moderator;
        @Getter
        private final String reason;
        @Getter
        private final String left;
        @Getter
        private final String issued;
        @Getter
        private final String expire;

        Punishment(JSONObject object) {
            moderator = object.getString("moderator");
            reason = object.getString("reason");
            left = object.getString("left");
            issued = DateFormatter.formatDate(new Date(object.getLong("issued")));
            if ("permanent".equals(left)) {
                expire = "никогда";
            } else {
                expire = DateFormatter.formatDate(new Date(object.getLong("expire")));
            }
        }

        public static String toString(List<Punishment> punishments) {
            StringBuilder builder = new StringBuilder();
            if (punishments.isEmpty()) {
                builder.append("\n└ Этот игрок не наказан.");
                return builder.toString();
            }
            List<Punishment> list = new ArrayList<>(punishments);
            punishments.forEach(punishment -> {
                list.remove(punishment);
                String s;
                if (list.isEmpty()) {
                    s = "\n ├ Модератор: " + punishment.getModerator() +
                            "\n ├ Причина: " + punishment.getReason() +
                            "\n ├ Выдан: " + punishment.getIssued() +
                            "\n └ Истечет: " + punishment.getExpire();
                } else {
                    s = "\n ├ Модератор: " + punishment.getModerator() +
                            "\n ├ Причина: " + punishment.getReason() +
                            "\n ├ Выдан: " + punishment.getIssued() +
                            "\n ├ Истечет: " + punishment.getExpire() + "\n";
                }
                builder.append(s);
            });
            return builder.toString();
        }
    }

    public static class Statistic {
        @Getter
        private final int kills;
        @Getter
        private final int deaths;
        @Getter
        private final int mobKills;
        @Getter
        private final int brokenBlocks;
        @Getter
        private final int placedBlocks;
        @Getter
        private final int advancements;

        public Statistic(JSONObject object) {
            kills = object.getInt("kills");
            deaths = object.getInt("deaths");

            mobKills = statisticOf(object.getJSONArray("mob-kills"));
            brokenBlocks = statisticOf(object.getJSONArray("broken-blocks"));
            placedBlocks = statisticOf(object.getJSONArray("placed-blocks"));

            JSONArray array = object.getJSONArray("advancements");
            int amount = 0;
            for (int i = 0; i < array.length(); i++) {
                amount += array.getJSONObject(i).getBoolean("completed") ? 1 : 0;
            }
            advancements = amount;
        }

        private static int statisticOf(JSONArray array) {
            int amount = 0;
            for (int i = 0; i < array.length(); i++) {
                amount += array.getJSONObject(i).getInt("amount");
            }
            return amount;
        }
    }
}
