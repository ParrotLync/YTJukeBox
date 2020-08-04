package nl.parrotlync.ytjukebox;

import net.mcjukebox.plugin.bukkit.MCJukebox;
import net.mcjukebox.plugin.bukkit.managers.shows.Show;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

abstract class TabArgument {

    protected List<String> suggestions = new ArrayList<>();

    public List<String> getSuggestions() {
        return this.suggestions;
    }
}

class StringTabArgument extends TabArgument {

    public StringTabArgument(String[] customSuggestions) {
        suggestions.addAll(Arrays.asList(customSuggestions));
    }
}

class PlayerOrShowTabArgument extends TabArgument {

    public PlayerOrShowTabArgument(String[] customSuggestions) {
        suggestions.addAll(Arrays.asList(customSuggestions));
    }

    @Override
    public List<String> getSuggestions() {
        HashMap<String, Show> shows = MCJukebox.getInstance().getShowManager().getShows();
        for (String show : shows.keySet()) {
            suggestions.add("@" + show);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            suggestions.add(player.getName());
        }
        return this.suggestions;
    }
}
