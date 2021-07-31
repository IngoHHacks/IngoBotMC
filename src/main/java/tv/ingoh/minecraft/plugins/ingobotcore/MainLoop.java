package tv.ingoh.minecraft.plugins.ingobotcore;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tv.ingoh.minecraft.plugins.ingobotcore.command.CommandResult;
import tv.ingoh.minecraft.plugins.ingobotcore.command.CoreCommands;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ResultType;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ScheduledCommand;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;

public class MainLoop implements Runnable {

    private volatile LinkedList<Message> outputQueue;
    private volatile LinkedList<ScheduledCommand> commandQueue;

    boolean queueUsed;
    DiscordInterface discord;

    public MainLoop(LinkedList<Message> outputQueue, LinkedList<ScheduledCommand> commandQueue, DiscordInterface discord) {
        this.outputQueue = outputQueue;
        this.commandQueue = commandQueue;
        queueUsed = false;
        this.discord = discord;
    }

    /**
     * Runs every tick!
     */
    @Override
    public void run() {
        try {
            queueUsed = true;
            if (outputQueue.size() > 0) {
                if (outputQueue.getFirst().receiver != null) {
                    Player p = Bukkit.getPlayer(outputQueue.getFirst().receiver);
                    if (p != null) p.sendMessage(outputQueue.getFirst().message);
                    else Bukkit.getLogger().info(outputQueue.getFirst().message);
                    discord.sendChat("IngoBot -> " + outputQueue.getFirst().receiver + ": " + outputQueue.getFirst().message);
                } else {
                    Bukkit.broadcastMessage(outputQueue.getFirst().message);
                    discord.sendChat(outputQueue.getFirst().message);
                }
                outputQueue.removeFirst();
            }

            if (commandQueue.size() > 0) {
                CommandResult result;
                String[] args = commandQueue.getFirst().getArgs();
                if (args.length >= 1)  {
                    result = CoreCommands.executeCommand(commandQueue.getFirst());
                } else {
                    result = new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, "0", "1+");
                }
                if (!result.isSuccessful()) {
                    IngoBot.sendMessageRaw(result.toString(), discord);
                    if (result.isUnhandledException()) {
                        result.printStackTrace(discord);
                    }
                }
                commandQueue.removeFirst();
            }
            queueUsed = false;
        } catch (Exception e) {
            // Failsafe to prevent infinite loops
            discord.sendDebug("SEVERE ERROR IN MAIN LOOP: " + e.getMessage());
            discord.printStackTrace(e.getStackTrace());
            if (outputQueue.size() > 0) outputQueue.removeFirst();
            if (commandQueue.size() > 0) commandQueue.removeFirst();
        }
    }
}
