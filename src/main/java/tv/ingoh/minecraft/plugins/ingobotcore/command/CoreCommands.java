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
        return executeCommand(cmd.main, cmd.getString(), cmd.getArgs(), cmd.getSender(), cmd.getwThread(), cmd.isPublic(), cmd.getDiscord());
    }

    public static CommandResult executeCommand(Main main, String command, String[] args, String sender, WebThread wt, boolean isPublic, DiscordInterface discord) {
        try {
            Player senderP = Bukkit.getPlayer(sender);
            // TODO: Use StringBuilder
            String argsS = "";
            String argsF = "";
            for (String s : args) {
                if (argsS != "") argsS += " ";
                argsS += s;
                argsF += s;
            }

            switch (command.toUpperCase()) {
                case "CALC":
                    if (args.length > 0) {
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
                    } else {
                        return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, "0", "1+");
                    }
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
                case "COUNTDOWN":
                    if (args.length < 1) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, "0", "1+");
                    else if (args.length > 1) return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "1");
                    double time;
                    try {
                        time = Double.parseDouble(args[0]);
                    } catch (NumberFormatException ex) {
                        return new CommandResult(ResultType.EXECUTIONFAILUREEXCEPTION, "Cannot convert " + args[0] + " to a number.");
                    }
                    if (!isPublic) {
                        if (senderP != null) {
                            if (time > 300) {
                                return new CommandResult(ResultType.VALUEOUTOFRANGEEXCEPTION, Double.toString(time), "duration", "<=300");
                            }
                            boolean r = main.scheduleCountdown(senderP, time);
                            if (!r) return new CommandResult(ResultType.EXECUTIONFAILUREEXCEPTION, "Too many countdowns.");
                            IngoBot.sendMessageTo(ChatColor.GREEN + "Starting countdown for " + time + " seconds.", discord, isPublic, sender);
                        }
                    } else {
                        if (time > 60) {
                            return new CommandResult(ResultType.VALUEOUTOFRANGEEXCEPTION, Double.toString(time), "duration", "<=60");
                        }
                        boolean r = main.scheduleCountdown(null, time);
                        if (!r) return new CommandResult(ResultType.EXECUTIONFAILUREEXCEPTION, "Too many countdowns.");
                    }
                    return new CommandResult(ResultType.SUCCESS, command);
                case "USERINFO":
                    if (args.length <= 1) {
                        wt.add(new Query(Type.USERINFO, sender, args, isPublic));
                    } else {
                        return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "1-");
                    }
                    return new CommandResult(ResultType.SUCCESS, command);
                case "PING":
                    if (args.length > 0) return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "0");
                    if (senderP != null) IngoBot.sendMessageTo("Ping: " + senderP.getPing(), discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                default:
                    return new CommandResult(ResultType.NOTEXISTEXCEPTION, command);

            }  
        } catch (Exception e /* Catch all command errors */) {
            return new CommandResult(ResultType.EXECUTIONERROREXCEPTION, command);
        }
    }

    public static void scheduleCommand(Main main, String string, String[] args, String name, WebThread wThread, boolean b, DiscordInterface discord) {
        main.scheduleCommand(new ScheduledCommand(main, string, args, name, wThread, b, discord));
    }
}
