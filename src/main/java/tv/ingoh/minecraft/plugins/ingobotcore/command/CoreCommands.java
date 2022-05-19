package tv.ingoh.minecraft.plugins.ingobotcore.command;

import java.awt.Color;
import java.security.SecureRandom;
import java.util.function.Function;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.minecraft.util.Tuple;
import tv.ingoh.minecraft.plugins.ingobotcore.IngoBot;
import tv.ingoh.minecraft.plugins.ingobotcore.IngoBotTabCompleter;
import tv.ingoh.minecraft.plugins.ingobotcore.Main;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;
import tv.ingoh.minecraft.plugins.ingobotcore.web.AsyncWebThread.Type;
import tv.ingoh.minecraft.plugins.ingobotcore.web.Query;
import tv.ingoh.minecraft.plugins.ingobotcore.web.WebThread;
import tv.ingoh.util.Colors;
import tv.ingoh.util.RandomTaglines;
import tv.ingoh.util.RandomThings;
import tv.ingoh.util.calculator.Calculator;

public class CoreCommands {

    static long imageCooldownTime = 0;
    static int count69 = -1;

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
            TextComponent selector = new TextComponent(sender);
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
                    wt.add(new Query(Type.CHAT, sender, new String[]{argsS, "false", "gpt2"}, isPublic));
                    return new CommandResult(ResultType.SUCCESS, command);
                case "FS":
                    if (!isPublic) {
                        if (senderP != null) senderP.sendMessage(ChatColor.GRAY + "YOU -> IngoBot: " + ChatColor.RESET + argsS + "...");
                        discord.sendDebug("{" + sender + "} " + argsS + "...");
                    } else {
                        discord.sendDebug("[" + sender + "] " + argsS + "...");
                    }
                    wt.add(new Query(Type.CHAT, sender, new String[]{argsS, "true", "gpt2"}, isPublic));
                    return new CommandResult(ResultType.SUCCESS, command);
                case "FURRY":
                    if (!isPublic) {
                        if (senderP != null) senderP.sendMessage(ChatColor.GRAY + "YOU -> IngoBot: " + ChatColor.RESET + " [F] " + argsS + "...");
                        discord.sendDebug("{" + sender + "} [F] " + argsS + "...");
                    } else {
                        discord.sendDebug("[" + sender + "] [F] " + argsS + "...");
                    }
                    wt.add(new Query(Type.CHAT, sender, new String[]{argsS, "true", "gpt2furry"}, isPublic));
                    return new CommandResult(ResultType.SUCCESS, command);
                case "S":
                    if (!isPublic) {
                        if (senderP != null) senderP.sendMessage(ChatColor.GRAY + "YOU -> IngoBot:" + ChatColor.RESET + " [S] " + argsS + "...");
                        discord.sendDebug("{" + sender + "} [S] " + argsS + "...");
                    } else {
                        discord.sendDebug("[" + sender + "] [S] " + argsS + "...");
                    }
                    wt.add(new Query(Type.CHAT, sender, new String[]{argsS, "true", "gptneo"}, isPublic));
                    return new CommandResult(ResultType.SUCCESS, command);
                case "COUNTDOWN":
                    if (args.length < 1) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, "0", "1+");
                    else if (args.length > 1) return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "1");
                    double time;
                    try {
                        time = Double.parseDouble(args[0]);
                    } catch (NumberFormatException ex) {
                        return new CommandResult(ResultType.EXECUTIONFAILUREEXCEPTION, "Cannot convert " + args[0] + " to a number, idiot.");
                    }
                    if (!isPublic) {
                        if (senderP != null) {
                            if (time > 300) {
                                return new CommandResult(ResultType.VALUEOUTOFRANGEEXCEPTION, Double.toString(time), "duration", "<=300");
                            }
                            boolean r = main.scheduleCountdown(senderP, time);
                            if (!r) return new CommandResult(ResultType.EXECUTIONFAILUREEXCEPTION, "Too many countdowns, idiot.");
                            IngoBot.sendMessageTo(ChatColor.GREEN + "Starting countdown for " + time + " seconds, idiot.", discord, isPublic, sender);
                        }
                    } else {
                        if (time > 60) {
                            return new CommandResult(ResultType.VALUEOUTOFRANGEEXCEPTION, Double.toString(time), "duration", "<=60");
                        }
                        boolean r = main.scheduleCountdown(null, time);
                        if (!r) return new CommandResult(ResultType.EXECUTIONFAILUREEXCEPTION, "Too many countdowns, idiot.");
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
                    else IngoBot.sendMessageTo("Only players may execute this command, idiot.", discord, isPublic, sender);
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
                    else IngoBot.sendMessageTo("Only players may execute this command, idiot.", discord, isPublic, sender);
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
                    if (System.currentTimeMillis() > imageCooldownTime) {
                        if (args.length < 1) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, Integer.toString(args.length), "1");
                        else if (args.length > 1) return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "1");
                        wt.add(new Query(Type.IMAGE, sender, new String[]{args[0]}, isPublic));
                        imageCooldownTime = System.currentTimeMillis() + 60000;
                        return new CommandResult(ResultType.SUCCESS, command);
                    } else {
                        return new CommandResult(ResultType.COOLDOWNEXCEPTION, Integer.toString((int)(Math.ceil(System.currentTimeMillis() - 60000))));
                    }
                case "HELP":
                    printCommandList(sender, isPublic, discord);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "69":
                    if (senderP != null) {
                        Bson q = Filters.eq("_69", true);
                        Function<FindIterable<Document>, String> f = new Function<FindIterable<Document>,String>() {
                            @Override
                            public String apply(FindIterable<Document> t) {
                                if (count69 == -1) {
                                    count69 = 0;
                                    MongoCursor<Document> it = t.iterator();
                                    while (it.hasNext()) {
                                        it.next();
                                        count69++;
                                    }
                                }
                                FindIterable<Document> current = t.filter(Filters.and(Filters.eq("_UUID", senderP.getUniqueId().toString()), Filters.eq("_69", true)));
                                Function<UpdateResult, String> f2 = new Function<UpdateResult,String>() {
                                    @Override
                                    public String apply(UpdateResult t) {
                                        if (t.wasAcknowledged()) {
                                            count69++;
                                            IngoBot.sendMessageTo(ChatColor.BLUE + "You did a 69! " + count69 + " players have done a 69!", discord, isPublic, sender);
                                        } else {
                                            IngoBot.sendMessageTo("Something went wrong trying to do a 69...", discord, isPublic, sender);
                                        }
                                        return "";
                                    };
                                };
                                if (current.first() == null) {
                                    Bson q2 = Filters.eq("_UUID", senderP.getUniqueId().toString());
                                    Bson u = Updates.set("_69", true);
                                    main.getMongo().update("players", q2, u, isPublic, f2);
                                } else {
                                    IngoBot.sendMessageTo("You have already used the 69 command, idiot.", discord, isPublic, sender);
                                }
                                return "";
                            }
                        };
                        main.getMongo().find("players", q, f);
                    } else {
                        IngoBot.sendMessageTo("Only players may execute this command, idiot.", discord, isPublic, sender);
                    }
                    return new CommandResult(ResultType.SUCCESS, command);
                case "WIKISEARCH":
                    wt.add(new Query(Type.WIKISEARCH, sender, new String[]{argsS}, isPublic));
                    return new CommandResult(ResultType.SUCCESS, command);
                case "ADVANCEMENT":
                    String uContent = "Advancement Command";
                    if (args.length > 0) uContent = argsS;
                    String content = ChatColor.GREEN + "[" + uContent + "]" + ChatColor.WHITE;
                    TextComponent tc = new TextComponent(content);
                    BaseComponent[] bc = new ComponentBuilder(uContent + "\nRun the advancement command").color(net.md_5.bungee.api.ChatColor.GREEN).create();
                    tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, bc));
                    ComponentBuilder builder = new ComponentBuilder()
                    .append(new TranslatableComponent("chat.type.advancement.task", selector, tc));
                    IngoBot.sendMessageToRaw(builder.create(), discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "PONG":
                    IngoBot.sendMessageTo("Ping!", discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "BRUH":
                    IngoBot.sendMessageTo("Bruh moment", discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "TEST":
                    IngoBot.sendMessageTo("Test command please ignore", discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "RJ":
                    ComponentBuilder builder2 = new ComponentBuilder()
                    .append(selector)
                    .color(net.md_5.bungee.api.ChatColor.YELLOW)
                    .append(" joined the game again");
                    IngoBot.sendMessageToRaw(builder2.create(), discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "DIE":
                    String uContent2 = "Die Command";
                    if (args.length > 0) uContent2 = argsS;
                    String content2 = ChatColor.LIGHT_PURPLE + "[" + uContent2 + "]" + ChatColor.WHITE;
                    TextComponent tc2 = new TextComponent(content2);
                    tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent("{id: \"minecraft:brick\", tag: {display: {Name: '{\"text\":\"" + ChatColor.LIGHT_PURPLE + "Die Command\"}'}, Enchantments: [{id: \"minecraft:sharpness\", lvl: 500s}]}, Count: 1b}")}));
                    TextComponent target = selector;
                    ComponentBuilder builder3 = new ComponentBuilder()
                    .append(new TranslatableComponent("death.attack.player.item", selector, target, tc2));
                    IngoBot.sendMessageToRaw(builder3.create(), discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "IDIOT":
                    String random = RandomThings.getRandomThing();
                    IngoBot.sendMessageTo("Nice " + ChatColor.AQUA + random  + ChatColor.WHITE + ", idiot!", discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "HUG":
                    String message = "(っ´▽｀)っ";
                    if (args.length == 2 && StringUtils.isNumeric(args[1])){
                        if (Long.parseLong(args[1]) <= 0 || Long.parseLong(args[1]) > Long.parseLong("1000000000000000000"))
                            message = "(っ˘̩╭╮˘̩)っ" + args[0];
                        else if (Long.parseLong(args[1]) <= 3)
                            message = "(っ´▽｀)っ" + args[0];
                        else if (Long.parseLong(args[1]) <= 6)
                            message = "╰(´︶`)╯" + args[0];
                        else if (Long.parseLong(args[1]) <= 9)
                            message = "(つ≧▽≦)つ" + args[0];
                        else
                            message = "(づ￣ ³￣)づ" + args[0] + "⊂(´・ω・｀⊂)";
                    } else if (args.length == 0) {
                        message = "(っ´▽｀)っ" + sender;
                    } else {
                        message = "(っ´▽｀)っ" + argsS;
                    }
                    IngoBot.sendMessageTo(message, discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "HINT":
                case "SECRET":
                case "TAGLINE":
                    String secret = RandomTaglines.getRandomTagline();
                    IngoBot.sendMessageTo(secret, discord, isPublic, sender);
                    return new CommandResult(ResultType.SUCCESS, command);
                default:
                    boolean foundColor = tryFormatColor(command, argsS, discord, isPublic, sender);
                    if (foundColor) return new CommandResult(ResultType.SUCCESS, command);
                    else return new CommandResult(ResultType.NOTEXISTEXCEPTION, command);

            }
        } catch (Exception e /* Catch all command errors */) {
            return new CommandResult(ResultType.EXECUTIONERROREXCEPTION, command);
        }
    }

    private static boolean tryFormatColor(String command, String argsS, DiscordInterface discord, boolean isPublic, String user) {
        boolean isColor = false;
        for (Colors c : Colors.values()) {
            if (c.name().replace("_", "").replace("'", "").replace("-", "_").toLowerCase().replace("grey", "gray").equals(command.toLowerCase().replace("_", "").replace("grey", "gray"))){
                isColor = true;
                if (argsS.length() > 0){
                    ComponentBuilder cb = new ComponentBuilder(argsS)
                    .color(net.md_5.bungee.api.ChatColor.of(c.color));
                    IngoBot.sendMessageToRaw(cb.create(), discord, isPublic, user);
                } else {
                    ComponentBuilder cb = new ComponentBuilder(user + " is " + c.name().replace("_"," "))
                    .color(net.md_5.bungee.api.ChatColor.of(c.color));
                    IngoBot.sendMessageToRaw(cb.create(), discord, isPublic, user);
                }
            }
        }
        if (command.charAt(0) == '#'){
            String aa = command.substring(1);
            if (aa.length() == 6) {
                int iget = Integer.parseInt(aa, 16);
                Color get = new Color(iget);
                if (argsS.length() > 0){
                    ComponentBuilder cb = new ComponentBuilder(argsS)
                    .color(net.md_5.bungee.api.ChatColor.of(get));
                    IngoBot.sendMessageToRaw(cb.create(), discord, isPublic, user);
                } else {
                    String best = Colors.getColorClosestTo(get);
                    ComponentBuilder cb = new ComponentBuilder(user + " is " + best.replace("_"," "))
                    .color(net.md_5.bungee.api.ChatColor.of(get));
                    IngoBot.sendMessageToRaw(cb.create(), discord, isPublic, user);
                }
                isColor = true;
            }
        }
        return isColor;
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

    public static void printCommandList(String sender, boolean isPublic, DiscordInterface discord) {
        String s = "List of commands:\n";
        for (String command : IngoBotTabCompleter.COMMANDS) {
            if (!s.equals("List of commands:\n")) s += ", ";
            s += command;
        }
        IngoBot.sendMessageTo(s, discord, isPublic, sender);
    }
}
