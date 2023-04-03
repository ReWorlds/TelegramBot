package net.reworlds.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.reworlds.Bot;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionPool {

    public static String setAccountString;
    public static String getAccountString;
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource source;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://%s/%s"
                    .formatted(Bot.getJson().getString("database-host"), Bot.getJson().getString("database-name")));
            config.setUsername(Bot.getJson().getString("database-user"));
            config.setPassword(Bot.getJson().getString("database-password"));


            String table = Bot.getJson().getString("database-table");
            setAccountString = "INSERT INTO %s (id, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = ?;".formatted(table);
            getAccountString = "SELECT * FROM %s WHERE id = ? LIMIT 1".formatted(table);

            source = new HikariDataSource(config);
        } catch (ClassNotFoundException e) {
            Bot.getLogger().warn(e, e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    private ConnectionPool() {
    }
}
