package tv.ingoh.minecraft.plugins.ingobotcore.chat;

import java.util.Arrays;
import java.util.LinkedList;

import tv.ingoh.minecraft.plugins.ingobotcore.Main;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ChatMessage;
import tv.ingoh.minecraft.plugins.ingobotcore.command.CoreCommands;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;
import tv.ingoh.minecraft.plugins.ingobotcore.web.WebThread;

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
                    CoreCommands.scheduleCommand(main, args[0], Arrays.copyOfRange(args, 1, args.length), msg.sender.getName(), wThread, true, discord);
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
