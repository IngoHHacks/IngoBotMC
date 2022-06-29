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
    long mPlayerlist;
    String spreadsheet;
    String dbName;
    String dbPwd;
    String dbConnection;
    String fakeHost;
    int fakePort;

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
        plugin.getConfig().set("message-playerlist", mPlayerlist);
        plugin.getConfig().set("spreadsheet", spreadsheet);
        plugin.getConfig().set("db-username", dbName);
        plugin.getConfig().set("db-password", dbPwd);
        plugin.getConfig().set("db-connection", dbConnection);
        plugin.getConfig().set("fake-connection-host", fakeHost);
        plugin.getConfig().set("fake-connection-port", fakePort);
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
        mPlayerlist = (long) plugin.getConfig().get("message-playerlist");
        spreadsheet = (String) plugin.getConfig().get("spreadsheet");
        dbName = (String) plugin.getConfig().get("db-username");
        dbPwd = (String) plugin.getConfig().get("db-password");
        dbConnection = (String) plugin.getConfig().get("db-connection");
        fakeHost = (String) plugin.getConfig().get("fake-connection-host");
        fakePort = (int) plugin.getConfig().get("fake-connection-port");
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

    public long getmPlayerlist() {
        return mPlayerlist;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbPwd() {
        return dbPwd;
    }

    public String getDbConnection() {
        return dbConnection;
    }

    public String getSpreadsheet() {
        return spreadsheet;
    }

    public String getFakeConnectionHost() {
        return fakeHost;
    }

    public int getFakeConnectionPort() {
        return fakePort;
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

    public void setSpreadsheet(String spreadsheet) {
        this.spreadsheet = spreadsheet;
        save();
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
        save();
    }

    public void setDbPwd(String dbPwd) {
        this.dbPwd = dbPwd;
        save();
    }

    public void setDbConnection(String dbConnection) {
        this.dbConnection = dbConnection;
        save();
    }

    public void setFakeConnectionHost(String fakeHost) {
        this.fakeHost = fakeHost;
    }

    public void setFakeConnectionPort(int fakePort) {
        this.fakePort = fakePort;
    }
}
