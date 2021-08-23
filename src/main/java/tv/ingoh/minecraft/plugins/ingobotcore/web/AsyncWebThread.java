package tv.ingoh.minecraft.plugins.ingobotcore.web;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

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
        IMAGE,
        USERINFO;
    }

    final static String CHAT = "http://ingoh.tv/api.php?prompt&path=gpt2&text=%0";

    LinkedList<Query> queue = new LinkedList<>();
    DiscordInterface discord;
    Main main;
    Config config;

    public AsyncWebThread(Main main, DiscordInterface discord) {
        this.discord = discord;
        this.main = main;
        config = main.config;
    }

    @Override
    public void run() {
        ChatHistory ch = new ChatHistory();
        while (!end) {
            if (queue.size() > 0) {
                Query q = queue.getFirst();
                String user = q.user;
                boolean isPublic = q.isPublic;
                URL u;
                switch (q.type) {
                    case CHAT:
                        try {
                            String text = q.args[0];
                            boolean finish = Boolean.parseBoolean(q.args[1]);
                            u = new URL(CHAT.replace("%0", URLEncoder.encode(ch.getHistory(isPublic, user) + text.replaceAll("[^\\x00-\\x7F]", ""), "UTF-8")));
                            ch.append(text, isPublic, user);
                            String res = executeConverse(u, finish, ch);
                            if (res.charAt(0) != '[') {
                                if (finish) {
                                    ch.append(res.substring(2), isPublic, user);
                                    res = ("..." + res).replace("...>>", "...");
                                } else {
                                    ch.append("\n" + res, isPublic, user);
                                }
                            } else {
                                ch.removeLast();
                            }
                            if (isPublic) {
                                IngoBot.sendMessageFromAsync(main, res);
                            } else {
                                IngoBot.sendMessageFromAsync(main, res, user);
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

    private String executeConverse(URL url, boolean finish, ChatHistory ch) {
        try {
            int tries = 0;
            boolean retry = true;
            while (retry && tries < 3) {
                try {
                    InputStream in = url.openStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    String s = r.readLine().replace("<br>", "");
                    if (s.contains("Timed out")) {
                        if (tries == 2) {
                            discord.sendDebug("NO OUTPUT");
                            r.close();
                            return "[ERROR] Timed out: No output";
                        }
                    } else {
                        String row2 = r.readLine();
                        if (row2 != null) {
                            String out = row2.replace("<br>", "");
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
                                if (finish && !s.equals(">>") && !s.equals(">> ") && !s.equals(">>?") && !s.equals(">>.")) {
                                    if (!Filter.isBanned(s)) {
                                        out = s;
                                        discord.sendDebug(("..." + out).replace("...>>", "..."));
                                        r.close();
                                        return out;
                                    } else {
                                        discord.sendDebug("**Filtered: " + s + "**");
                                    }
                                } else if (!finish && !out.equals(".") && !out.equals("?") && !out.equals("") && !out.equals(" ")) {
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
                                    discord.sendDebug(("..." + out).replace("...>>", "..."));
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
                    r.close();
                } catch (Exception e) {
                    discord.sendDebug("ERROR");
                    discord.sendDebug("CODE: " + e.getMessage());
                    if (tries == 2) {
                        return "[ERROR] Response is invalid";
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
    public void add(Query q) {
        if (queue.size() < 20) {
            queue.add(q);
        }
    }
}
