package tv.ingoh.minecraft.plugins.ingobotcore;

import java.util.ArrayList;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.starsdown64.minecord.api.ExternalMessageEvent;
import tv.ingoh.minecraft.plugins.ingobotcore.command.CommandResult;
import tv.ingoh.minecraft.plugins.ingobotcore.command.CoreCommands;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ScheduledCommand;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;

public class MainLoop implements Runnable {

    private volatile LinkedList<Message> outputQueue;
    private volatile LinkedList<ScheduledCommand> commandQueue;
    private volatile LinkedList<String> syncCommands;
    private volatile LinkedList<ExternalMessageEvent> minecordBC;
    private volatile ArrayList<Countdown> countdowns;
    private volatile LinkedList<OfflinePlayer> whitelistQueue;


    boolean queueUsed;
    DiscordInterface discord;

    public MainLoop(LinkedList<Message> outputQueue, LinkedList<ScheduledCommand> commandQueue, LinkedList<String> syncCommands, LinkedList<ExternalMessageEvent> minecordBC, ArrayList<Countdown> countdowns, LinkedList<OfflinePlayer> whitelistQueue, DiscordInterface discord) {
        this.outputQueue = outputQueue;
        this.commandQueue = commandQueue;
        this.syncCommands = syncCommands;
        this.minecordBC = minecordBC;
        this.countdowns = countdowns;
        this.whitelistQueue = whitelistQueue;
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
                if (System.currentTimeMillis() > outputQueue.getFirst().delay) {
                    if (outputQueue.getFirst().receiver != null) {
                        Player p = Bukkit.getPlayer(outputQueue.getFirst().receiver);
                        if (p != null) p.sendMessage(outputQueue.getFirst().message);
                        else Bukkit.getLogger().info(outputQueue.getFirst().message);
                        discord.sendChat("IngoBot -> " + outputQueue.getFirst().receiver + ": " + outputQueue.getFirst().message, false);
                    } else {
                        Bukkit.broadcastMessage(outputQueue.getFirst().message);
                        discord.sendChat(outputQueue.getFirst().message);
                    }
                    outputQueue.removeFirst();
                }
            }

            if (commandQueue.size() > 0) {
                CommandResult result;
                result = CoreCommands.executeCommand(commandQueue.getFirst());
                if (!result.isSuccessful()) {
                    IngoBot.sendMessageRaw(result.toString(), discord);
                    if (result.isUnhandledException()) {
                        result.printStackTrace(discord);
                    }
                }
                commandQueue.removeFirst();
            }

            if (syncCommands.size() > 0) {
                String contentRaw = syncCommands.getFirst();
                if (contentRaw.charAt(0) == '/') {
                    boolean result = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), contentRaw.substring(1));
                    if (result /*exists*/) {
                        discord.sendInfoChat("Command executed.");
                    } else {
                        discord.sendInfoChat("Command not executed.");
                    }
                }
                else Bukkit.broadcastMessage("[IngoBot] " + contentRaw);
                syncCommands.removeFirst();
            }

            if (minecordBC.size() > 0) {
                ExternalMessageEvent msg = minecordBC.getFirst();
                Bukkit.getPluginManager().callEvent(msg);
                minecordBC.removeFirst();
            }

            if (countdowns.size() > 0) {
                LinkedList<Countdown> scheduleDeletion = new LinkedList<>();
                for (int i = 0; i < countdowns.size(); i++) {
                    long time = countdowns.get(i).endTime - System.currentTimeMillis();
                    if (time/1000.0 < countdowns.get(i).prevSecond) {
                        countdowns.get(i).printSecond();
                        if (countdowns.get(i).isFinished()) scheduleDeletion.add(countdowns.get(i));
                    }
                }
                for (Countdown countdown : scheduleDeletion) {
                    countdowns.remove(countdown);
                }
            }

            if (whitelistQueue.size() > 0) {
                if (!whitelistQueue.getFirst().isWhitelisted()) {
                    whitelistQueue.getFirst().setWhitelisted(true);
                    discord.sendTo(discord.getChannels().spreadsheetChannel, "Whitelisted user " + whitelistQueue.getFirst().getName() + ", UUID: " + whitelistQueue.getFirst().getUniqueId());
                } else {
                    discord.sendTo(discord.getChannels().spreadsheetChannel, "User " + whitelistQueue.getFirst().getName() + ", UUID: " + whitelistQueue.getFirst().getUniqueId() + " is already whitelisted");
                }
                whitelistQueue.removeFirst();
            }

        } catch (Exception e) {
            // Failsafe to prevent infinite loops
            discord.sendDebug("ERROR IN MAIN LOOP: " + e.getMessage());
            discord.printStackTrace(e.getStackTrace());
            if (outputQueue.size() > 0) outputQueue.removeFirst();
            if (commandQueue.size() > 0) commandQueue.removeFirst();
            if (syncCommands.size() > 0) syncCommands.removeFirst();
            if (minecordBC.size() > 0) minecordBC.removeFirst();
            if (countdowns.size() > 0) countdowns.remove(0);
        }
        queueUsed = false;
    }
}
