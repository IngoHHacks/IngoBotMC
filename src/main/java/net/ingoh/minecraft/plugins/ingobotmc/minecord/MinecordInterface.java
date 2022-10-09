package net.ingoh.minecraft.plugins.ingobotmc.minecord;

import org.bukkit.Bukkit;

import io.github.starsdown64.minecord.api.ExternalMessageEvent;

public class MinecordInterface {

    public static void sendMessage(String message) {
        if (Bukkit.getServer().getPluginManager().getPlugin("Minecord") != null) {
            ExternalMessageEvent messageEvent = new ExternalMessageEvent(message);
            Bukkit.getPluginManager().callEvent(messageEvent);
        }

    }

}
