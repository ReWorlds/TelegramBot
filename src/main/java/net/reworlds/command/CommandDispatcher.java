package net.reworlds.command;

import com.pengrad.telegrambot.model.Message;
import net.reworlds.Bot;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class CommandDispatcher {
    private final CommandService service;
    private final Map<String, Method> methodHashMap = new HashMap<>();

    /**
     * Конструктор объекта, собирающий все методы с аннотацией <code>@Command</code> из объекта ServiceCommands
     * для дальнейшего использования.
     *
     * @param service объект <code>ServiceCommands</code>.
     */
    public CommandDispatcher(@NotNull CommandService service) {
        Objects.requireNonNull(service);
        this.service = service;

        for (Method method : service.getClass().getDeclaredMethods()) {
            Command command = method.getAnnotation(Command.class);
            if (command != null) {
                String commandId = command.value();   // Команда
                String[] aliases = command.aliases(); // Алиасы
                if (!commandId.isEmpty()) {
                    for (String alias : aliases) {
                        methodHashMap.put(alias, method);
                    }
                    methodHashMap.put(commandId, method);
                }
            }
        }
    }

    /**
     * Выполняет команду, заложенную в объекте <code>ServiceCommand</code>.
     */
    public void executeCommand() throws InvocationTargetException, IllegalAccessException {
        Message message = service.getMessage();
        Method method = methodHashMap.get(service.getCommand());

        if (method != null) {
            Bot.getLogger().info("{user: " + message.from().username() + ", chat-name: " + message.chat().username()
                    + ", message: " + message.text() + "}");
            method.invoke(service);
        }
    }
}
