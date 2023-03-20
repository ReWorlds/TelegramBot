package net.reworlds.utils;

import net.reworlds.Bot;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestUtils {
    public static JSONObject getJSON(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = Bot.getClient().send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(response.body());

        } catch (IOException | InterruptedException e) {
            return null;
        }
    }
}
