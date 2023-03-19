package net.reworlds.dispatcher;

import com.pengrad.telegrambot.model.Update;
import net.reworlds.Bot;
import net.reworlds.controller.CommandController;
import net.reworlds.service.Command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandDispatcher {
    private final CommandController controller;
    private final Map<String, Method> methodHashMap = new HashMap<>();
    private Method defaultMethod;

    public CommandDispatcher(CommandController controller) {
        Objects.requireNonNull(controller);
        this.controller = controller;

        for (Method method : controller.getClass().getDeclaredMethods()) {
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
            Bot.getLogger().info(update.message().from().username() + " " + update.message().text());
            method.invoke(controller);
        }
    }
}
