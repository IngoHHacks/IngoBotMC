package tv.ingoh.util;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.dv8tion.jda.api.EmbedBuilder;
import tv.ingoh.minecraft.plugins.ingobotcore.Config;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;

public class PlayerListHandler {

    public static void updatePlayerList(Config config, DiscordInterface discord) {
        List<String> players = getPlayerList();
        EmbedBuilder eBuilder = new EmbedBuilder();
        eBuilder.setTitle(players.size() + " player" + Pluralize.s(players.size()) + " online");
        players.forEach(player -> eBuilder.appendDescription(player));
        discord.editMessage(config.getcPlayerlist(), config.getmPlayerlist(), eBuilder.build());
    }


    public static List<String> getPlayerList() {
        LinkedList<String> pList = new LinkedList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            pList.add(p.getName());
        }
        return pList;
    }
}
