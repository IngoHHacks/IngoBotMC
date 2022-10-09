package net.ingoh.minecraft.plugins.ingobotmc.command;

import net.ingoh.minecraft.plugins.ingobotmc.Main;
import net.ingoh.minecraft.plugins.ingobotmc.discord.DiscordInterface;
import net.ingoh.minecraft.plugins.ingobotmc.web.WebThread;

public class ScheduledCommand {

    Main main;
    String string;
    String[] args;
    String sender;
    WebThread wThread;
    boolean isPublic;
    DiscordInterface discord;

    public ScheduledCommand(Main main, String string, String[] args, String sender, WebThread wThread, boolean isPublic, DiscordInterface discord) {
        this.main = main;
        this.string = string;
        this.args = args;
        this.sender = sender;
        this.wThread = wThread;
        this.isPublic = isPublic;
        this.discord = discord;
    }

    public String getString() {
        return string;
    }

    public String[] getArgs() {
        return args;
    }

    public String getSender() {
        return sender;
    }

    public WebThread getwThread() {
        return wThread;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public DiscordInterface getDiscord() {
        return discord;
    }

    public Main getMain() {
        return main;
    }

}
