package net.ingoh.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import net.dv8tion.jda.api.EmbedBuilder;
import net.ingoh.minecraft.plugins.ingobotmc.Config;
import net.ingoh.minecraft.plugins.ingobotmc.discord.DiscordInterface;

public class PlayerListHandler {

    public static void updatePlayerList(Config config, DiscordInterface discord, List<String> exclude) {
        List<String> players = getPlayerList();
        EmbedBuilder eBuilder = new EmbedBuilder();
        eBuilder.setTitle(players.size() - exclude.size() + " player" + Pluralize.s(players.size() - exclude.size()) + " online");
        players.forEach(player -> {
            if (!exclude.contains(player)) {
                Team team = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player);
                String role = team == null ? "🙂 " : getColorIcon(team.getColor());
                eBuilder.appendDescription(role + player + "\n");
            }
        });
        eBuilder.appendDescription("Last update: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        try {
            discord.editMessage(config.getcPlayerlist(), config.getmPlayerlist(), eBuilder.build());
        } catch (Exception ignored) {}
    }


    private static String getColorIcon(ChatColor color) {
        switch (color) {
            case AQUA:
                return "📽️ ";
            case BLACK:
                return "🥸 ";
            case BLUE:
                return "🕵️ ";
            case DARK_AQUA:
                return "❓ ";
            case DARK_BLUE:
                return "❓ ";
            case DARK_GRAY:
                return "❓ ";
            case DARK_GREEN:
                return "❓ ";
            case DARK_PURPLE:
                return "🛡️ ";
            case DARK_RED:
                return "❓ ";
            case GOLD:
                return "🟧 ";
            case GRAY:
                return "❓ ";
            case GREEN:
                return "⚙️ ";
            case LIGHT_PURPLE:
                return "🤖 ";
            case RED:
                return "👮 ";
            case WHITE:
                return "◻️ ";
            case YELLOW:
                return "🟨 ";
            default:
                return "❓ ";
        }
    }


    public static List<String> getPlayerList() {
        LinkedList<String> pList = new LinkedList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            pList.add(p.getName());
        }
        return pList;
    }
}
