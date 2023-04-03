package net.reworlds.cache;

import net.reworlds.Bot;
import net.reworlds.database.ConnectionPool;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class Cache {
    private static boolean isRunningGB = false;
    private static Metrics metrics;

    private static final Map<Long, String> accounts = new HashMap<>();
    private static final Map<String, Player> players = new HashMap<>();
    private static final Map<String, Release> releases = new HashMap<>();

    /**
     * Получение метрики сервера из "кэша".
     * В случае, если в кэше нет метрики или метрика устарела, делает новый объект <code>Metrics</code>,
     * который добавляется в кэш и после передается из функции.
     *
     * @return объект <code>Metrics</code>.
     * @see Metrics
     */
    public static @NotNull Metrics getMetrics() {
        if (metrics != null && !metrics.old()) {
            return metrics;
        }
        return metrics = new Metrics();
    }

    /**
     * Получение игрока сервера из "кэша" через строку, которая может быть как никнеймом, так и ID игрока.
     * В случае, если в кэше нет игрока или данные об игроке устарели, делается новый объект <code>Player</code>,
     * который добавляется в кэш и после передается из функции.
     * В сам кэш попадает 2 объекта с ключами в виде никнейма и ID.
     *
     * @param user никнейм или ID игрока
     * @return объект <code>Player</code>.
     * @see Player
     */
    public static @NotNull Player getPlayer(@NotNull String user) {
        user = user.toLowerCase();

        Player player = players.get(user);
        if (player != null && !player.old()) {
            return player;
        }
        player = new Player(user);
        players.put(user, player);
        if (player.getStatus() == Status.READY) {
            players.put("" + player.getId(), player);
        }
        return player;
    }

    /**
     * Получение релиза бота из "кэша" через строку.
     * В случае, если в кэше нет релиза или данные о релизе устарели, делается новый объект <code>Release</code>.
     * В случае, если <code>tag</code> - "latest", в кэш будет записано 2 ключа релиза - latest и сам тег.
     *
     * @param tag версия обновления бота.
     * @return объект <code>Release</code>.
     * @see Release
     */
    public static @NotNull Release getRelease(@NotNull String tag) {
        Release release = releases.get(tag);
        if (release != null && !release.old()) {
            return release;
        }
        release = new Release(tag);
        releases.put(tag, release);
        if ("latest".equals(tag)) {
            releases.put(release.getTag(), release);
        }
        return release;
    }

    /**
     * Получение последнего релиза бота из "кэша".
     * В случае, если в кэше нет релиза или данные о релизе устарели, делается новый объект <code>Release</code>.
     * В кэш будет записано 2 ключа релиза - latest и сам тег.
     *
     * @return объект <code>Release</code>.
     * @see Release
     */
    public static @NotNull Release getRelease() {
        return getRelease("latest");
    }

    /**
     * Получение связи пользователя telegram с идентификатором (никнейм или id) игрока сервера.
     *
     * @param id id пользователя telegram.
     * @return идентификатор игрока сервера.
     */
    public static @NotNull String getAccountLink(long id) {

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

    /**
     * Установка связи пользователя telegram с идентификатором (никнейм или id) игрока сервера.
     *
     * @param id id пользователя telegram.
     */
    public static void setAccountLink(long id, @NotNull String name) {
        accounts.put(id, name);
    }

    /**
     * Коллектор мусора, убирающий устаревшие данные из кэша.
     * Запускается единожды.
     */
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

    /**
     * Особый класс, от которого наследуются все кэшируемые объекты (Metrics, Player, Release)
     * Хранит в себе время создания запроса и метод old, возвращающий true, если объект существует >= 60 секунд.
     */
    public static class Oldable {
        int requestTime;

        /**
         * Возвращает <code>boolean</code> ответ состояния старости объекта, где true - устарел.
         *
         * @return true, если прошло 60 секунд с момента создания объекта.
         */
        boolean old() {
            return requestTime + 60 < System.currentTimeMillis() / 1000L;
        }
    }

    /**
     * Enum класс для обобщения статуса кэшируемых объектов (Metrics, Player, Release)
     */
    public enum Status {
        READY,
        ERROR,
        BROKEN,
        API_ERROR
    }
}
