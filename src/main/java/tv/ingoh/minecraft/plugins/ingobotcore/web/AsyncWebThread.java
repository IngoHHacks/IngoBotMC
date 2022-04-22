package tv.ingoh.minecraft.plugins.ingobotcore.web;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import tv.ingoh.minecraft.plugins.ingobotcore.Config;
import tv.ingoh.minecraft.plugins.ingobotcore.IngoBot;
import tv.ingoh.minecraft.plugins.ingobotcore.Main;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;
import tv.ingoh.util.Filter;


public class AsyncWebThread implements Runnable {

    boolean end = false;

    public enum Type {
        CHAT,
        COLOR,
        IMAGE,
        USERINFO;
    }

    final static String CHAT = "https://ingoh.net/capi/%1/%0";

    LinkedList<Query> queue = new LinkedList<>();
    DiscordInterface discord;
    Main main;
    Config config;
    long nextTime;

    public AsyncWebThread(Main main, DiscordInterface discord) {
        nextTime = (System.currentTimeMillis() + (7200000 + (long)(36000000 * Math.random()) / 2) / 2);
        discord.sendDebug("Next random message at " + DateFormat.getDateTimeInstance().format(nextTime));
        this.discord = discord;
        this.main = main;
        config = main.config;
    }

    @Override
    public void run() {
        ChatHistory ch = new ChatHistory();
        while (!end) {
            if (System.currentTimeMillis() > nextTime) {
                nextTime = (System.currentTimeMillis() + (7200000 + (long)(36000000 * Math.random())) / 2);
                discord.sendDebug("Next random message at " + DateFormat.getDateTimeInstance().format(nextTime));
                String start = randomStartMessage();
                if (Math.random() > 0.5) start = start.toLowerCase();
                queue.add(new Query(Type.CHAT, null, new String[]{start, "true", "random"}, true));
            }
            if (queue.size() > 0) {
                Query q = queue.getFirst();
                String user = q.user;
                boolean isPublic = q.isPublic;
                String[] args = q.args;
                URL u;
                switch (q.type) {
                    case CHAT:
                        try {
                            String text = args[0];
                            boolean finish = Boolean.parseBoolean(args[1]);
                            String model = args[2];
                            if (model.equals("gpt2")) u = new URL(CHAT.replace("%0", URLEncoder.encode(ch.getHistory(isPublic, user) + text.replaceAll("[^\\x00-\\x7F]", ""), "UTF-8")).replace("%1", model));
                            else if (model.equals("gptneo") || model.equals("gpt2furry")) u = new URL(CHAT.replace("%0", URLEncoder.encode(text.replaceAll("[^\\x00-\\x7F]", ""), "UTF-8")).replace("%1", model));
                            else u = new URL(CHAT.replace("%0", URLEncoder.encode(text.replaceAll("[^\\x00-\\x7F]", ""), "UTF-8")).replace("%1", "gpt2"));
                            String res;
                            if (model.equals("gpt2")) {
                                res = executeConverse(u, finish, ch, model, text);
                                if (res.charAt(0) != '[') {
                                    if (finish) {
                                        ch.append(res.replaceFirst(Pattern.quote(">>"), text), isPublic, user);
                                    } else {
                                        ch.append(text, isPublic, user);
                                        ch.append(res, isPublic, user);
                                    }

                                }
                            }
                            else res = executeConverse(u, finish, model, text);
                            if (model.equals("random")) {
                                if (res.charAt(0) != '[') {
                                    res = res.replaceFirst(Pattern.quote(">>"), text);
                                    IngoBot.sendMessagesFromAsync(main, res);
                                }
                            } else {
                                if (isPublic) {
                                    IngoBot.sendMessageFromAsync(main, res);
                                } else {
                                    IngoBot.sendMessageFromAsync(main, res, user);
                                }
                            }
                        } catch (Exception e) {
                            discord.sendDebug("Unhandled Exception: " + e.toString());
                            discord.printStackTrace(e.getStackTrace());
                        }
                        break;
                    case USERINFO:
                        String s;
                        if (q.args.length > 0) {
                            s = doUserinfoLookup(q.args[0]);
                        } else {
                            s = doUserinfoLookup(q.user);
                        }
                        String[] strs = s.split(",");
                        if (strs.length >= 7) {
                            String out = ChatColor.GOLD + "Username: " + ChatColor.YELLOW + strs[2] + "\n"
                                       + ChatColor.GOLD + "Discord: " + ChatColor.YELLOW + strs[3] +  "\n"
                                       + ChatColor.GOLD + "Request submitted at: " + ChatColor.YELLOW + strs[0];
                            if (q.args.length > 0 && q.args[0].equals(q.user) && !isPublic && !strs[6].equals("")) {
                                out += "\n" + ChatColor.GOLD + "PWC: " + ChatColor.YELLOW + strs[6];
                            }
                            IngoBot.sendMessageToRaw(out, discord, isPublic, q.user);
                        } else {
                            IngoBot.sendMessageToRaw(ChatColor.GOLD + "User not on whitelist spreadsheet.", discord, isPublic, q.user);
                        }
                        break;
                    case IMAGE:
                        String url = q.args[0];
                        try {
                            int[][] pixels = getImage(url);
                            String out = "[";
                            if (pixels != null) {
                                for (int i = 0; i < pixels[0].length; i++) {
                                    for (int j = 0; j < pixels.length; j++) {
                                        String hex = String.format("#%06X", (0xFFFFFF & pixels[j][i]));
                                        if (!(i == 0 && j == 0)) out += ",";
                                        out += "{\"text\":\"■\",\"color\":\"" + hex + "\"}";
                                    }
                                    if (i != pixels.length - 1) out += ",{\"text\":\"\\n\"}";
                                }
                                out += "]";
                                BaseComponent[] bc = ComponentSerializer.parse(out);
                                IngoBot.sendMessageToRaw(bc, discord, isPublic, q.user);
                            } else {
                                IngoBot.sendMessageToRaw(ChatColor.RED + "Failed to get image from <" + url + ">", discord, isPublic, q.user);
                            }
                        } catch (Exception e) {
                            IngoBot.sendMessageToRaw(ChatColor.RED + "Failed to draw image from <" + url  + ">", discord, isPublic, q.user);
                        }
                        break;
                    case COLOR:
                        colorUser(args[0], args[1], args[2], args[3]);
                    default:
                        break;

                }
                queue.removeFirst();
            }
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                end = true;
            }
        }
    }

    final static String[] STARTERS = {"I", "I", "I", "I'm", "I'm", "I'm", "I am", "I am", "I'll", "I will", "I am going to", "I'm going to", "I think", "I wish", "I saw", "I love", "I like", "I hate", "I know", "I didn't know", "I wonder", "What if", "Imagine if", "Did you know that", "How", "My", "My favorite", "Did I", "Can I", "Can you", "Should I", "May I", "You"};
    final static String[] BEFORE_PERSON = {"I wonder if", "What if", "Imagine if", "Did you know that", "How did", "I didn't know that"};
    final static String[] AFTER_PERSON = {"", "", "is", "is", "is", "will", "is going to", "loves", "likes", "knows"};

    private String randomStartMessage() {
        if (Math.random() > 0.5) {
            return STARTERS[(int)(Math.random() * STARTERS.length)];
        } else {
            if (Math.random() > 0.5) {
                return BEFORE_PERSON[(int)(Math.random() * BEFORE_PERSON.length)] + " " + randomPerson();
            } else {
                return randomPerson() + " " + AFTER_PERSON[(int)(Math.random() * AFTER_PERSON.length)];
            }
        }
    }

    public final static String[] NAMES = {"Ingo", "Vic", "Stars", "Simple", "tangy", "misplet", "matthew", "Omega", "plex", "arc", "Spooked", "snake", "Veritas", "Timmy_ir", "captainNeda", "Pyro", "Racoonix", "s0und_", "nathan", "B_jamin", "NkdSquid", "frigateorpheon", "bl1ngbl0ng", "HAX0R", "Cha0s", "glitch", "Samuel", "FoxyProxy", "Tub", "DoubleL", "KitLemonfoot", "Zyn"};

    private String randomPerson() {
        if (Math.random() > 0.5) {
            return NAMES[(int)(Math.random() * NAMES.length)];
        } else {
            Object[] p = Bukkit.getWhitelistedPlayers().toArray();
            return ((OfflinePlayer)(p[(int)(Math.random() * p.length)])).getName();
        }
    }

    private static int[][] getImage(String url) {
        try {
            URL urlU = new URL(url);
            BufferedImage img = ImageIO.read(urlU);
            BufferedImage imgR = new BufferedImage(16, 16, img.getType());
            Graphics2D g2d = imgR.createGraphics();
            g2d.drawImage(img, 0, 0, 16, 16, 0, 0, img.getWidth(), img.getHeight(), null);
            g2d.dispose();
            int[][] pixels = new int[16][16];
            for(int i = 0; i < 16; i++) {
                for(int j = 0; j < 16; j++) {
                    pixels[i][j] = imgR.getRGB(i,j);
                }
            }
            return pixels;
        } catch (Exception e) {
            return null;
        }
    }

    private String doRead(String cell) {
        try {
            URL url = new URL(config.getSpreadsheet() + "/exec?cell=" + cell + "&action=read");
            InputStream in = url.openStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String s;
            String text = "";
            while ((s = r.readLine()) != null) {
                text += s + "\n";
            }
            String result = StringUtils.substringBetween(text, "\\x22userHtml\\x22:\\x22", "\\x22,\\x22");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String colorUser(String user, String r, String g, String b) {
        try {
            URL url = new URL(config.getSpreadsheet() + "/exec?action=colorl&find=" + user + "&r=" + r + "&g=" + g + "&b=" + b);
            InputStream in = url.openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            String s;
            String text = "";
            while ((s = rd.readLine()) != null) {
                text += s + "\n";
            }
            String result = StringUtils.substringBetween(text, "\\x22userHtml\\x22:\\x22", "\\x22,\\x22");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String doUserinfoLookup(String username) {
        try {
            URL url = new URL(config.getSpreadsheet() +  "/exec?find=" + username + "&action=lookup");
            InputStream in = url.openStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String s;
            String text = "";
            while ((s = r.readLine()) != null) {
                text += s + "\n";
            }
            String result = StringUtils.substringBetween(text, "\\x22userHtml\\x22:\\x22", "\\x22,\\x22");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String executeConverse(URL url, boolean finish, ChatHistory ch, String model, String msg) {
        try {
            int tries = 0;
            boolean retry = true;
            while (retry && tries < 3) {
                try {
                    InputStream in = url.openStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    String s = r.readLine().replace("<br>", "");
                    if (model.equals("random")) {
                        if (s.contains("Timed out")) {
                            if (tries == 2) {
                                discord.sendDebug("NO OUTPUT");
                                r.close();
                                return "[ERROR] Timed out: No output";
                            }
                        } else {
                            // TODO: Fix bad repeating code here
                            String row1 = s;
                            String row2 = r.readLine();
                            String row3 = r.readLine();
                            String row4 = r.readLine();
                            String row5 = r.readLine();
                            String row6 = r.readLine();
                            String out = row1.replace("<br>", "");
                            if (row3 != null) s += row2.replace("<br>","\n");
                            if (row4 != null) s += row3.replace("<br>","\n");
                            if (row5 != null) s += row4.replace("<br>","\n");
                            if (row6 != null) s += row5.replace("<br>","\n");
                            if (tries == 2) {
                                if (!Filter.isBanned(s)) {
                                    if (row1.equals(">>") && !row1.equals(">> ")) {
                                        out = s;
                                    }
                                    discord.sendDebug(msg + out);
                                    r.close();
                                    return out;
                                } else {
                                    discord.sendDebug("**Filtered: " + s + "**");
                                    r.close();
                                    return "[ERROR] Response contains prohibited word";
                                }
                            } else {
                                if (!row1.equals(">>") && !row1.equals(">> ") && !row1.equals(">>?") && !row1.equals(">>!") && !row1.equals(">>.") && !row1.equals(">>,")) {
                                    if (!Filter.isBanned(s)) {
                                        out = s;
                                        discord.sendDebug(msg + out);
                                        r.close();
                                        return out;
                                    } else {
                                        discord.sendDebug("**Filtered: " + s + "**");
                                    }
                                }
                            }
                        }
                    } else {
                        if (s.contains("Timed out")) {
                            if (tries == 2) {
                                discord.sendDebug("NO OUTPUT");
                                r.close();
                                return "[ERROR] Timed out: No output";
                            }
                        } else {
                            // TODO: Fix bad repeating code here
                            String row2 = r.readLine();
                            String row3 = r.readLine();
                            String row4 = r.readLine();
                            String row5 = r.readLine();
                            String row6 = r.readLine();
                            if (row2 != null) {
                                String out = row2.replace("<br>", "");
                                if (model.equals("gptneo")) {
                                    s += row2.replace("<br>","\n");
                                    if (row3 != null) s += row3.replace("<br>","\n");
                                    if (row4 != null) s += row4.replace("<br>","\n");
                                    if (row5 != null) s += row5.replace("<br>","\n");
                                    if (row6 != null) s += "\n...";
                                }
                                if (tries == 2) {
                                    if (!Filter.isBanned(s) && !Filter.isBanned(out)) {
                                        if (finish && !s.equals(">>") && !s.equals(">> ")) {
                                            out = s;
                                        }
                                        discord.sendDebug(("..." + out).replace("...>>", "..."));
                                        r.close();
                                        return out;
                                    } else {
                                        discord.sendDebug("**Filtered: " + s + "**");
                                        r.close();
                                        return "[ERROR] Response contains prohibited word";
                                    }
                                } else {
                                    if (finish && !s.equals(">>") && !s.equals(">> ") && !s.equals(">>?") && !s.equals(">>!") && !s.equals(">>.") && !s.equals(">>,")) {
                                        if (!Filter.isBanned(s)) {
                                            out = s;
                                            discord.sendDebug(("..." + out).replace("...>>", "..."));
                                            r.close();
                                            return out;
                                        } else {
                                            discord.sendDebug("**Filtered: " + s + "**");
                                        }
                                    } else if (!finish && !out.equals(".") && !out.equals("?") && !out.equals("!") && !out.equals("") && !out.equals(" ") && !out.equals(",")) {
                                        if (!Filter.isBanned(out)) {
                                            discord.sendDebug(out);
                                            r.close();
                                            return out;
                                        } else {
                                            discord.sendDebug("**Filtered: " + s + "**");
                                        }
                                    }
                                }
                            } else {
                                if (tries == 2) {
                                    String out = s;
                                    if (!Filter.isBanned(out)) {
                                        if (model.equals("random")) discord.sendDebug((msg +  out));
                                        else discord.sendDebug(("..." + out).replace("...>>", "..."));
                                        r.close();
                                        return out;
                                    } else {
                                        discord.sendDebug("**Filtered: " + s + "**");
                                        r.close();
                                        return "[ERROR] Response contains prohibited word";
                                    }
                                }
                            }
                        }
                    }
                    r.close();
                } catch (Exception e) {
                    discord.sendDebug("Attempt " + (tries + 1) + " failed");
                    if (tries == 2) {
                        discord.sendDebug("ERROR");
                        discord.sendDebug("CODE: " + e.getMessage());
                        if (e.getMessage().contains("HTTP response code: ")) {
                            String code = e.getMessage().split("HTTP response code: ",2)[1].split(" ", 2)[0];
                            return "[ERROR] Server error (Response code: " + code + ")";
                        }
                        else return "[ERROR] Response is invalid";
                    }
                }
                tries++;
            }
        } catch (Exception e) {
            discord.sendDebug("ERROR");
            discord.sendDebug("CODE: " + e.getMessage());
            if (e.getMessage().contains("HTTP response code: ")) {
                String code = e.getMessage().split("HTTP response code: ",2)[1].split(" ", 2)[0];
                return "[ERROR] Server error (Response code: " + code + ")";
            }
            else return "[ERROR] Response is invalid";
        }
        return "[NO RESPONSE]";     // Unused
    }

    private String executeConverse(URL url, boolean finish, String model, String msg) {
        ChatHistory dummy = new ChatHistory();
        return executeConverse(url, finish, dummy, model, msg);
    }

    public void add(Query q) {
        if (queue.size() < 20) {
            queue.add(q);
        }
    }
}
