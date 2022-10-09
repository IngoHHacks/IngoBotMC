package net.ingoh.minecraft.plugins.ingobotmc;

import net.ingoh.minecraft.plugins.ingobotmc.discord.DiscordInterface;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Countdown {

    Player target;
    long endTime;
    int prevSecond;
    boolean uppies;
    long startTime;
    double totalTime;
    DiscordInterface discord;
    boolean started;

    public Countdown(DiscordInterface discord, Player target, double time, boolean uppies) {
        this.discord = discord;
        this.target = target;
        this.startTime = System.currentTimeMillis();
        this.uppies = uppies;
        totalTime = time;
        endTime = System.currentTimeMillis() + (long)(time * 1000);
        if (uppies) prevSecond = 0;
        else prevSecond = (int) Math.floor(time);
        started = false;
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
        if (uppies) prevSecond++;
        else prevSecond--;
    }

    public boolean isFinished() {
        return (System.currentTimeMillis() > endTime);
    }

    public void printTotalTime() {
        if (target != null) {
            if (uppies || ((int)totalTime != totalTime ||
            !(prevSecond <= 10 ||
            (prevSecond <= 20 && (prevSecond % 5 == 0)) ||
            (prevSecond <= 60 && (prevSecond % 10 == 0)) ||
            (prevSecond <= 300 && (prevSecond % 30 == 0))
        ))) {
                IngoBot.sendMessageTo(ChatColor.GREEN + "[C] " + Double.toString(totalTime), discord, false, target.getName());
            }
        } else {
            if (uppies || ((int)totalTime != totalTime ||
            !(prevSecond <= 10 ||
            (prevSecond <= 20 && (prevSecond % 5 == 0)) ||
            (prevSecond <= 60 && (prevSecond % 10 == 0)) ||
            (prevSecond <= 300 && (prevSecond % 30 == 0))
        ))) {
                IngoBot.sendMessage(ChatColor.GREEN + "[C] " + Double.toString(totalTime), discord);
            }
        }
       
    }
}
