package net.reworlds.cache;

import lombok.Getter;
import lombok.ToString;
import net.reworlds.utils.DateFormatter;
import net.reworlds.utils.RequestUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ToString
public class Metrics extends Cache.Oldable {
    @Getter
    private final List<String> players = new ArrayList<>();
    @Getter
    private final List<String> tps = new ArrayList<>();
    @Getter
    private int online = 0;
    @Getter
    private String updateTime;
    @Getter
    private boolean brokenRequest = false;
    @Getter
    private boolean metricsExists = false;

    public Metrics() {
        JSONObject json = RequestUtils.getJSON("https://api.reworlds.net/server");
        if (json == null) {
            brokenRequest = true;
            return;
        }
        if (json.has("error-code")) {
            return;
        }
        this.requestTime = (int) (System.currentTimeMillis() / 1000L);

        updateTime = DateFormatter.formatDate(new Date(json.getLong("update-time")));
        online = json.getInt("online");

        json.getJSONArray("players").forEach(player -> this.players.add(player.toString()));
        json.getJSONArray("tps").forEach(tps -> this.tps.add(tps.toString().replaceAll("\\.\\d+", "")));
        metricsExists = true;
    }
}
