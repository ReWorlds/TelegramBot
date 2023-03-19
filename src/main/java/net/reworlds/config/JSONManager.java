package net.reworlds.config;

import lombok.Data;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public final class JSONManager {
    private Path path;
    private JSONObject jsonObject;

    public JSONManager(String path) throws IOException {
        this.path = getPath(path);
        this.jsonObject = new JSONObject(Files.readString(this.path, StandardCharsets.UTF_8));
    }

    private Path getPath(String stringPath) throws IOException {
        Path path = Paths.get(stringPath);
        if (!Files.isDirectory(path.getParent())) {
            Files.createDirectory(path.getParent());
        }
        if (Files.notExists(path)) {
            Files.createFile(path);
        }

        return path;
    }

    public JSONManager updateFile() throws IOException {
        Files.writeString(path, this.jsonObject.toString(), StandardCharsets.UTF_8);
        return this;
    }

    public JSONManager updateFile(JSONObject jsonObject, boolean saveInObject) throws IOException {
        if (saveInObject) {
            this.jsonObject = jsonObject;
        }
        Files.writeString(path, jsonObject.toString(), StandardCharsets.UTF_8);
        return this;
    }

    public static JSONManager of(String path) throws IOException {
        return new JSONManager(path);
    }
}
