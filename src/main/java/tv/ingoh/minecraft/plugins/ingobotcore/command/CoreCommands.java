package tv.ingoh.minecraft.plugins.ingobotcore.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;
import tv.ingoh.minecraft.plugins.ingobotcore.web.Query;
import tv.ingoh.minecraft.plugins.ingobotcore.web.WebThread;
import tv.ingoh.minecraft.plugins.ingobotcore.web.AsyncWebThread.Type;
import tv.ingoh.util.calculator.Calculator;
import tv.ingoh.minecraft.plugins.ingobotcore.IngoBot;
import tv.ingoh.minecraft.plugins.ingobotcore.Main;

public class CoreCommands {

    public static CommandResult executeCommand(ScheduledCommand cmd) {
        return executeCommand(cmd.getString(), cmd.getArgs(), cmd.getSender(), cmd.getwThread(), cmd.isPublic(), cmd.getDiscord());
    }

    public static CommandResult executeCommand(String command, String[] args, String sender, WebThread wt, boolean isPublic, DiscordInterface discord) {
        try {
            Player senderP = Bukkit.getPlayer(sender);
            String argsS = "";
            String argsF = "";
            for (String s : args) {
                if (argsS != "") argsS += " ";
                argsS += s;
                argsF += s;
            }

            switch (command.toUpperCase()) {
                case "CALC":
                    String[] result = Calculator.calculate(argsF);
                    int i = 0;
                    if (result.length > 1) {
                        discord.sendDebug("CALC ERROR");
                        for (String r : result) {
                            if (i == 0) IngoBot.sendMessageTo(ChatColor.AQUA + argsS + ChatColor.RED + " IS " + ChatColor.GOLD + r, discord, isPublic, sender);
                            discord.sendDebug(r);
                            i++;
                        }
                    } else {
                        IngoBot.sendMessageTo(ChatColor.AQUA + argsS + ChatColor.RED + " IS " + ChatColor.GOLD + result[0], discord, isPublic, sender);
                    }
                    return new CommandResult(ResultType.SUCCESS, command);
                case "C":
                    if (!isPublic) {
                        if (senderP != null) senderP.sendMessage(ChatColor.GRAY + "YOU -> IngoBot: " + ChatColor.RESET + argsS);
                        discord.sendDebug("{" + sender + "} " + argsS);
                    } else {
                        discord.sendDebug("[" + sender + "] " + argsS);
                    }
                    wt.add(new Query(Type.CHAT, sender, new String[]{argsS, "false"}, isPublic));
                    return new CommandResult(ResultType.SUCCESS, command);
                case "FS":
                    if (!isPublic) {
                        if (senderP != null) senderP.sendMessage(ChatColor.GRAY + "YOU -> IngoBot: " + ChatColor.RESET + argsS + "...");
                        discord.sendDebug("{" + sender + "} " + argsS + "...");
                    } else {
                        discord.sendDebug("[" + sender + "] " + argsS + "...");
                    }
                    wt.add(new Query(Type.CHAT, sender, new String[]{argsS, "true"}, isPublic));
                    return new CommandResult(ResultType.SUCCESS, command);
                default:
                    return new CommandResult(ResultType.NOTEXISTEXCEPTION, command);

            }  
        } catch (Exception e /* Catch all command errors */) {
            return new CommandResult(ResultType.EXECUTIONXCEPTION, command);
        }
    }

    public static void scheduleCommand(Main main, String string, String[] args, String name, WebThread wThread, boolean b, DiscordInterface discord) {
        main.scheduleCommand(new ScheduledCommand(string, args, name, wThread, b, discord));
    }
}
