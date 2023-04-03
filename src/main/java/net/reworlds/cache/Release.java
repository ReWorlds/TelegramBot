package net.reworlds.cache;

import lombok.Getter;
import net.reworlds.Bot;
import net.reworlds.command.CommandText;
import net.reworlds.utils.MessageUtils;
import net.reworlds.utils.RequestUtils;
import org.json.JSONObject;

import java.io.IOException;

public final class Release extends Cache.Oldable {
    @Getter
    private String arg;
    @Getter
    private String tag;
    @Getter
    private String releaseUrl;
    @Getter
    private String name;
    @Getter
    private String draft;
    @Getter
    private String preRelease;
    @Getter
    private String author;
    @Getter
    private String authorUrl;
    @Getter
    private String releaseDate;
    @Getter
    private String description;
    @Getter
    private String asString;
    @Getter
    private boolean errorRequest = false;
    @Getter
    private boolean brokenRequest = false;
    @Getter
    private boolean releaseExists = false;

    public Release(String tag) {
        JSONObject json;
        if ("latest".equals(tag)) {
            try {
                json = RequestUtils.getJSON("https://api.github.com/repos/ReWorlds/TelegramBot/releases/latest");
            } catch (IOException | InterruptedException e) {
                Bot.getLogger().warn(e, e);
                errorRequest = true;
                return;
            }
        } else {
            try {
                json = RequestUtils.getJSON("https://api.github.com/repos/ReWorlds/TelegramBot/releases/tags/" + tag);
            } catch (IOException | InterruptedException e) {
                Bot.getLogger().warn(e, e);
                errorRequest = true;
                return;
            }
        }
        if (json == null) {
            brokenRequest = true;
            return;
        }
        if (json.has("documentation_url")) {
            return;
        }
        this.requestTime = (int) (System.currentTimeMillis() / 1000L);

        getInfo(json);
        arg = tag;
        releaseExists = true;
    }

    private void getInfo(JSONObject json) {
        tag = json.getString("tag_name");
        releaseUrl = json.getString("html_url");
        name = json.getString("name");
        draft = json.getBoolean("draft") ? "✏️" : "";
        preRelease = json.getBoolean("prerelease") ? "⌛" : "";
        authorUrl = json.getJSONObject("author").getString("html_url");
        author = json.getJSONObject("author").getString("login");
        releaseDate = json.getString("published_at");
        releaseDate = releaseDate.replaceAll("[TZ]+", " ").trim();

        description = json.getString("body");
        String text = CommandText.releaseMessage;
        if (!description.contains("\r\n")) {
            text = MessageUtils.replaceLast(text, "├", "└");
            description = description.replaceFirst("-", "");
        } else {
            description = MessageUtils.replaceLast(description
                    .replaceFirst("-", "")
                    .replaceAll("`", "")
                    .replaceAll("\r\n-", "\r\n ├"), "├", "└");
        }

        asString = String.format(text, tag, releaseUrl, name, draft, preRelease, authorUrl,
                author, releaseDate, description);
    }
}
