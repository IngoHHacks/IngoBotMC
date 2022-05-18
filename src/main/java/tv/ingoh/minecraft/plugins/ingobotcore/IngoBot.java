package tv.ingoh.minecraft.plugins.ingobotcore;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;

public class IngoBot {

    public static void sendMessage(String message, DiscordInterface discord) {
        Bukkit.broadcastMessage("<IngoBot> " + message);
        discord.sendChat("<IngoBot> " + message, true);
    }

    public static void sendMessage(String message, CommandSender user, DiscordInterface discord) {
        user.sendMessage(ChatColor.GRAY + "IngoBot -> YOU: " + ChatColor.RESET + message);
        discord.sendChat("IngoBot -> " + user.getName() + ": " + message, false);
    }


    public static void sendMessageTo(String string, DiscordInterface discord, boolean isPublic, String sender) {
        if (isPublic) sendMessage(string, discord);
        else {
            CommandSender s = Bukkit.getPlayer(sender);
            if (s != null) sendMessage(string, s, discord);
        }
	}

    public static void sendMessageToRaw(String message, DiscordInterface discord, boolean isPublic, String sender) {
        if (isPublic) sendMessageRaw(message, discord);
        else {
            CommandSender s = Bukkit.getPlayer(sender);
            if (s != null) sendMessageRaw(message, s, discord);
        }
	}

    public static void sendMessageToRaw(BaseComponent[] message, DiscordInterface discord, boolean isPublic, String sender) {
        if (isPublic) sendMessageRaw(message, discord);
        else {
            CommandSender s = Bukkit.getPlayer(sender);
            if (s != null) sendMessageRaw(message, s, discord);
        }
	}

    public static void sendMessageRaw(String message, DiscordInterface discord) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendRawMessage(message);
        }
        discord.sendChat(message, true);
    }

    public static void sendMessageRaw(BaseComponent[] message, DiscordInterface discord) {
        Bukkit.getServer().spigot().broadcast(message);
        for (BaseComponent component : message) {
            String msg = component.toPlainText();
            if (component.isUnderlined()) msg = "__" + msg + "__";
            if (component.isBold()) msg = "**" + msg + "**";
            if (component.isItalic()) msg = "*" + msg + "*";
            if (component.isStrikethrough()) msg = "~~" + msg + "~~";
            if (component.isObfuscated()) msg = "||" + msg + "||";
            discord.sendChat(msg);
            if (component.getClickEvent() != null && component.getClickEvent().getAction() == Action.OPEN_URL) {
                discord.sendChat("(" + component.getClickEvent().getValue() + ")");
            }
        }
    }

    public static void sendMessageRaw(String message, CommandSender user, DiscordInterface discord) {
        user.sendMessage(message);
        discord.sendChat("(to " + user.getName() + ") " + message, false);
    }

    public static void sendMessageRaw(BaseComponent[] message, CommandSender user, DiscordInterface discord) {
        user.spigot().sendMessage(message);
        for (BaseComponent component : message) {
            String msg = component.toPlainText();
            if (component.isUnderlined()) msg = "__" + msg + "__";
            if (component.isBold()) msg = "**" + msg + "**";
            if (component.isItalic()) msg = "*" + msg + "*";
            if (component.isStrikethrough()) msg = "~~" + msg + "~~";
            if (component.isObfuscated()) msg = "||" + msg + "||";
            discord.sendChat("(to " + user.getName() + ") " + msg);
            if (component.getClickEvent() != null && component.getClickEvent().getAction() == Action.OPEN_URL) {
                discord.sendChat("(" + component.getClickEvent().getValue() + ")");
            }
        }
    }

    public static void sendMessagesFromAsync(Main main, String messages) {
        String[] msgs = messages.split("\n");
        main.scheduleMessage(new Message("<IngoBot> " + msgs[0], null));
        int i = 1;
        while (msgs.length > i) {
            if (Math.random() > 0.35) break;
            main.scheduleMessage(new Message("<" + randomBotName() + "> " + msgs[i], null, 3000 * i + (int)(Math.random() * 1000)));
            i++;
        }
    }
    public final static String[] USERNAMES = {"IngoHBot", "VicBot", "StarsBot", "SimpleBot", "tangyBot", "mispletBot", "matthewBot", "OmegaBot", "plexBot", "ArcBot", "SpookedBot", "snakeBot", "VeritasBot", "TimmyBot", "captainNedaBot", "PyroBot", "RacoonixBot", "s0undBot", "nathanBot", "B_jaminBot", "NkdSquidBot", "frigateBot", "bl1ngbl0ngBot", "HAX0RBot", "Cha0sBot", "glitchBot", "SamuelBot", "FoxyProxyBot", "TubBot", "DoubleLBot", "KitLemonBot", "ZynBot"};

    private static String randomBotName() {
        return USERNAMES[(int)(Math.random() * USERNAMES.length)];
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

    public static void sendFormattedMessageFromAsync(Main main, String message) {
        main.scheduleMessage(new FormattedMessage(message, null));
    }

}
