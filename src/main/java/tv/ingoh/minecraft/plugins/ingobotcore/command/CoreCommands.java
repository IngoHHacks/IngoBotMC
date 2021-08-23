package tv.ingoh.minecraft.plugins.ingobotcore.command;

import java.security.SecureRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import tv.ingoh.minecraft.plugins.ingobotcore.IngoBot;
import tv.ingoh.minecraft.plugins.ingobotcore.Main;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;
import tv.ingoh.minecraft.plugins.ingobotcore.web.AsyncWebThread.Type;
import tv.ingoh.minecraft.plugins.ingobotcore.web.Query;
import tv.ingoh.minecraft.plugins.ingobotcore.web.WebThread;
import tv.ingoh.util.calculator.Calculator;

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
                // TODO: Use objects instead of switch cases
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
                case "HOW":
                    IngoBot.sendMessageTo("§LHOW", discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "BOLD":
                case "ITALIC":
                case "UNDERLINED":
                case "STRIKETHROUGH":
                case "OBFUSCATED":
                    String formatted;
                    if (args.length > 0) formatted = format(command.toLowerCase(), argsS);
                    else formatted = format(command.toLowerCase(), sender);
                    IngoBot.sendMessageTo(formatted, discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "EYES":
                    IngoBot.sendMessageTo(":eyes:", discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "F":
                    IngoBot.sendMessageToRaw("￿￿￿\n￿\n￿￿￿\n￿\n￿", discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "RQ":
                    if (senderP != null) senderP.kickPlayer("Ragequit?");
                    return new CommandResult(ResultType.SUCCESS, command);
                case "CUM":
                    if (senderP != null) senderP.playSound(senderP.getLocation(), Sound.BLOCK_HONEY_BLOCK_BREAK, 100, 0.0f);
                    IngoBot.sendMessageTo("* " + sender + " cums", discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "RNG":
                    SecureRandom rng = new SecureRandom();
                    String out = "INVALID";
                    if (args.length >= 1) {
                        boolean list = false;
                        boolean ints = true;
                        if (args.length > 2) {
                            list = true;
                        } else {
                            for (String string : args) {
                                if (!string.matches("-?\\d+(\\.\\d+)?")) list = true;
                                if (string.contains(".")) ints = false;
                            }
                        }
                        if (!list) {
                            try {
                                if (args.length == 2) {
                                    if (ints) {
                                        int min = Math.min(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                                        int max = Math.max(Integer.parseInt(args[0]), Integer.parseInt(args[1])) + 1;
                                        out = Integer.toString(rng.nextInt(max - min) + min);
                                    } else {
                                        double min = Math.min(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
                                        double max = Math.max(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
                                        double pct = ((double)(rng.nextLong() / 2 + Long.MAX_VALUE / 2) / Long.MAX_VALUE);
                                        out = Double.toString(pct * (max - min) + min);
                                    }
                                } else {
                                    if (ints) {
                                        out = Integer.toString(rng.nextInt(Integer.parseInt(args[0]) + 1));
                                    } else {
                                        double pct = ((double)(rng.nextLong() / 2 + Long.MAX_VALUE / 2) / Long.MAX_VALUE);
                                        out = Double.toString(pct * Double.parseDouble(args[0]));
                                    }
                                }
                            } catch (Exception e) {
                                int index = rng.nextInt(args.length);
                                out = args[index];
                            }
                        } else {
                            int index = rng.nextInt(args.length);
                            out = args[index];
                        }
                        IngoBot.sendMessageTo(ChatColor.AQUA + "RNG: " + out, discord, isPublic, sender);
                    } else {
                        return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, Integer.toString(args.length), "1+");
                    }
                    return new CommandResult(ResultType.SUCCESS, command);
                case "IMAGE":
                    if (args.length < 1) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, Integer.toString(args.length), "1");
                    else if (args.length > 1) return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "1");
                    wt.add(new Query(Type.IMAGE, sender, new String[]{args[0]}, isPublic));
                    return new CommandResult(ResultType.SUCCESS, command);
                case "69":
                case "WIKISEARCH":
                case "HELP":
                // TODO
                    IngoBot.sendMessageTo("Command to be added.", discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                default:
                    return new CommandResult(ResultType.NOTEXISTEXCEPTION, command);

            }  
        } catch (Exception e /* Catch all command errors */) {
            return new CommandResult(ResultType.EXECUTIONERROREXCEPTION, command);
        }
    }

    private static String format(String formatting, String msg) {
        switch (formatting) {
            case "bold":
                return "§L" + msg;
            case "italic":
                return "§O" + msg;
            case "underlined":
                return "§N" + msg;
            case "strikethrough":
                return "§M" + msg;
            case "obfuscated":
                return "§K" + msg;
        }
        return msg;
    }

    public static void scheduleCommand(Main main, String string, String[] args, String name, WebThread wThread, boolean b, DiscordInterface discord) {
        main.scheduleCommand(new ScheduledCommand(main, string, args, name, wThread, b, discord));
    }
}
