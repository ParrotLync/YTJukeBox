package nl.parrotlync.ytjukebox.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtil {

    public static void sendMessage(CommandSender sender, String msg, boolean withPrefix) {
        if (withPrefix) {
            msg = ChatColor.translateAlternateColorCodes('&', "&8[&cYTJukebox&8] " + msg);
        } else {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
        }

        sender.sendMessage(msg);
    }
}
