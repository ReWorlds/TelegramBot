package net.reworlds.dispatcher;

import net.reworlds.TelegramBot;
import net.reworlds.controller.Command;
import org.springframework.util.ReflectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class CommandDispatcher {
    private final Object controller;
    private final Map<String, Method> methodHashMap = new HashMap<>();
    private Method defaultMethod;

    public CommandDispatcher(Object controller) {
        Objects.requireNonNull(controller);
        this.controller = controller;

        for (Method method : ReflectionUtils.getAllDeclaredMethods(controller.getClass())) {
            Command command = method.getAnnotation(Command.class);
            if (command != null) {
                String commandId = command.value();
                if (commandId.isEmpty()) {
                    defaultMethod = method;
                } else {
                    methodHashMap.put(commandId, method);
                }
            }
        }
    }

    public void executeCommand(String command, Update update) throws InvocationTargetException, IllegalAccessException {
        Method method = methodHashMap.getOrDefault(command, defaultMethod);
        if (method != null) {
            TelegramBot.getLogger().info(update.getMessage().getFrom().getUserName() + " " + update.getMessage().getText());
            method.invoke(controller);
        }
    }
}
