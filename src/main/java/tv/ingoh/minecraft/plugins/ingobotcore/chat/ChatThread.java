package tv.ingoh.minecraft.plugins.ingobotcore.chat;

import java.util.LinkedList;

import org.bukkit.Bukkit;

import tv.ingoh.minecraft.plugins.ingobotcore.Main;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ChatMessage;
import tv.ingoh.minecraft.plugins.ingobotcore.web.WebThread;

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
