package net.reworlds.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Component
public final class ConfigManager {
    @Setter(AccessLevel.PRIVATE)
    private String botName;
    @Setter(AccessLevel.PRIVATE)
    private String botToken;

    public ConfigManager() {
        loadDefaultSettings(getPath());
    }

    private Path getPath() {
        Path path = Paths.get(System.getProperty("user.dir") + "/config/config.json");
        if (!Files.isDirectory(path.getParent())) {
            try {
                Files.createDirectory(path.getParent());
                setDefaultSettings(Files.createFile(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return path;
    }

    private void setDefaultSettings(Path path) {
        JSONObject object = new JSONObject();
        object.put("bot-name", "");
        object.put("bot-token", "");
        try {
            Files.writeString(path, object.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadDefaultSettings(Path path) {
        try {
            JSONObject object = new JSONObject(Files.readString(path));
            botName = object.getString("bot-name");
            botToken = object.getString("bot-token");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
