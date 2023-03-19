package net.reworlds;

import lombok.Getter;
import net.reworlds.config.ConfigManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Date;


@SpringBootApplication
public class TelegramBot {
    static{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss");
        System.setProperty("current.date", dateFormat.format(new Date()));
    }

    public static ConfigManager configManager = new ConfigManager();
    @Getter
    private static final Logger logger = Logger.getLogger(TelegramBot.class);

    public static void main(String[] args) {
        if ("".equals(configManager.getBotToken()) || "".equals(configManager.getBotName())) {
            logger.debug("Перед стартом запишите корректно токен и никнейм бота!");
        } else {
            SpringApplication.run(TelegramBot.class);
        }
    }
}