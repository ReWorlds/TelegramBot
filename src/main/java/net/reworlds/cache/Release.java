package net.reworlds.cache;

import lombok.Getter;
import net.reworlds.config.CommandText;
import net.reworlds.utils.MessageUtils;
import net.reworlds.utils.RequestUtils;
import org.json.JSONObject;

public class Release extends Cache.Oldable {

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

    public Release(String tag) {
        JSONObject json;
        if ("latest".equals(tag)) {
            json = RequestUtils.getJSON("https://api.github.com/repos/ReWorlds/TelegramBot/releases/latest");
        } else {
            json = RequestUtils.getJSON("https://api.github.com/repos/ReWorlds/TelegramBot/releases/tags/" + tag);
        }
        if (json == null || json.has("documentation_url")) {
            return;
        }
        this.requestTime = (int) (System.currentTimeMillis() / 1000L);

        getInfo(json);
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
        releaseDate = releaseDate.substring(0, releaseDate.length() - 1);
        description = MessageUtils.replaceLast(json.getString("body")
                .replaceFirst("-", "")
                .replaceAll("`", "")
                .replaceAll("\r\n-", "\r\n ├"), "├", "└");
        if (!description.matches("\r\n")) {
            description = description.replace("├", "└");
        }

        asString = String.format(CommandText.releaseMessage, tag, releaseUrl, name, draft, preRelease, authorUrl,
                author, releaseDate, description);
    }
}
