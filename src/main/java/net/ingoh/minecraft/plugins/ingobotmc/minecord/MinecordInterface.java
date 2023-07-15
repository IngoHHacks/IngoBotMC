package net.ingoh.minecraft.plugins.ingobotmc.minecord;

import io.github.starsdown64.minecord.api.ExternalMessageEvent;
import org.bukkit.Bukkit;

public class MinecordInterface {

    public static void sendMessage(String message) {
        if (Bukkit.getServer().getPluginManager().getPlugin("Minecord") != null) {
            ExternalMessageEvent messageEvent = new ExternalMessageEvent(message);
            Bukkit.getPluginManager().callEvent(messageEvent);
        }
    }
}
