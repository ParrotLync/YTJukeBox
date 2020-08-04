package nl.parrotlync.ytjukebox;

import nl.parrotlync.ytjukebox.util.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

public class YTJukebox extends JavaPlugin {
    public static boolean updateAvailable;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new YTListener(), this);
        this.getCommand("youtube").setExecutor(new YTCommandExecutor(this));
        getLogger().info("YTJukebox is now enabled!");
        new UpdateChecker(this, 82361).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info("You are on the newest version.");
                updateAvailable = false;
            } else {
                getLogger().info("There is a new version available! https://www.spigotmc.org/resources/ytjukebox.82361/");
                updateAvailable = true;
            }
        });
    }
}
