package tv.ingoh.minecraft.plugins.ingobotcore.discord;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;

import io.github.starsdown64.minecord.api.ExternalMessageEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import tv.ingoh.minecraft.plugins.ingobotcore.Config;
import tv.ingoh.minecraft.plugins.ingobotcore.Main;

public class DiscordInterface {

    private String token;
    private JDA discord;
    private Channels channels;
    private Config config;
    private Main main;

    public DiscordInterface(Config config, Main main) {
        token = config.getToken();
        this.config = config;
        this.main = main;
    }

    public void start() throws LoginException {
        channels = new Channels(discord, config);
        discord = JDABuilder.createDefault(token).build();
        discord.getPresence().setActivity(Activity.playing("Minecraft"));
        discord.addEventListener(new Events(channels, main));
    }

    public final void stop() {
		discord.setAutoReconnect(false);
		discord.shutdown();
	}
    public void sendDebug(String out) {
        discord.getTextChannelById(channels.debugChannel).sendMessage(out).queue();
    }

    public void printStackTrace(StackTraceElement[] stackTrace) {
        for (StackTraceElement stackTraceElement : stackTrace) {
            discord.getTextChannelById(channels.debugChannel).sendMessage(stackTraceElement.toString()).queue();
        }
    }

    public void sendChat(String string) {
        discord.getTextChannelById(channels.textChannel).sendMessage(string).queue();
        if (Bukkit.getServer().getPluginManager().getPlugin("Minecord") != null) {
            ExternalMessageEvent messageEvent = new ExternalMessageEvent(string);
            main.scheduleMinecord(messageEvent);
        }
    }

    public void sendChat(String string, boolean sendMinecord) {
        discord.getTextChannelById(channels.textChannel).sendMessage(string).queue();
        if (sendMinecord && Bukkit.getServer().getPluginManager().getPlugin("Minecord") != null) {
            ExternalMessageEvent messageEvent = new ExternalMessageEvent(string);
            main.scheduleMinecord(messageEvent);
        }
    }
    public void sendInfoChat(String string) {
        discord.getTextChannelById(channels.textChannel).sendMessage("[INFO] " + string).queue();
    }

    public Channels getChannels() {
        return channels;
    }

    public void sendTo(long channelId, String string) {
        discord.getTextChannelById(channelId).sendMessage(string).queue();
    }
}
