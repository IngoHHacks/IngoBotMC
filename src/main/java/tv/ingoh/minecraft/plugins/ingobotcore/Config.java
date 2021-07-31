package tv.ingoh.minecraft.plugins.ingobotcore;

import org.bukkit.plugin.java.JavaPlugin;

public class Config {

    final JavaPlugin plugin;
    boolean enabled;
    String token;
    long cChat;
    long cDebug;
    long cPlayerlist;
    long cWatchlist;
    long cSpreadsheet;

    public Config(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        load();
    }

	public void save() {
        plugin.getConfig().set("enabled", enabled);
        plugin.getConfig().set("token", token);
        plugin.getConfig().set("channel-chat", cChat);
        plugin.getConfig().set("channel-debug", cDebug);
        plugin.getConfig().set("channel-playerlist", cPlayerlist);
        plugin.getConfig().set("channel-watchlist", cWatchlist);
        plugin.getConfig().set("channel-spreadsheet", cSpreadsheet);
        plugin.saveConfig();
	}

	public void load() {
        enabled = (boolean) plugin.getConfig().get("enabled");
        token = (String) plugin.getConfig().get("token");
        cChat = (long) plugin.getConfig().get("channel-chat");
        cDebug = (long) plugin.getConfig().get("channel-debug");
        cPlayerlist = (long) plugin.getConfig().get("channel-playerlist");
        cWatchlist = (long) plugin.getConfig().get("channel-watchlist");
        cSpreadsheet = (long) plugin.getConfig().get("channel-spreadsheet");
	}

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getToken() {
        return token;
    }

    public long getcChat() {
        return cChat;
    }

    public long getcDebug() {
        return cDebug;
    }

    public long getcPlayerlist() {
        return cPlayerlist;
    }

    public long getcSpreadsheet() {
        return cSpreadsheet;
    }

    public long getcWatchlist() {
        return cWatchlist;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        save();
    }

    public void setToken(String token) {
        this.token = token;
        save();
    }

    public void setcChat(long cChat) {
        this.cChat = cChat;
        save();
    }

    public void setcDebug(long cDebug) {
        this.cDebug = cDebug;
        save();
    }

    public void setcPlayerlist(long cPlayerlist) {
        this.cPlayerlist = cPlayerlist;
        save();
    }

    public void setcSpreadsheet(long cSpreadsheet) {
        this.cSpreadsheet = cSpreadsheet;
        save();
    }

    public void setcWatchlist(long cWatchlist) {
        this.cWatchlist = cWatchlist;
        save();
    }
    
}
