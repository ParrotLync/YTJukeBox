package nl.parrotlync.ytjukebox.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class YTDownloader {

    public String download(CommandSender sender, String query) throws IOException {
        String response;

        URL endpoint = new URL("https://yt.ipictserver.nl/api/download?query=" + query);
        URLConnection conn = endpoint.openConnection();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            response = reader.lines().collect(Collectors.joining("\n"));
        }
        JsonObject json = new Gson().fromJson(response, JsonObject.class);

        if (json.get("status").getAsString().equals("OK")) {
            ChatUtil.sendMessage(sender, "&a" + json.get("msg").getAsString(), true);
            return json.get("url").getAsString();
        } else if (json.get("status").getAsString().equals("ERROR")) {
            ChatUtil.sendMessage(sender, "&3" + json.get("msg").getAsString(), true);
            return null;
        } else {
            ChatUtil.sendMessage(sender, "&7" + json.get("msg").getAsString(), true);
            return null;
        }
    }
}
