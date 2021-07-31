package tv.ingoh.minecraft.plugins.ingobotcore.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;

import tv.ingoh.minecraft.plugins.ingobotcore.IngoBot;
import tv.ingoh.minecraft.plugins.ingobotcore.Main;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;
import tv.ingoh.util.Filter;


public class AsyncWebThread implements Runnable {

    boolean end = false;

    public enum Type {
        CHAT;
    }

    final static String CHAT = "http://ingoh.tv/api.php?prompt&path=gpt2&text=%0";

    LinkedList<Query> queue = new LinkedList<>();
    DiscordInterface discord;
    Main main;

    public AsyncWebThread(Main main, DiscordInterface discord) {
        this.discord = discord;
        this.main = main;
    }

    @Override
    public void run() {
        ChatHistory ch = new ChatHistory();
        while (!end) {
            if (queue.size() > 0) {
                Query q = queue.getFirst();
                String text = q.args[0];
                boolean finish = Boolean.parseBoolean(q.args[1]);
                String user = q.user;
                boolean isPublic = q.isPublic;
                URL u;
                switch (q.type) {
                    case CHAT:
                        try {
                            u = new URL(CHAT.replace("%0", URLEncoder.encode(ch.getHistory(isPublic, user) + text.replaceAll("[^\\x00-\\x7F]", ""), "UTF-8")));
                            ch.append(text, isPublic, user);
                            String res = executeConverse(u, finish, ch);
                            if (finish) {
                                ch.append(res.substring(2), isPublic, user);
                                res = ("..." + res).replace("...>>", "...");
                            } else {
                                ch.append("\n" + res, isPublic, user);
                            }
                            if (isPublic) {
                                IngoBot.sendMessageFromAsync(main, res);
                            } else {
                                IngoBot.sendMessageFromAsync(main, res, user);
                            }
                        } catch (MalformedURLException | UnsupportedEncodingException e) {
                            discord.sendDebug("Unhandled Exception: " + e.toString());
                            discord.printStackTrace(e.getStackTrace());
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

    private String executeConverse(URL url, boolean finish, ChatHistory ch) {
        try {
            int tries = 0;
            boolean retry = true;
            while (retry && tries < 3) {
                InputStream in = url.openStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                String s = r.readLine();
                if (s.contains("Timed out")) {
                    if (tries == 2) {
                        discord.sendDebug("ERROR01: No output");
                        r.close();
                        return "[TIMED OUT]\nError: No output";
                    }
                } else {
                    String out = r.readLine().replace("<br>", "");
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
                            discord.sendDebug("ERROR01: No output");
                            r.close();
                            return "[TIMED OUT]\nError: No output";
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
                }
                tries++;
                r.close();
            }
        } catch (IOException e) {    
            discord.sendDebug("ERROR02: No response");
            discord.sendDebug("CODE: " + e.getMessage());
            return "[NO RESPONSE] + \nError: " + e.getMessage();
        }
        return "[NO RESPONSE]";     // Unused
    }
    public void add(Query q) {
        if (queue.size() < 20) {
            queue.add(q);
        }
    }
    
}
