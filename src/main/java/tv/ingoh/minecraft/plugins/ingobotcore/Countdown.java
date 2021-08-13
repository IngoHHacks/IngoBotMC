package tv.ingoh.minecraft.plugins.ingobotcore;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;

public class Countdown {

    Player target;
    long endTime;
    int prevSecond;
    DiscordInterface discord;

    public Countdown(DiscordInterface discord, Player target, double time) {
        this.discord = discord;
        this.target = target;
        endTime = System.currentTimeMillis() + (long)(time * 1000);
        prevSecond = (int) Math.floor(time);
    }

    public void printSecond() {
        if (target != null) {
            if (prevSecond <= 10 ||
                (prevSecond <= 20 && (prevSecond % 5 == 0)) ||
                (prevSecond <= 60 && (prevSecond % 10 == 0)) ||
                (prevSecond <= 300 && (prevSecond % 30 == 0))
            )
            IngoBot.sendMessageTo(ChatColor.GREEN + "[C] " + Integer.toString(prevSecond), discord, false, target.getName());
        } else {
            if (prevSecond <= 10 ||
                (prevSecond <= 20 && (prevSecond % 5 == 0)) ||
                (prevSecond <= 60 && (prevSecond % 10 == 0)) ||
                (prevSecond <= 300 && (prevSecond % 30 == 0))
            )
            IngoBot.sendMessage(ChatColor.GREEN + "[C] " + Integer.toString(prevSecond), discord);
        }
        prevSecond--;
    }

    public boolean isFinished() {
        return (System.currentTimeMillis() > endTime);
    }
}
