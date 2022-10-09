package net.ingoh.minecraft.plugins.ingobotmc.chat;

import java.util.Arrays;
import java.util.LinkedList;

import net.ingoh.minecraft.plugins.ingobotmc.command.ChatMessage;
import net.ingoh.minecraft.plugins.ingobotmc.discord.DiscordInterface;
import net.ingoh.minecraft.plugins.ingobotmc.Main;
import net.ingoh.minecraft.plugins.ingobotmc.command.CoreCommands;
import net.ingoh.minecraft.plugins.ingobotmc.web.WebThread;

public class AsyncChatThread implements Runnable {

    boolean end = false;

    DiscordInterface discord;
    LinkedList<ChatMessage> queue;
    WebThread wThread;
    Main main;
    boolean queueUsed;

    public AsyncChatThread(Main main, DiscordInterface discord, LinkedList<ChatMessage> queue, WebThread wThread){
        this.discord = discord;
        this.queue = queue;
        this.wThread = wThread;
        queueUsed = false;
        this.main = main;
    }

    @Override
    public void run() {
        while (!end) {
            queueUsed = true;
            if (queue.size() > 0) {
                ChatMessage msg = queue.getFirst();
                if (msg.message.charAt(0) == '!' && msg.message.length() > 1) {
                    String args[] = msg.message.substring(1, msg.message.length()).split(" ");
                    CoreCommands.scheduleCommand(main, args[0], Arrays.copyOfRange(args, 1, args.length), msg.sender, wThread, true, discord);
                }
                queue.removeFirst();
            }
            queueUsed = false;
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                end = true;
            }
        }
    }
    
}
