package tv.ingoh.minecraft.plugins.ingobotcore.web;

import org.bukkit.Bukkit;

import tv.ingoh.minecraft.plugins.ingobotcore.Main;

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
}
