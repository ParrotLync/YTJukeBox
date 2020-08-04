package nl.parrotlync.ytjukebox;

import kong.unirest.Unirest;
import net.mcjukebox.plugin.bukkit.api.JukeboxAPI;
import net.mcjukebox.plugin.bukkit.api.ResourceType;
import net.mcjukebox.plugin.bukkit.api.models.Media;
import nl.parrotlync.ytjukebox.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class YTCommandExecutor implements TabExecutor {
    private JavaPlugin plugin;

    public YTCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("ytjukebox.use")) {
            if (args.length == 3) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    @Override
                    public void run() {
                        ResourceType type = ResourceType.MUSIC;
                        if (args[0].equalsIgnoreCase("sound")) {
                            type = ResourceType.SOUND_EFFECT;
                        }

                        Media media = null;
                        ChatUtil.sendMessage(player, "&7Start download...", true);
                        try {
                            String url;
                            URL endpoint = new URL("https://yt.ipictserver.nl/api/download?query=" + args[2]);
                            URLConnection conn = endpoint.openConnection();
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                                url = reader.lines().collect(Collectors.joining("\n"));
                            }
                            media = new Media(type, url);
                            ChatUtil.sendMessage(player, "&aDownload Successful!", true);
                        } catch (IOException e) {
                            ChatUtil.sendMessage(player, "&cDownload failed.", true);
                        }

                        if (args[1].startsWith("@")) {
                            JukeboxAPI.getShowManager().getShow(args[1]).play(media);
                        } else {
                            JukeboxAPI.play(player, media);
                        }
                    }
                });
                return true;
            }
            return help(sender);
        }
        ChatUtil.sendMessage(player, "&cYou don't have permission to do that!", true);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        HashMap<Integer, TabArgument> arguments = new HashMap<>();

        arguments.put(0, new StringTabArgument(new String[] {"music", "sound", "help"}));
        arguments.put(1, new PlayerOrShowTabArgument(new String[] {}));

        if (args.length <= 2) {
            List<String> suggestions = new ArrayList<>(arguments.get(args.length - 1).getSuggestions());
            return StringUtil.copyPartialMatches(args[args.length - 1], suggestions, new ArrayList<>());
        }
        return new ArrayList<>();
    }

    private boolean help(CommandSender sender) {
        ChatUtil.sendMessage(sender, "&f+---+ &aYTJukebox &f+---+", false);
        ChatUtil.sendMessage(sender, "&3/youtube music <player/@show> <youtube url> &7Play music to a player or show", false);
        ChatUtil.sendMessage(sender, "&3/youtube sound <player/@show> <youtube url> &7Play sound to a player or show", false);
        return true;
    }
}
