package tv.ingoh.minecraft.plugins.ingobotcore;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;

public class IngoBot {

    public static void sendMessage(String message, DiscordInterface discord) {
        Bukkit.broadcastMessage("<IngoBot> " + message);
        discord.sendChat("<IngoBot> " + message);
    }

    public static void sendMessage(String message, CommandSender user, DiscordInterface discord) {
        user.sendMessage(ChatColor.GRAY + "IngoBot -> YOU: " + ChatColor.RESET + message);
        discord.sendChat("IngoBot -> " + user.getName() + ": " + message);
    }


    public static void sendMessageTo(String string, DiscordInterface discord, boolean isPublic, String sender) {
        if (isPublic) sendMessage(string, discord);
        else {
            CommandSender s = Bukkit.getPlayer(sender);
            if (s != null) sendMessage(string, s, discord);
        }
	}

    public static void sendMessageRaw(String message, DiscordInterface discord) {
        Bukkit.broadcastMessage(message);
        discord.sendChat(message);
    }

    public static void sendMessageRaw(String message, CommandSender user, DiscordInterface discord) {
        user.sendMessage(message);
        discord.sendChat("IngoBot -> " + user.getName() + ": " + message);
    }

    public static void sendMessageFromAsync(Main main, String message) {
        main.scheduleMessage(new Message("<IngoBot> " + message, null));
    }

    public static void sendMessageFromAsync(Main main, String message, CommandSender user) {
        main.scheduleMessage(new Message(ChatColor.GRAY + "IngoBot -> YOU: " + ChatColor.RESET + message, user.getName()));
    }

    public static void sendMessageFromAsync(Main main, String message, String user) {
        main.scheduleMessage(new Message(ChatColor.GRAY + "IngoBot -> YOU: " + ChatColor.RESET + message, user));
    }

    public static void sendMessageRawFromAsync(Main main, String message) {
        main.scheduleMessage(new Message(message, null));
    }

    public static void sendMessageRawFromAsync(Main main, String message, CommandSender user) {
        main.scheduleMessage(new Message(message, user.getName()));
    }

    public static void sendMessageRawFromAsync(Main main, String message, String user) {
        main.scheduleMessage(new Message(message, user));
    }
    
}
