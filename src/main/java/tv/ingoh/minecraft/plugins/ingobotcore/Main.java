package tv.ingoh.minecraft.plugins.ingobotcore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.starsdown64.Minecord.api.ExternalMessageEvent;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import tv.ingoh.minecraft.plugins.ingobotcore.chat.ChatThread;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ChatMessage;
import tv.ingoh.minecraft.plugins.ingobotcore.command.CommandResult;
import tv.ingoh.minecraft.plugins.ingobotcore.command.CoreCommands;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ScheduledCommand;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;
import tv.ingoh.minecraft.plugins.ingobotcore.web.Query;
import tv.ingoh.minecraft.plugins.ingobotcore.web.WebThread;
import tv.ingoh.minecraft.plugins.ingobotcore.web.AsyncWebThread.Type;

public class Main extends JavaPlugin implements Listener {

    JavaPlugin plugin;
    public Config config;
    public DiscordInterface discord;
    LinkedList<ChatMessage> chatQueue;
    WebThread wThread;
    ChatThread cThread;
    LinkedList<Message> outputQueue;
    LinkedList<ScheduledCommand> commandQueue;
    MainLoop mainLoop;
    EntityPlayer ingobotNPC;
    LinkedList<String> syncCommands;
    LinkedList<ExternalMessageEvent> minecordBC;
    ArrayList<Countdown> countdowns;
    LinkedList<OfflinePlayer> whitelistQueue;

    @Override
    public void onEnable() {
        chatQueue = new LinkedList<>();
        plugin = this;
        config = new Config(this);
        config.load();
        discord = new DiscordInterface(config, this);
        try {
            discord.start();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        try {
            // Wait for a second to load JDA
            // TODO: Find workaround
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("i").setTabCompleter(new IngoBotTabCompleter());

        // Threads
        wThread = new WebThread();
        wThread.run(this);

        cThread = new ChatThread();
        cThread.run(this, chatQueue, wThread);

        outputQueue = new LinkedList<>();
        commandQueue = new LinkedList<>();
        syncCommands = new LinkedList<>();
        minecordBC = new LinkedList<>();
        countdowns = new ArrayList<>();
        whitelistQueue = new LinkedList<>();

        mainLoop = new MainLoop(outputQueue, commandQueue, syncCommands, minecordBC, countdowns, whitelistQueue, discord);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, mainLoop, 0, 1);

        // Entity
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle(); // First world (overworld)
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "IngoBot");

        // SKIN
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://api.ashcon.app/mojang/v2/user/%s", "IngoBot")).openConnection();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                ArrayList<String> lines = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reader.lines().forEach(line -> lines.add(line));
                String reply = String.join(" ", lines);
                int indexOfValue = reply.indexOf("\"value\": \"");
                int indexOfSignature = reply.indexOf("\"signature\": \"");
                String skin = reply.substring(indexOfValue + 10, reply.indexOf("\"", indexOfValue + 10));
                String signature = reply.substring(indexOfSignature + 14, reply.indexOf("\"", indexOfSignature + 14));
                gameProfile.getProperties().put("textures", new Property("textures", skin, signature));
            }
            else {
                Bukkit.getLogger().warning("Failed to open connection when loading IngoBot skin: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            Bukkit.getLogger().warning("Error while opening connection when loading IngoBot skin.");
            e.printStackTrace();
        }
        ingobotNPC = new EntityPlayer(nmsServer, nmsWorld, gameProfile);
        ingobotNPC.setLocation(/*x*/160.5, /*y*/55, /*z*/208.5, /*yaw*/90, /*pitch*/0);
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getScheduler().cancelTasks(this);
        wThread.end();
        cThread.end();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        discord.sendChat("<" + sender.getName() + "> /" + label + " " + argsToString(args), false);
        if (label.equalsIgnoreCase("i")) {
            CommandResult result;
            result = CoreCommands.executeCommand(this, args[0], Arrays.copyOfRange(args, 1, args.length), sender.getName(), wThread, false, discord);
            if (!result.isSuccessful()) {
                IngoBot.sendMessageRaw(ChatColor.RED + result.toString(), sender, discord);
                if (result.isUnhandledException()) {
                    result.printStackTrace(discord);
                }
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    @EventHandler
    public boolean onChatMessage(AsyncPlayerChatEvent event) {
        if (event.isAsynchronous()) {
            while (cThread.queueUsed()); // Wait
            chatQueue.add(new ChatMessage(event.getMessage(), event.getPlayer().getName()));
            discord.sendChat("<" + event.getPlayer().getName() + "> " + event.getMessage(), false);
        }
        return true;
    }

    public void scheduleCommand(ScheduledCommand scheduledCommand) {
        while (!Bukkit.isPrimaryThread() && mainLoop.queueUsed); // Wait until queue is not used
        commandQueue.add(scheduledCommand);
    }

    public void scheduleMessage(Message message) {
        while (!Bukkit.isPrimaryThread() && mainLoop.queueUsed); // Wait until queue is not used
        outputQueue.add(message);
    }

    public static String argsToString(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s + " ");
        }
        return sb.toString().trim();
    }

    @EventHandler
    public boolean onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        createNPCFor(ingobotNPC, p);
        return true;
    }

