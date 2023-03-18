package net.reworlds;

import net.reworlds.config.ConfigManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class TelegramBot {
    public static ConfigManager configManager = new ConfigManager();

    public static void main(String[] args) {
        if ("".equals(configManager.getBotToken()) || "".equals(configManager.getBotName())) {
            Logger.getAnonymousLogger().warning("Перед стартом запишите корректно токен и никнейм бота!");
        } else {
            SpringApplication.run(TelegramBot.class);
        }
    }
}