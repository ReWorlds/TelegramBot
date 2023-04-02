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
                String[] aliases = command.aliases();
                if (commandId.isEmpty()) {
                    defaultMethod = method;
                } else {
                    for (String alias : aliases) {
                        methodHashMap.put(alias, method);
                    }
                    methodHashMap.put(commandId, method);
                }
            }
        }
    }

    public void executeCommand(String command, Update update) throws InvocationTargetException, IllegalAccessException {
        Method method = methodHashMap.getOrDefault(command, defaultMethod);
        if (method != null) {
            Bot.getLogger().info("{user: " + update.message().from().username() + ", chat-name: " + update.message().chat().username()
                    + ", message: " + update.message().text() + "}");
            method.invoke(controller);
        }
    }
}
