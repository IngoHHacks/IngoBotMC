package tv.ingoh.minecraft.plugins.ingobotcore.discord;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import tv.ingoh.minecraft.plugins.ingobotcore.Config;

public class DiscordInterface {

    private String token;
    private JDA discord;
    private Channels channels;
    private Config config;

    public DiscordInterface(Config config) {
        token = config.getToken();  
        this.config = config;
    }

    public void start() throws LoginException {
        discord = JDABuilder.createDefault(token).build();
        discord.getPresence().setActivity(Activity.playing("Minecraft"));
        discord.addEventListener(new Events());
        channels = new Channels(discord, config);
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
    }
}
