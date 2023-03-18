package net.reworlds.controller;

import lombok.Getter;
import net.reworlds.TelegramBot;
import net.reworlds.cache.Cache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Logger;

@Component
public class TelegramController extends TelegramLongPollingBot {
    @Getter
    private final int launchTime = (int) (System.currentTimeMillis() / 1000L);
    private final UpdateController updateController;

    public TelegramController(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    public String getBotUsername() {
        return TelegramBot.configManager.getBotName();
    }

    @Override
    public String getBotToken() {
        return TelegramBot.configManager.getBotToken();
    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
        Cache.garbageCollector();
        Logger.getLogger("TelegramController.launch")
                .info("Launch Time > " + LocalDate.now() + " " + LocalTime.now());
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    public void sendMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                Logger.getLogger("TelegramController.sendMessage")
                        .warning(e.getMessage());
            }
        }
    }
}