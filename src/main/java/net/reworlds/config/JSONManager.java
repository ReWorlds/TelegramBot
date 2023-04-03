package net.reworlds.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public final class JSONManager {
    @Setter(AccessLevel.PRIVATE)
    private Path path;
    private JSONObject jsonObject;

    /**
     * Конструктор класса, заполняющийся объектом <code>Path</code> с путем до файла
     * и объектом <code>JSONObject</code> с данными из файла.
     *
     * @param path путь до json файла, который необходимо открыть.
     */
    public JSONManager(@NotNull String path) throws IOException {
        this.path = getPath(path);
        create();
        this.jsonObject = getObject();
    }

    /**
     * Получает путь до файла в виде строки и возвращает в виде объекта <code>Path</code>.
     *
     * @param stringPath путь до файла в виде строки.
     * @return путь до файла
     * @see Path
     */
    private @NotNull Path getPath(@NotNull String stringPath) {
        return Paths.get(stringPath);
    }

    /**
     * Создает директории и сам файл по пути в объекте, если они отсутствуют.
     */
    private void create() throws IOException {
        if (!Files.isDirectory(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
    }

    /**
     * Позволяет обновить файл, используя вложенный объект <code>JSONObject</code>.
     *
     * @return тот же объект используемого класса.
     */
    public @NotNull JSONManager updateFile() throws IOException {
        Files.writeString(path, this.jsonObject.toString(), StandardCharsets.UTF_8);
        return this;
    }

    /**
     * Позволяет обновить файл используя передаваемый объект <code>JSONObject</code>, который также можно
     * сохранить внутри объекта <code>JSONManager</code>, передав <code>boolean</code> аргумент для сохранения.
     *
     * @param jsonObject   объект класса <code>jsonObject</code>, который требуется сохранить в файл.
     * @param saveInObject <code>boolean</code> тип данных, при <code>true</code> сохраняет передаваемые данные в объект.
     * @return тот же объект используемого класса.
     */
    public @NotNull JSONManager updateFile(@NotNull JSONObject jsonObject, boolean saveInObject) throws IOException {
        if (saveInObject) {
            this.jsonObject = jsonObject;
        }
        Files.writeString(path, jsonObject.toString(), StandardCharsets.UTF_8);
        return this;
    }

    /**
     * Позволяет получить объект <code>JSONObject</code>, заполненный данными из файла, который
     * находится по пути из конструктора объекта.
     * В случае, если файл или директории до файла были удалены, они будут созданы вновь.
     *
     * @return объект <code>JSONObject</code>.
     */
    public @NotNull JSONObject getObject() throws IOException {
        create();
        return new JSONObject(Files.readString(this.path, StandardCharsets.UTF_8));
    }

    /**
     * Cтатический метод для создания объекта класса <code>JSONManager</code>.
     *
     * @param path путь до json файла, который необходимо открыть.
     */
    public static @NotNull JSONManager of(@NotNull String path) throws IOException {
        return new JSONManager(path);
    }
}
