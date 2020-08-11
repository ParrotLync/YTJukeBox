package nl.parrotlync.ytjukebox;

import net.mcjukebox.plugin.bukkit.api.JukeboxAPI;
import net.mcjukebox.plugin.bukkit.api.ResourceType;
import net.mcjukebox.plugin.bukkit.api.models.Media;
import nl.parrotlync.ytjukebox.util.ChatUtil;
import nl.parrotlync.ytjukebox.util.YTDownloader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YTCommandExecutor implements TabExecutor {
    private JavaPlugin plugin;
    private YTDownloader downloader = new YTDownloader();

    public YTCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("ytjukebox.use")) {
            if (args.length == 3) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    ResourceType type = ResourceType.MUSIC;
                    if (args[0].equalsIgnoreCase("sound")) {
                        type = ResourceType.SOUND_EFFECT;
                    }

                    ChatUtil.sendMessage(sender, "&7Start download...", true);
                    String url = null;
                    try {
                        url = downloader.download(sender, args[2]);
                    } catch (IOException e) {
                        ChatUtil.sendMessage(sender, "&cDownload failed. Please check if you provided a valid URL.", true);
                    }

                    if (url != null) {
                        Media media = new Media(type, url);
                        if (args[1].startsWith("@")) {
                            JukeboxAPI.getShowManager().getShow(args[1]).play(media);
                        } else {
                            Player player = Bukkit.getPlayer(args[1]);
                            if (player != null) {
                                JukeboxAPI.play(player, media);
                            } else {
                                ChatUtil.sendMessage(sender, "&cPlayer not found.", true);
                            }
                        }
                    }
                });
                return true;
            }
            return help(sender);
        }
        ChatUtil.sendMessage(sender, "&cYou don't have permission to do that!", true);
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
