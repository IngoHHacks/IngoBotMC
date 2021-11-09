package tv.ingoh.minecraft.plugins.ingobotcore.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tv.ingoh.minecraft.plugins.ingobotcore.Main;

public class Events extends ListenerAdapter {

    Main m;
    Channels channels;

    public Events(Channels channels, Main m) {
        this.channels = channels;
        this.m = m;
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()){
            try {
                if (event.getChannel().getId().equals(Long.toString(channels.textChannel))) {
                    m.sendCommand(event.getMessage().getContentRaw());
                    event.getChannel().deleteMessageById(event.getMessageId()).queue();
                    event.getChannel().sendMessage("[IngoBot] " + event.getMessage().getContentRaw()).queue();
                }
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        try {
            if ((long)(event.getChannel().getIdLong()) == (channels.spreadsheetChannel) && event.getReaction().getReactionEmote().getName().equals("✅")) {
                String ign = event.getReaction().getTextChannel().retrieveMessageById(event.getMessageId()).complete().getContentRaw().split("`Minecraft Username`: ")[1].split("\n")[0].trim();
                m.whitelist(ign);
            }
        } catch (Exception ignore) {}
    }
}