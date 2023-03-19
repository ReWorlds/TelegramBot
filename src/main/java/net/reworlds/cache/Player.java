package net.reworlds.cache;

import lombok.Getter;
import lombok.ToString;
import net.reworlds.utils.DateFormatter;
import net.reworlds.utils.RequestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ToString
public class Player {
    @Getter
    private int requestTime = 0;
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
    private boolean online;
    @Getter
    private final List<Ban> bans = new ArrayList<>();
    @Getter
    private final List<Mute> mutes = new ArrayList<>();
    @Getter
    private Statistic stats;

    public Player(String player, Type type) throws JSONException {
        JSONObject json = null;
        switch (type) {
            case NAME -> {
                json = RequestUtils.getJSON("https://api.reworlds.net/player/" + player);
            }
            case ID -> {
                json = RequestUtils.getJSON("https://api.reworlds.net/id/" + player);
            }
            case ALL -> {
                json = RequestUtils.getJSON("https://api.reworlds.net/player/" + player);
                if (json != null && json.has("error-code")) {
                    json = RequestUtils.getJSON("https://api.reworlds.net/id/" + player);
                }
            }
        }

        if (json == null || json.has("error-code")) {
            return;
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
        online = json.getBoolean("online");
        stats = new Statistic(json.getJSONObject("statistic"));

        var array = json.getJSONArray("bans");
        for (int i = 0; i < array.length(); i++) {
            bans.add(new Ban(array.getJSONObject(i)));
        }

        array = json.getJSONArray("mutes");
        for (int i = 0; i < array.length(); i++) {
            mutes.add(new Mute(array.getJSONObject(i)));
        }
    }

    public enum Type {
        NAME,
        ID,
        ALL
    }

    @ToString
    public static class Ban {
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

        public Ban(JSONObject object) {
            moderator = object.getString("moderator");
            reason = object.getString("reason");
            left = object.getString("left");
            issued = DateFormatter.formatDate(new Date(object.getLong("issued")));
            expire = DateFormatter.formatDate(new Date(object.getLong("expire")));
        }
    }

    @ToString
    public static class Mute {
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

        public Mute(JSONObject object) {
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
