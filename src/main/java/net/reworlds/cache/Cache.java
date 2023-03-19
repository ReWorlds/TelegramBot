package net.reworlds.cache;

import net.reworlds.TelegramBot;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Cache {
    private static boolean isRunningGB = false;
    private static Metrics metrics;
    private static final Map<String, Player> players = new HashMap<>();

    public static Metrics getMetrics() {
        if (metrics != null && metrics.getRequestTime() + 60 > System.currentTimeMillis() / 1000L) {
            return metrics;
        }
        return metrics = new Metrics();
    }

    public static Player getPlayer(String user, Player.Type type) {
        user = user.toLowerCase();

        Player player = players.get(user);
        if (player != null && player.getRequestTime() + 60 > System.currentTimeMillis() / 1000L) {
            return player;
        }
        player = new Player(user, type);
        players.put(user, player);
        if (player.getName() != null) {
            players.put("" + player.getId(), player);
        }
        return player;
    }

    public static void garbageCollector() {
        if (isRunningGB) {
            return;
        }
        isRunningGB = true;

        Thread t = new Thread(() -> {
            players.forEach((playerName, player) -> {
                if (player.getRequestTime() + 60 < System.currentTimeMillis() / 1000L) {
                    players.remove(playerName);
                }
            });

            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                TelegramBot.getLogger().warn(e);
            }
        });
        t.start();
    }
}
