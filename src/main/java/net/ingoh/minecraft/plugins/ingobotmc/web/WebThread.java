package net.ingoh.minecraft.plugins.ingobotmc.web;

import org.bukkit.Bukkit;

import net.ingoh.minecraft.plugins.ingobotmc.Main;

public class WebThread {

    private AsyncWebThread a;

    public void run(Main pl) {
        a = new AsyncWebThread(pl, pl.discord);
        Bukkit.getScheduler().runTaskAsynchronously(pl, a);
    }

    public void add(Query q) {
        a.add(q);
    }

    public void end() {
        a.end = true;
    }

    public void undoHist(String user, boolean isPublic) {
        a.add(new Query(AsyncWebThread.Type.REMOVEHISTORY, user, new String[]{"true", "1"}, isPublic));
    }

    public void clearHist(String user, boolean isPublic) {
        a.add(new Query(AsyncWebThread.Type.REMOVEHISTORY, user, new String[]{"true"}, isPublic));
    }

    public void undo(Query qr) {
        if (qr.args[2].equals("gpt2")) {
            if (qr.args[1].equals("true")) {
                a.add(new Query(AsyncWebThread.Type.REMOVEHISTORY, qr.user, new String[]{"false", "1"}, qr.isPublic));
            } else {
                a.add(new Query(AsyncWebThread.Type.REMOVEHISTORY, qr.user, new String[]{"false", "2"}, qr.isPublic));
            }
        }
    }

    public void printHist(String user, boolean isPublic) {
        a.add(new Query(AsyncWebThread.Type.PRINTHISTORY, user, new String[]{}, isPublic));
    }
}
