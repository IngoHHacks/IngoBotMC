package net.ingoh.minecraft.plugins.ingobotmc.minecord;

import org.bukkit.Bukkit;

public class MinecordInterface {

    public static void sendMessage(String message) {
        if (Bukkit.getServer().getPluginManager().getPlugin("Minecord") != null) {
            ExternalMessage messageEvent = new ExternalMessage(message);
            Bukkit.getPluginManager().callEvent(messageEvent.getEvent());
        }
    }
}
