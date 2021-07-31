package tv.ingoh.minecraft.plugins.ingobotcore.discord;

import net.dv8tion.jda.api.JDA;
import tv.ingoh.minecraft.plugins.ingobotcore.Config;

public class Channels {

    public long textChannel;
    public long debugChannel;
    public long playerListChannel;
    public long watchListChannel;
    public long spreadsheetChannel;

    public Channels(JDA discord, Config config) {
        textChannel = config.getcChat();
        debugChannel = config.getcDebug();
        playerListChannel = config.getcPlayerlist();
        watchListChannel = config.getcWatchlist();
        spreadsheetChannel = config.getcSpreadsheet();
    }
    
}
