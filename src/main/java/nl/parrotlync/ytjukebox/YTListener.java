package nl.parrotlync.ytjukebox;

import nl.parrotlync.ytjukebox.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class YTListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("ytjukebox.update")) {
            if (YTJukebox.updateAvailable) {
                ChatUtil.sendMessage(player, "&7There is an update available! Get it at https://www.spigotmc.org/resources/ytjukebox.82361/", true);
            }
        }
    }
}