    @EventHandler
    public boolean onBroadcastMessage(BroadcastMessageEvent event) {
        // For Discord messages
        String msg = event.getMessage();
        if (msg.contains("#") && msg.contains("<") && msg.contains(">") && msg.contains("!")) {
            String sender = StringUtils.substringBetween(msg, "<", ">");
            while (cThread.queueUsed()); // Wait
            chatQueue.add(new ChatMessage("!" + event.getMessage().split("!")[1], sender));
            discord.sendChat(event.getMessage(), false);
        }
        return true;
    }

    public void createNPCFor(EntityPlayer npc, Player target) {
        PlayerConnection connection = ((CraftPlayer)target).getHandle().b; /* playerconnection */
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a /* add */, npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
    }

    public void sendCommand(String contentRaw) {
        while (!Bukkit.isPrimaryThread() && mainLoop.queueUsed); // Wait until queue is not used
        syncCommands.add(contentRaw);
    }

    public void scheduleMinecord(ExternalMessageEvent messageEvent) {
        while (!Bukkit.isPrimaryThread() && mainLoop.queueUsed); // Wait until queue is not used
        minecordBC.add(messageEvent);
    }

    public boolean scheduleCountdown(Player senderP, double time) {

        // Max 1 countdown per player, 5 total.

        if (countdowns.size() > 4) {
            return false;
        }

        for (Countdown cd : countdowns) {
            if (cd.target == senderP) {
                return false;
            }
        }

        while (!Bukkit.isPrimaryThread() && mainLoop.queueUsed); // Wait until queue is not used
        countdowns.add(new Countdown(discord, senderP, time));

        return true;

    }
    public static List<String> getPlayerList() {
        LinkedList<String> pList = new LinkedList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            pList.add(p.getName());
        }
        return pList;
    }

    public void whitelist(String ign) {
        WhitelistThread t = new WhitelistThread(ign);
        Bukkit.getScheduler().runTaskAsynchronously(this, t);
    }

    class WhitelistThread implements Runnable {

        String ign;

        public WhitelistThread(String ign) {
            this.ign = ign;
        }

        @Override
        public void run() {
            try {
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + ign);
                InputStream in = url.openStream();
                // Exists
                if (in.available() > 0) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(ign);
                    whitelistQueue.add(player);
                    wThread.add(new Query(Type.COLOR, null, new String[]{ign, "0", "255", "0"}, false));
                } else {
                    discord.sendTo(discord.getChannels().spreadsheetChannel, "Failed to whitelist " + ign + " (Name does not exist)");
                    wThread.add(new Query(Type.COLOR, null, new String[]{ign, "255", "0", "255"}, false));
                }
            } catch (Exception e) {
                discord.sendTo(discord.getChannels().spreadsheetChannel, "Whitelisted user " + ign + ", (Exception: " + e.getMessage() + ")");
                wThread.add(new Query(Type.COLOR, null, new String[]{ign, "255", "0", "255"}, false));
            }
        }

    }
}