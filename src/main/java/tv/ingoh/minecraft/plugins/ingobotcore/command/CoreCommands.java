package tv.ingoh.minecraft.plugins.ingobotcore.command;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.map.CraftMapRenderer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.minecraft.world.level.saveddata.maps.WorldMap;
import net.minecraft.world.level.material.MaterialMapColor;
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

    static HashMap<String, Query> lastQueries = new HashMap<>();

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
                case "CALCULATE":
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
                case "CONVERSE":
                case "C":
                    if (!isPublic) {
                        if (senderP != null) senderP.sendMessage(ChatColor.GRAY + "YOU -> IngoBot: " + ChatColor.RESET + argsS);
                        discord.sendDebug("{" + sender + "} " + argsS);
                    } else {
                        discord.sendDebug("[" + sender + "] " + argsS);
                    }
                    Query qq = new Query(Type.CHAT, sender, new String[]{argsS, "false", "gpt2"}, isPublic);
                    wt.add(qq);
                    lastQueries.put(isPublic ? "*" : sender, qq);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "RETRY":
                    if (!isPublic) {
                        if (senderP != null) senderP.sendMessage(ChatColor.GRAY + "YOU -> IngoBot: " + ChatColor.RESET + "[RETRY]");
                        discord.sendDebug("{" + sender + "} [RETRY]");
                    } else {
                        discord.sendDebug("[" + sender + "] [RETRY]");
                    }
                    Query qr = lastQueries.get(isPublic ? "*" : sender);
                    if (qr != null) {
                        wt.undo(qr);
                        wt.add(qr);
                    } else {
                        IngoBot.sendMessageTo(ChatColor.RED + "There is nothing to retry, idiot.", discord, isPublic, sender);
                    }
                    return new CommandResult(ResultType.SUCCESS, command);
                case "FINISHSENTENCE":
                case "FS":
                    if (!isPublic) {
                        if (senderP != null) senderP.sendMessage(ChatColor.GRAY + "YOU -> IngoBot: " + ChatColor.RESET + argsS + "...");
                        discord.sendDebug("{" + sender + "} " + argsS + "...");
                    } else {
                        discord.sendDebug("[" + sender + "] " + argsS + "...");
                    }
                    Query qq2 = new Query(Type.CHAT, sender, new String[]{argsS, "true", "gpt2"}, isPublic);
                    wt.add(qq2);
                    lastQueries.put(isPublic ? "*" : sender, qq2);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "FURRY":
                    if (!isPublic) {
                        if (senderP != null) senderP.sendMessage(ChatColor.GRAY + "YOU -> IngoBot: " + ChatColor.RESET + " [F] " + argsS + "...");
                        discord.sendDebug("{" + sender + "} [F] " + argsS + "...");
                    } else {
                        discord.sendDebug("[" + sender + "] [F] " + argsS + "...");
                    }
                    Query qq3 = new Query(Type.CHAT, sender, new String[]{argsS, "true", "gpt2furry"}, isPublic);
                    wt.add(qq3);
                    lastQueries.put(isPublic ? "*" : sender, qq3);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "STORY":
                case "S":
                    if (!isPublic) {
                        if (senderP != null) senderP.sendMessage(ChatColor.GRAY + "YOU -> IngoBot:" + ChatColor.RESET + " [S] " + argsS + "...");
                        discord.sendDebug("{" + sender + "} [S] " + argsS + "...");
                    } else {
                        discord.sendDebug("[" + sender + "] [S] " + argsS + "...");
                    }
                    Query qq4 = new Query(Type.CHAT, sender, new String[]{argsS, "true", "gptneo"}, isPublic);
                    wt.add(qq4);
                    lastQueries.put(isPublic ? "*" : sender, qq4);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "HISTORY":
                case "HIST":
                    wt.printHist(sender, isPublic);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "CLEARHISTORY":
                case "CLEARHIST":
                case "CH":
                    wt.clearHist(sender, isPublic);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "UNDOHISTORY":
                case "UNDOHIST":
                case "UH":
                    wt.undoHist(sender, isPublic);
                    return new CommandResult(ResultType.SUCCESS, command);
                case "COUNTDOWN":
                case "CD":
                case "COUNTUP":
                case "CU":
                    if (args.length < 1) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, "0", "1");
                    else if (args.length > 1) return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "1");
                    double time;
                    boolean uppies = false;
                    try {
                        double d = Double.parseDouble(args[0]);
                        if (d < 0) uppies = true;
                        time = Math.abs(d);
                    } catch (NumberFormatException ex) {
                        return new CommandResult(ResultType.EXECUTIONFAILUREEXCEPTION, "Cannot convert " + args[0] + " to a number, idiot.");
                    }
                    if (command.toUpperCase().equals("COUNTUP") || command.toUpperCase().equals("CU")) uppies = !uppies;
                    if (!isPublic) {
                        if (senderP != null) {
                            if (time > 300) {
                                return new CommandResult(ResultType.VALUEOUTOFRANGEEXCEPTION, Double.toString(time), "duration", "<=300");
                            }
                            boolean r = main.scheduleCountdown(senderP, time, uppies);
                            if (!r) return new CommandResult(ResultType.EXECUTIONFAILUREEXCEPTION, "Too many countdowns, idiot.");
                            IngoBot.sendMessageTo(ChatColor.GREEN + "Starting count" + (uppies ? "up" : "down") + "for " + time + " seconds.", discord, isPublic, sender);
                        }
                    } else {
                        if (time > 60) {
                            return new CommandResult(ResultType.VALUEOUTOFRANGEEXCEPTION, Double.toString(time), "duration", "<=60");
                        }
                        boolean r = main.scheduleCountdown(null, time, uppies);
                        if (!r) return new CommandResult(ResultType.EXECUTIONFAILUREEXCEPTION, "Too many countdowns, idiot.");
                        IngoBot.sendMessageTo(ChatColor.GREEN + "Starting count" + (uppies ? "up" : "down") + " for " + time + " seconds.", discord, isPublic, sender);
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
                case "BONK":
                    String formatted2;
                    if (args.length > 0) formatted2 = argsS;
                    else formatted2 = "IngoBot";
                    if (formatted2.toLowerCase().equals("i") || formatted2.toLowerCase().endsWith(" i")) {
                        IngoBot.sendMessageTo("* " + formatted2 + " get bonked", discord, isPublic, sender);
                    } else if (formatted2.toLowerCase().equals("you") || formatted2.toLowerCase().endsWith(" you")) {
                        IngoBot.sendMessageTo("* " + formatted2 + " get bonked", discord, isPublic, sender);
                    }
                    else IngoBot.sendMessageTo("* " + formatted2 + " gets bonked", discord, isPublic, sender);
                    if (formatted2.toLowerCase().equals("ingobot")) {
                        main.bonk();
                    }
                    return new CommandResult(ResultType.SUCCESS, command);
                case "BAN":
                    String formatted3;
                    if (args.length > 0) formatted3 = argsS;
                    else formatted3 = "IngoBot";
                    if (formatted3.toLowerCase().equals("i") || formatted3.toLowerCase().endsWith(" i")) {
                        IngoBot.sendMessageTo("* " + formatted3 + " am banned", discord, isPublic, sender);
                    } else if (formatted3.toLowerCase().equals("you") || formatted3.toLowerCase().endsWith(" you")) {
                        IngoBot.sendMessageTo("* " + formatted3 + " are banned", discord, isPublic, sender);
                    } else  IngoBot.sendMessageTo("* " + formatted3 + " is banned", discord, isPublic, sender);
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
                        if (args.length == 1) {
                            switch (args[0]) {
                                case "coin", "coinflip" -> {
                                    out = rng.nextInt(2) == 0 ? "HEADS" : "TAILS";
                                }
                            }
                            if (args[0].startsWith("d") && args[0].substring(1).matches("-?\\d+(\\.\\d+)?")) {
                                if (Integer.parseInt(args[0].substring(1)) == 0) out = "0";
                                else out = Integer.toString(1 + rng.nextInt(Math.abs(Integer.parseInt(args[0].substring(1)))));
                            }
                        }
                        if (out.equals("INVALID")) {
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
                                            int mul = (Integer.parseInt(args[0]) < 0) ? -1 : 1;
                                            out = Integer.toString(rng.nextInt(Math.abs(Integer.parseInt(args[0])) + 1) * mul);
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
                case "PRINTMAP":
                    if (senderP != null) {
                        ItemStack stack = senderP.getInventory().getItemInMainHand();
                        if (stack.getType() != null && stack.getType() == Material.FILLED_MAP) {
                            MapMeta mm = (MapMeta) stack.getItemMeta();
                            MapRenderer renderer = mm.getMapView().getRenderers().get(0);
                            Field f1 = CraftMapRenderer.class.getDeclaredField("worldMap");
                            f1.setAccessible(true);
                            WorldMap worldMap = (WorldMap) f1.get(renderer);
                            Field f2 = worldMap.getClass().getDeclaredField("g");
                            f2.setAccessible(true);
                            byte[] materials = (byte[]) f2.get(worldMap); // colors
                            byte[] colors = new byte[materials.length*3];

                            int sz = (int) Math.sqrt(materials.length);

                            BufferedImage img = new BufferedImage(sz, sz, BufferedImage.TYPE_INT_RGB);
                            int i = 0;
                            for(int r=0; r<sz; r++)
                            {
                                for(int c=0; c<sz; c++)
                                {
                                    int index=r*sz+c;
                                    int dec = MaterialMapColor.b(materials[i]);
                                    int red = dec & 0xff;
                                    int green = (dec >> 8) & 0xff;
                                    int blue = (dec >> 16) & 0xff;
                                    Color col = new Color(red, green, blue);
                                    img.setRGB(c, r, col.getRGB());
                                    i++;
                                }
                            }
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(img, "png", baos);
                            byte[] imgArr = baos.toByteArray();
                            String b64 = Base64.getEncoder().encodeToString(imgArr);
                            TextComponent comp = new TextComponent("[CLICK HERE TO COPY IMAGE]");
                            comp.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                            comp.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "data:image/png;base64," + b64));
                            senderP.spigot().sendMessage(comp);
                        } else {
                            IngoBot.sendMessageTo("You are not holding a map, idiot.", discord, isPublic, sender);
                        }
                    } else {
                        IngoBot.sendMessageTo("Only players may execute this command, idiot.", discord, isPublic, sender);
                    }
                    return new CommandResult(ResultType.SUCCESS, command);
                case "PRINTALLMAPS":
                    if (senderP == null) {
                        IngoBot.sendMessageTo("Only players may execute this command, idiot.", discord, isPublic, sender);
                        return new CommandResult(ResultType.SUCCESS, command);
                    }
                    if (senderP.isOp()) {
                        int i2 = 0;
                        while (true) {
                            WorldMap map = (WorldMap) ((CraftServer) Bukkit.getServer()).getServer().C().t().a(WorldMap::b, "map_" + i2);
                            if (map == null) {
                                break;
                            }
                            try {
                                Field f3 = map.getClass().getDeclaredField("g");
                                f3.setAccessible(true);
                                byte[] materials = (byte[]) f3.get(map); // colors
                                byte[] colors = new byte[materials.length*3];

                                int sz = (int) Math.sqrt(materials.length);

                                BufferedImage img = new BufferedImage(sz, sz, BufferedImage.TYPE_INT_RGB);
                                int i3 = 0;
                                for(int r=0; r<sz; r++)
                                {
                                    for(int c=0; c<sz; c++)
                                    {
                                        int index=r*sz+c;
                                        int dec = MaterialMapColor.b(materials[i3]);
                                        int red = dec & 0xff;
                                        int green = (dec >> 8) & 0xff;
                                        int blue = (dec >> 16) & 0xff;
                                        Color col = new Color(red, green, blue);
                                        img.setRGB(c, r, col.getRGB());
                                        i3++;
                                    }
                                }
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ImageIO.write(img, "png", baos);
                                byte[] imgArr = baos.toByteArray();
                                String b64 = Base64.getEncoder().encodeToString(imgArr);
                                Document q = new Document().append("_id", i2);
                                Bson u = Updates.combine(Updates.set("base64", b64), Updates.set("x", map.c), Updates.set("z", map.d), Updates.set("dimension", map.e.a().a()), Updates.set("scale", map.f), Updates.set("locked", map.h));
                                main.getMongo().update("maps", q, u, false);
                            } catch (Exception ignore) {}
                            i2++;
                        }
                    } else {
                        IngoBot.sendMessageTo("Only Operators may execute this command, idiot.", discord, isPublic, sender);
                    }
                    return new CommandResult(ResultType.SUCCESS, command);
                case "STATISTICS":
                case "STATISTIC":
                case "STATS":
                case "STAT":
                    if (senderP == null) {
                        IngoBot.sendMessageTo("Only players may execute this command, idiot.", discord, isPublic, sender);
                        return new CommandResult(ResultType.SUCCESS, command);
                    }
                    if (args.length < 1) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, Integer.toString(args.length), "1*");
                    else {
                        String[] split = argsS.split(" ",2);
                        String type = split[0].toLowerCase();
                        String argsS2 = split.length > 1 ? split[1] : "";
                        switch (type) {
                            case "kill", "kills", "killed" -> {
                                String entity = argsS2.replace(" ", "_").toUpperCase();
                                try {
                                    EntityType entType = EntityType.valueOf(entity);
                                    int value = senderP.getStatistic(Statistic.KILL_ENTITY, entType);
                                    IngoBot.sendMessageTo(sender + " has " + ChatColor.AQUA + value + " " + entity.replace("_", " ") + ChatColor.RESET + " kills.", discord, isPublic, sender);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(entity + " is not a valid entity, idiot.", discord, isPublic, sender);
                                }
                            }
                            case "mine", "mined" -> {
                                String item = argsS2.replace(" ", "_").toUpperCase();
                                try {
                                    Material mat = Material.valueOf(item);
                                    int value = senderP.getStatistic(Statistic.MINE_BLOCK, mat);
                                    IngoBot.sendMessageTo(sender + " has mined " + ChatColor.AQUA + value + " " + item.replace("_", " ") + ChatColor.RESET + ".", discord, isPublic, sender);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(item + " is not a valid item, idiot.", discord, isPublic, sender);
                                }
                            }
                            case "break", "broken" -> {
                                String item = argsS2.replace(" ", "_").toUpperCase();
                                try {
                                    Material mat = Material.valueOf(item);
                                    int value = senderP.getStatistic(Statistic.BREAK_ITEM, mat);
                                    IngoBot.sendMessageTo(sender + " has broken " + ChatColor.AQUA + value + " " + item.replace("_", " ") + ChatColor.RESET + ".", discord, isPublic, sender);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(item + " is not a valid item, idiot.", discord, isPublic, sender);
                                }
                            }
                            case "craft", "crafted" -> {
                                String item = argsS2.replace(" ", "_").toUpperCase();
                                try {
                                    Material mat = Material.valueOf(item);
                                    int value = senderP.getStatistic(Statistic.CRAFT_ITEM, mat);
                                    IngoBot.sendMessageTo(sender + " has crafted " + ChatColor.AQUA + value + " " + item.replace("_", " ")+ ChatColor.RESET + ".", discord, isPublic, sender);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(item + " is not a valid item, idiot.", discord, isPublic, sender);
                                }
                            }
                            case "use", "used", "uses" -> {
                                String item = argsS2.replace(" ", "_").toUpperCase();
                                try {
                                    Material mat = Material.valueOf(item);
                                    int value = senderP.getStatistic(Statistic.USE_ITEM, mat);
                                    IngoBot.sendMessageTo(sender + " has used " + ChatColor.AQUA + value + " " + item.replace("_", " ") + ChatColor.RESET + ".", discord, isPublic, sender);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(item + " is not a valid item, idiot.", discord, isPublic, sender);
                                }
                            }
                            case "pickup", "pick_up" -> {
                                String item = argsS2.replace(" ", "_").toUpperCase();
                                try {
                                    Material mat = Material.valueOf(item);
                                    int value = senderP.getStatistic(Statistic.PICKUP, mat);
                                    IngoBot.sendMessageTo(sender + " has picked up " + ChatColor.AQUA + value + " " + item.replace("_", " ") + ChatColor.RESET + ".", discord, isPublic, sender);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(item + " is not a valid item, idiot.", discord, isPublic, sender);
                                }
                            }
                            case "drop", "dropped" -> {
                                String item = argsS2.replace(" ", "_").toUpperCase();
                                try {
                                    Material mat = Material.valueOf(item);
                                    int value = senderP.getStatistic(Statistic.DROP, mat);
                                    IngoBot.sendMessageTo(sender + " has dropped " + ChatColor.AQUA + value + " " + item.replace("_", " ") + ChatColor.RESET + ".", discord, isPublic, sender);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(item + " is not a valid item, idiot.", discord, isPublic, sender);
                                }
                            }
                            default -> {
                                String statistic = argsS.replace(" ", "_").toUpperCase();
                                try {
                                    Statistic stat = Statistic.valueOf(statistic);
                                    int value = senderP.getStatistic(stat);
                                    IngoBot.sendMessageTo(sender + " has " + ChatColor.AQUA + value + " " + statistic.replace("_", " ") + ChatColor.RESET + ".", discord, isPublic, sender);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(statistic + " is not a valid statistic, idiot.", discord, isPublic, sender);
                                }
                            }
                                                        
                        }
                    }
                    return new CommandResult(ResultType.SUCCESS, command);
                case "SWC":
                    if (senderP == null) {
                        IngoBot.sendMessageTo("Only players may execute this command, idiot.", discord, isPublic, sender);
                        return new CommandResult(ResultType.SUCCESS, command);
                    }
                    if (args.length < 1) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, Integer.toString(args.length), "1+");
                    switch (args[0].toLowerCase()) {
                        case "claim":
                        if (args.length < 2) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, Integer.toString(args.length), "2");
                        if (args.length > 2) return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "2");
                        if (args[1].toLowerCase().equals("milestones")) {
                            main.claimMilestones(isPublic, senderP);
                        } else {
                            IngoBot.sendMessageTo(args[1] + " is not a valid reward, idiot.", discord, isPublic, sender);
                        }
                            break;
                        case "get":
                            String userg = sender;
                            if (args.length == 2) {
                                if (senderP.isOp()) userg = args[1];
                            }
                            main.getSWC(sender, isPublic, userg, true);
                            break;
                        case "rewards":
                        /*
                            IngoBot.sendMessageTo(ChatColor.BLUE + "<spawn> [20] SWC: Teleport to world spawn.\n" +
                                                  ChatColor.BLUE + "<*manual> [50] SWC: Pick any unusual dragon head.\n" +
                                                  ChatColor.BLUE + "<*manual> [50-100] SWC: Pick any unusual helmet (100 diamond | 80 turtle | 50 other).\n" +
                                                  ChatColor.BLUE + "<*manual> [50-100] SWC: Pick any unusual elytra (100 effect AND song | 50 effect OR song).\n" +
                                                  ChatColor.BLUE + "<*manual> [500] SWC: Choose 5 physical stickers from Community Commissions (IRL).\n" +
                                                  ChatColor.BLUE + "<chicken> [500] SWC: Summon a random Legendary Resource Chicken at your current location.\n" +
                                                  ChatColor.BLUE + "<*manual> [500] SWC: Create any non-beneficial, non-destuctive IngoBot command.\n" +
                                                  ChatColor.BLUE + "- Regular SWC rewards can be claimed with " + ChatColor.LIGHT_PURPLE + "swc claim <name>, or by asking IngoH for <*manual> rewards.", discord, isPublic, sender);
                        */
                            IngoBot.sendMessageToRaw(ChatColor.BLUE + "Regular SWC rewards will be added soon.", discord, isPublic, sender);
                            IngoBot.sendMessageToRaw(ChatColor.DARK_PURPLE + "[50] Total SWC: Wiki Contributor Feather.\n" +
                                                  ChatColor.DARK_PURPLE + "[300] Total SWC: Super Contributor Feather.\n" +
                                                  ChatColor.DARK_PURPLE + "[1500] Total SWC: Ultimate Contributor Feather.\n" +
                                                  ChatColor.DARK_PURPLE + "- Total SWC rewards can be claimed with " + ChatColor.LIGHT_PURPLE + "swc claim milestones.", discord, isPublic, sender);
                            break;
                        case "add":
                            if (senderP.isOp()) {
                                if (args.length < 3) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, Integer.toString(args.length), "3");
                                if (args.length > 3) return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "3");
                                String user = args[1];
                                try {
                                    int amount = Integer.parseInt(args[2]);
                                    main.addSWC(sender, isPublic, user, amount, true);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(args[2] + " is not a valid amount, idiot.", discord, isPublic, sender);
                                }
                            } else IngoBot.sendMessageTo("Only Operators may execute this operation, idiot.", discord, isPublic, sender);
                            break;
                        case "remove":
                            if (senderP.isOp()) {
                                if (args.length < 3) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, Integer.toString(args.length), "3");
                                if (args.length > 3) return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "3");
                                String user = args[1];
                                try {
                                    int amount = Integer.parseInt(args[2]);
                                    main.addSWC(sender, isPublic, user, -amount, true);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(args[2] + " is not a valid amount, idiot.", discord, isPublic, sender);
                                }
                            } else IngoBot.sendMessageTo("Only Operators may execute this operation, idiot.", discord, isPublic, sender);
                            break;
                        case "softadd":
                            if (senderP.isOp()) {
                                if (args.length < 3) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, Integer.toString(args.length), "3");
                                if (args.length > 3) return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "3");
                                String user = args[1];
                                try {
                                    int amount = Integer.parseInt(args[2]);
                                    main.addSWC(sender, isPublic, user, amount, false);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(args[2] + " is not a valid amount, idiot.", discord, isPublic, sender);
                                }
                            } else IngoBot.sendMessageTo("Only Operators may execute this operation, idiot.", discord, isPublic, sender);
                            break;
                        case "softremove":
                            if (senderP.isOp()) {
                                if (args.length < 3) return new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, Integer.toString(args.length), "3");
                                if (args.length > 3) return new CommandResult(ResultType.TOOMANYARGUMENTSEXCEPTION, Integer.toString(args.length), "3");
                                String user = args[1];
                                try {
                                    int amount = Integer.parseInt(args[2]);
                                    main.addSWC(sender, isPublic, user, -amount, false);
                                } catch (Exception e) {
                                    IngoBot.sendMessageTo(args[2] + " is not a valid amount, idiot.", discord, isPublic, sender);
                                }
                            } else IngoBot.sendMessageTo("Only Operators may execute this operation, idiot.", discord, isPublic, sender);
                            break;
                        default: 
                            if (!senderP.isOp()) IngoBot.sendMessageTo("Unknown argument idiot: " + args[0] + ". Valid arguments: [claim, get, rewards]", discord, isPublic, sender);
                            else IngoBot.sendMessageTo("Unknown argument, idiot: " + args[0] + ". Valid arguments: [claim, get, rewards, add, remove, softadd, softremove]", discord, isPublic, sender);
                    }
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
