package net.reworlds.utils;

import net.reworlds.Bot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class RequestUtils {
    /**
     * Позволяет получать <code>JSONObject</code> с REST API страниц.
     *
     * @param url ссылка на страницу, с которой необъодимо взять json.
     * @return JSONObject, в случае успешного (200) запроса или null, в случае, если запрос не удался.
     */
    public static @Nullable JSONObject getJSON(@NotNull String url) throws IOException, InterruptedException {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(1)).build();
            HttpResponse<String> response = Bot.getClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return null;
            }
            return new JSONObject(response.body());
    }
}
