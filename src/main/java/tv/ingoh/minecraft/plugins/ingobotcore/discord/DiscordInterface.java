package tv.ingoh.minecraft.plugins.ingobotcore.discord;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;

import io.github.starsdown64.minecord.api.ExternalMessageEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
        string = formatCodes(string);
        discord.getTextChannelById(channels.textChannel).sendMessage(string).queue();
        if (Bukkit.getServer().getPluginManager().getPlugin("Minecord") != null) {
            ExternalMessageEvent messageEvent = new ExternalMessageEvent(string);
            main.scheduleMinecord(messageEvent);
        }
    }

    public void sendChat(String string, boolean sendMinecord) {
        string = formatCodes(string);
        discord.getTextChannelById(channels.textChannel).sendMessage(string).queue();
        if (sendMinecord && Bukkit.getServer().getPluginManager().getPlugin("Minecord") != null) {
            ExternalMessageEvent messageEvent = new ExternalMessageEvent(string);
            main.scheduleMinecord(messageEvent);
        }
    }
    private String formatCodes(String string) {
        try {
            String endReplacement = "";
            StringBuilder builder = new StringBuilder(string);
            for (int i = 0; i < builder.length() - 1; i++) {
                if (builder.charAt(i) == '§') {
                    if (builder.charAt(i+1) == 'k' || builder.charAt(i+1) == 'K') {
                        builder.delete(i, i+2);
                        builder.insert(i, "||");
                        endReplacement = "||" + endReplacement;
                    }
                    else if (builder.charAt(i+1) == 'l' || builder.charAt(i+1) == 'L') {
                        builder.delete(i, i+2);
                        builder.insert(i, "**");
                        endReplacement = "**" + endReplacement;
                    }
                    else if (builder.charAt(i+1) == 'm' || builder.charAt(i+1) == 'M') {
                        builder.delete(i, i+2);
                        builder.insert(i, "~~");
                        endReplacement = "~~" + endReplacement;
                    }
                    else if (builder.charAt(i+1) == 'n' || builder.charAt(i+1) == 'N') {
                        builder.delete(i, i+2);
                        builder.insert(i, "__");
                        endReplacement = "__" + endReplacement;
                    }
                    else if (builder.charAt(i+1) == 'o' || builder.charAt(i+1) == 'O') {
                        builder.delete(i, i+2);
                        builder.insert(i, "*");
                        endReplacement = "*" + endReplacement;
                        i -= 1;
                    }
                    else if (builder.charAt(i+1) == 'r' || builder.charAt(i+1) == 'R') {
                        builder.delete(i, i+2);
                        builder.insert(i, endReplacement);
                        endReplacement = "";
                        i += endReplacement.length() - 2;
                    } else {
                        builder.delete(i, i+2);
                        i -= 2;
                    }
                }
            }
            builder.append(endReplacement);
            return builder.toString();
        } catch (Exception e) {
            sendDebug("ERROR! FAILED TO FORMAT MESSAGE: " + string);
            printStackTrace(e.getStackTrace());
            return string;
        }
    }

    public void sendInfoChat(String string) {
        discord.getTextChannelById(channels.textChannel).sendMessage("[INFO] " + string).queue();
    }

    public Channels getChannels() {
        return channels;
    }

    public void sendTo(long channelId, String string) {
        string = formatCodes(string);
        discord.getTextChannelById(channelId).sendMessage(string).queue();
    }

    public void sendTo(long channelId, Message message) {
        discord.getTextChannelById(channelId).sendMessage(message).queue();
    }

    public void sendTo(long channelId, MessageEmbed message) {
        discord.getTextChannelById(channelId).sendMessageEmbeds(message).queue();
    }


    public void editMessage(long channelId, long messageId, String string) {
        discord.getTextChannelById(channelId).editMessageById(messageId, string).queue();
    }

    public void editMessage(long channelId, long messageId, Message message) {
        discord.getTextChannelById(channelId).editMessageById(messageId, message).queue();
    }

    public void editMessage(long channelId, long messageId, MessageEmbed message) {
        discord.getTextChannelById(channelId).editMessageEmbedsById(messageId, message).queue();
    }
}
