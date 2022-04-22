package tv.ingoh.minecraft.plugins.ingobotcore;

import java.util.ArrayList;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

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
    private volatile LinkedList<Mob> sacrifices;
    private volatile LinkedList<Integer> sacrificeAges;
    private Main mainI;
    
    boolean queueUsed;
    DiscordInterface discord;

    public MainLoop(Main mainI, LinkedList<Message> outputQueue, LinkedList<ScheduledCommand> commandQueue, LinkedList<String> syncCommands, LinkedList<ExternalMessageEvent> minecordBC, ArrayList<Countdown> countdowns, LinkedList<OfflinePlayer> whitelistQueue, DiscordInterface discord, LinkedList<Mob> sacrifices, LinkedList<Integer> sacrificeAges) {
        this.mainI = mainI;
        this.outputQueue = outputQueue;
        this.commandQueue = commandQueue;
        this.syncCommands = syncCommands;
        this.minecordBC = minecordBC;
        this.countdowns = countdowns;
        this.whitelistQueue = whitelistQueue;
        queueUsed = false;
        this.discord = discord;
        this.sacrifices = sacrifices;
        this.sacrificeAges = sacrificeAges;
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
                ScheduledCommand cmd = commandQueue.getFirst();
                result = CoreCommands.executeCommand(cmd);
                if (!result.isSuccessful()) {
                    IngoBot.sendMessageToRaw(result.toString(), discord, cmd.isPublic(), cmd.getSender());
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

            if (sacrifices.size() > 0) {
                for (int i = sacrifices.size() - 1; i >= 0; i--) {
                    Mob entity = sacrifices.get(i);
                    Vector loc = entity.getLocation().toVector();
                    Vector target = new Vector(160.5, 56, 208.5);
                    Vector translation = target.clone().subtract(loc);
                    entity.setVelocity(translation.clone().normalize().multiply(0.1));
                    entity.getWorld().spawnParticle(Particle.SOUL, entity.getLocation(), 3, 0.25, 0.25, 0.25, 0.05);
                    sacrificeAges.set(i, sacrificeAges.get(i) + 1);
                    if (translation.lengthSquared() < 0.25 || sacrificeAges.get(i) > 100) {
                        sacrifices.remove(i);
                        sacrificeAges.remove(i);
                        entity.damage(1000000000, mainI.ingobotNPC.getBukkitEntity());
                    }
                }
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
            if (sacrifices.size() > 0) sacrifices.removeFirst();
        }
        queueUsed = false;
    }
}
