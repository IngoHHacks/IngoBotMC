package tv.ingoh.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import net.dv8tion.jda.api.EmbedBuilder;
import tv.ingoh.minecraft.plugins.ingobotcore.Config;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;

public class PlayerListHandler {

    public static void updatePlayerList(Config config, DiscordInterface discord, List<String> exclude) {
        List<String> players = getPlayerList();
        EmbedBuilder eBuilder = new EmbedBuilder();
        eBuilder.setTitle(players.size() - exclude.size() + " player" + Pluralize.s(players.size() - exclude.size()) + " online");
        players.forEach(player -> {
            if (!exclude.contains(player)) {
                Team team = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player);
                String role = team == null ? "<:white:955431358229012580> " : getColorIcon(team.getColor());
                eBuilder.appendDescription(role + player + "\n");
            }
        });
        eBuilder.appendDescription("Last update: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        discord.editMessage(config.getcPlayerlist(), config.getmPlayerlist(), eBuilder.build());
    }


    private static String getColorIcon(ChatColor color) {
        switch (color) {
            case AQUA:
                return "<:streamer:955432956313358376> ";
            case BLACK:
                return "<:spy:955431358048661554> ";
            case BLUE:
                return "<:investigator:955431358090592326> ";
            case DARK_AQUA:
                return "<:yellow:955431358312902656> ";
            case DARK_BLUE:
                return "<:yellow:955431358312902656> ";
            case DARK_GRAY:
                return "<:yellow:955431358312902656> ";
            case DARK_GREEN:
                return "<:yellow:955431358312902656> ";
            case DARK_PURPLE:
                return "<:moderator:955431358241574912> ";
            case DARK_RED:
                return "<:yellow:955431358312902656> ";
            case GOLD:
                return "<:orange:955431358090575902> ";
            case GRAY:
                return "<:yellow:955431358312902656> ";
            case GREEN:
                return "<:operator:955431358048636978> ";
            case LIGHT_PURPLE:
                return "<:bot:955431358195462164> ";
            case RED:
                return "<:officer:955431357675347969> ";
            case WHITE:
                return "<:white:955431358229012580> ";
            case YELLOW:
                return "<:yellow:955431358312902656> ";
            default:
                return "<:white:955431358229012580> ";
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
