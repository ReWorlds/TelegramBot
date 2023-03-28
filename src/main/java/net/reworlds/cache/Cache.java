package net.reworlds.cache;

import net.reworlds.Bot;
import net.reworlds.config.CommandText;
import net.reworlds.database.ConnectionPool;
import net.reworlds.utils.MessageUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Cache {
    private static boolean isRunningGB = false;
    private static Metrics metrics;

    private static final Map<Long, String> accounts = new HashMap<>();
    private static final Map<String, Player> players = new HashMap<>();

    public static Metrics getMetrics() {
        if (metrics != null && !metrics.old()) {
            return metrics;
        }
        return metrics = new Metrics();
    }

    public static Player getPlayer(String user) {
        user = user.toLowerCase();

        Player player = players.get(user);
        if (player != null && !player.old()) {
            return player;
        }
        player = new Player(user);
        players.put(user, player);
        if (player.getName() != null) {
            players.put("" + player.getId(), player);
        }
        return player;
    }

    public static String getAccountLink(Long id) {

        String account = accounts.get(id);
        if (account != null) {
            return account;
        }

        try (var statement = ConnectionPool.getConnection().prepareStatement(ConnectionPool.getAccountString)) {
            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();

            if (set.next()) {
                account = set.getString("name");
                Cache.setAccountLink(id, account);
                return account;
            } else {
                return "";
            }
        } catch (SQLException e) {
            Bot.getLogger().warn(e, e);
        }
        return accounts.get(id);
    }

    public static void setAccountLink(Long id, String name) {
        accounts.put(id, name);
    }

    public static void collector() {
        if (isRunningGB) {
            return;
        }
        isRunningGB = true;

        Thread t = new Thread(() -> {
            players.forEach((playerName, player) -> {
                if (player.old()) {
                    players.remove(playerName);
                }
            });

            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                Bot.getLogger().warn(e);
            }
        });
        t.start();
    }

    public static class Oldable {
        int requestTime;
        boolean old() {
            return requestTime + 60 < System.currentTimeMillis() / 1000L;
        }
    }
}
