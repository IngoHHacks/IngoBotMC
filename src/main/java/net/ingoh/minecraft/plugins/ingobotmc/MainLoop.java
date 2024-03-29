package net.ingoh.minecraft.plugins.ingobotmc;

import java.util.ArrayList;
import java.util.LinkedList;

import io.github.starsdown64.minecord.api.ExternalMessageEvent;
import net.ingoh.minecraft.plugins.ingobotmc.command.CommandResult;
import net.ingoh.minecraft.plugins.ingobotmc.command.CoreCommands;
import net.ingoh.minecraft.plugins.ingobotmc.command.ScheduledCommand;
import net.ingoh.minecraft.plugins.ingobotmc.discord.DiscordInterface;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MainLoop implements Runnable {

    private LinkedList<Message> outputQueue;
    private LinkedList<ScheduledCommand> commandQueue;
    private LinkedList<String> syncCommands;
    private LinkedList<ExternalMessageEvent> minecordBC;
    private ArrayList<Countdown> countdowns;
    private LinkedList<OfflinePlayer> whitelistQueue;
    private LinkedList<Mob> sacrifices;
    private LinkedList<Integer> sacrificeAges;
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

    public MainLoop(Main mainI, LinkedList<Message> outputQueue, LinkedList<ScheduledCommand> commandQueue, LinkedList<String> syncCommands, ArrayList<Countdown> countdowns, LinkedList<OfflinePlayer> whitelistQueue, DiscordInterface discord, LinkedList<Mob> sacrifices, LinkedList<Integer> sacrificeAges) {
        this.mainI = mainI;
        this.outputQueue = outputQueue;
        this.commandQueue = commandQueue;
        this.syncCommands = syncCommands;
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
        synchronized (mainI.mainLock) {
            try {
                if (!outputQueue.isEmpty()) {
                    Message item = outputQueue.getFirst();
                    if (item.message.isBlank() || item.message.replace(">>", "").isBlank()) {
                        outputQueue.removeFirst();
                        return;
                    }
                    if (System.currentTimeMillis() > item.delay) {
                        if (outputQueue.getFirst() instanceof FormattedMessage) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendRawMessage(item.message);
                            }
                        } else {
                            if (outputQueue.getFirst().receiver != null) {
                                Player p = Bukkit.getPlayer(item.receiver);
                                if (p != null) p.sendMessage(item.message);
                                else Bukkit.getLogger().info(item.message);
                                discord.sendChat("IngoBot -> " + item.receiver + ": " + item.message, false);
                            } else {
                                Bukkit.broadcastMessage(item.message);
                                discord.sendChat(item.message);
                            }
                        }
                        outputQueue.removeFirst();
                    }
                }

                if (!commandQueue.isEmpty()) {
                    CommandResult result;
                    ScheduledCommand cmd = commandQueue.getFirst();
                    result = CoreCommands.executeCommand(cmd);
                    if (!result.isSuccessful()) {
                        IngoBot.sendErrorMessageTo(result.toString(), discord, cmd.isPublic(), cmd.getSender());
                        if (result.isUnhandledException()) {
                            result.printStackTrace(discord);
                        }
                    }
                    commandQueue.removeFirst();
                }

                if (!syncCommands.isEmpty()) {
                    String contentRaw = syncCommands.getFirst();
                    if (contentRaw.length() > 1 && contentRaw.charAt(0) == '.' && contentRaw.charAt(1) != '.') {
                        CommandSourceStack clw = mainI.ingobotNPC.createCommandSourceStack();
                        mainI.nmsServer.getCommands().dispatchServerCommand(clw, "/" + contentRaw.substring(1));
                    } else Bukkit.broadcastMessage("[IngoBot] " + contentRaw);
                    syncCommands.removeFirst();
                }

                if (Bukkit.getServer().getPluginManager().getPlugin("Minecord") != null) {
                    if (!minecordBC.isEmpty()) {
                        ExternalMessageEvent msg = minecordBC.getFirst();
                        Bukkit.getServer().getPluginManager().callEvent(msg);
                        minecordBC.removeFirst();
                    }
                }

                if (!countdowns.isEmpty()) {
                    LinkedList<Countdown> scheduleDeletion = new LinkedList<>();
                    for (int i = 0; i < countdowns.size(); i++) {
                        if (countdowns.get(i).uppies) {
                            long time = System.currentTimeMillis() - countdowns.get(i).startTime;
                            if (countdowns.get(i).isFinished()) {
                                countdowns.get(i).printTotalTime();
                                scheduleDeletion.add(countdowns.get(i));
                            } else if (time / 1000.0 >= countdowns.get(i).prevSecond) {
                                countdowns.get(i).printSecond();
                            }
                        } else {
                            if (!countdowns.get(i).started) {
                                countdowns.get(i).started = true;
                                countdowns.get(i).printTotalTime();
                            }
                            long time = countdowns.get(i).endTime - System.currentTimeMillis();
                            if (time / 1000.0 < countdowns.get(i).prevSecond) {
                                countdowns.get(i).printSecond();
                                if (countdowns.get(i).isFinished()) scheduleDeletion.add(countdowns.get(i));
                            }
                        }
                    }
                    for (Countdown countdown : scheduleDeletion) {
                        countdowns.remove(countdown);
                    }
                }

                if (!whitelistQueue.isEmpty()) {
                    if (!whitelistQueue.getFirst().isWhitelisted()) {
                        whitelistQueue.getFirst().setWhitelisted(true);
                        discord.sendTo(discord.getChannels().spreadsheetChannel, "Whitelisted user " + whitelistQueue.getFirst().getName() + ", UUID: " + whitelistQueue.getFirst().getUniqueId());
                    } else {
                        discord.sendTo(discord.getChannels().spreadsheetChannel, "User " + whitelistQueue.getFirst().getName() + ", UUID: " + whitelistQueue.getFirst().getUniqueId() + " is already whitelisted");
                    }
                    whitelistQueue.removeFirst();
                }

                if (!sacrifices.isEmpty()) {
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

                mainI.watchTick();

            } catch(Exception e){
                // Failsafe to prevent infinite loops
                discord.sendDebug("ERROR IN MAIN LOOP: " + e.getMessage());
                discord.printStackTrace(e.getStackTrace());
                if (!outputQueue.isEmpty()) outputQueue.removeFirst();
                if (!commandQueue.isEmpty()) commandQueue.removeFirst();
                if (!syncCommands.isEmpty()) syncCommands.removeFirst();
                if (Bukkit.getServer().getPluginManager().getPlugin("Minecord") != null) {
                    if (!minecordBC.isEmpty()) minecordBC.removeFirst();
                }
                if (!countdowns.isEmpty()) countdowns.remove(0);
                if (!sacrifices.isEmpty()) sacrifices.removeFirst();
            }
        }
    }
}
