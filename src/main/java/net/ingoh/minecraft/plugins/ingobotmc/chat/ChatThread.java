package net.ingoh.minecraft.plugins.ingobotmc.chat;

import java.util.LinkedList;

import net.ingoh.minecraft.plugins.ingobotmc.command.ChatMessage;
import org.bukkit.Bukkit;

import net.ingoh.minecraft.plugins.ingobotmc.Main;
import net.ingoh.minecraft.plugins.ingobotmc.web.WebThread;

public class ChatThread {

    private AsyncChatThread a;

    public void run(Main pl, LinkedList<ChatMessage> queue, WebThread wThread) {
        a = new AsyncChatThread(pl, pl.discord, queue, wThread);
        Bukkit.getScheduler().runTaskAsynchronously(pl, a);
    }

    public boolean queueUsed() {
        return a.queueUsed;
    }

    public void end() {
        a.end = true;
    }
}
