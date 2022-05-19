package tv.ingoh.minecraft.plugins.ingobotcore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mongodb.client.model.Updates;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import io.github.starsdown64.minecord.api.ExternalMessageEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.players.PlayerList;
import tv.ingoh.minecraft.plugins.ingobotcore.chat.ChatThread;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ChatMessage;
import tv.ingoh.minecraft.plugins.ingobotcore.command.CommandResult;
import tv.ingoh.minecraft.plugins.ingobotcore.command.CoreCommands;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ScheduledCommand;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;
import tv.ingoh.minecraft.plugins.ingobotcore.web.AsyncWebThread.Type;
import tv.ingoh.minecraft.plugins.ingobotcore.web.Query;
import tv.ingoh.minecraft.plugins.ingobotcore.web.WebThread;
import tv.ingoh.util.Mongo;
import tv.ingoh.util.PlayerListHandler;
import tv.ingoh.util.RandomThings;

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
    public EntityPlayer ingobotNPC;
    LinkedList<String> syncCommands;
    LinkedList<ExternalMessageEvent> minecordBC;
    ArrayList<Countdown> countdowns;
    LinkedList<OfflinePlayer> whitelistQueue;
    LinkedList<Mob> sacrifices;
    LinkedList<Integer> sacrificeAges;

    MinecraftServer nmsServer;
    WorldServer nmsWorld; // First world (overworld)
    GameProfile gameProfile;

    Mongo mongo;
    BukkitTask mongoTask;

    @Override
    public void onEnable() {
        chatQueue = new LinkedList<>();
        plugin = this;
        config = new Config(this);
        config.load();
        discord = new DiscordInterface(config, this);
        mongo = new Mongo(config, discord);
        mongoTask = Bukkit.getScheduler().runTaskAsynchronously(this, mongo);
        try {
            discord.start();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        try {
            // Wait for three seconds to load JDA
            // TODO: Find workaround
            Thread.sleep(3000);
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
        sacrifices = new LinkedList<>();
        sacrificeAges = new LinkedList<>();

        mainLoop = new MainLoop(this, outputQueue, commandQueue, syncCommands, minecordBC, countdowns, whitelistQueue, discord, sacrifices, sacrificeAges);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, mainLoop, 0, 1);

        // Entity
        nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        nmsWorld = ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle(); // First world (overworld)
        gameProfile = new GameProfile(UUID.fromString("54655d3e-47ae-43f8-8108-310967590778"), "IngoBot");

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
        ingobotNPC = new ListeningNPC(nmsServer, nmsWorld, gameProfile, this, discord);

        ingobotNPC.m(true); // Invulnerable

        try {
            InetSocketAddress address = new InetSocketAddress(Bukkit.getIp(), Bukkit.getPort());

            NetworkManager conn = NetworkManager.a(address, true);

            nmsWorld.a(ingobotNPC); // Add to overworld

            nmsServer.ac().a(conn, ingobotNPC); // Summon to create connection

            nmsServer.ac().t().add(ingobotNPC); // Add to player list

            ingobotNPC.b(/*x*/160.5, /*y*/55, /*z*/208.5, /*yaw*/90, /*pitch*/0);

            RandomThings.initialize();

            Field f;
            try {
                // CRINGE PATCH STUFF
                f = PlayerList.class.getDeclaredField("playersByName");
                f.setAccessible(true);
                Map<String,EntityPlayer> map = (Map<String,EntityPlayer>) f.get(nmsServer.ac());
                map.put("ingobot", ingobotNPC);

            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                Bukkit.getLogger().warning("[IngoBotCore] playersByName field not found");
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("[IngoBotCore] ERROR: Exception: " + e.getMessage());
            Arrays.stream(e.getStackTrace()).forEach(x -> Bukkit.getLogger().severe("[IngoBotCore] DEBUG: Exception: " + x.toString()));
        }
        
    }

    @Override
    public void onDisable() {
        int ms = mongo.getQueue().size();
        if (ms > 0) {
            discord.sendDebug("WARNING: Mongo queue not empty! " + ms + " elements still remaining!");
        }
        Bukkit.getServer().getScheduler().cancelTasks(this);
        wThread.end();
        cThread.end();
        mongo.end();
        mongoTask.cancel();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        discord.sendChat("<" + sender.getName() + "> /" + label + " " + argsToString(args), false);
        if (label.equalsIgnoreCase("i")) {
            CommandResult result;
            if (args.length > 0) {
                result = CoreCommands.executeCommand(this, args[0], Arrays.copyOfRange(args, 1, args.length), sender.getName(), wThread, false, discord);
                if (!result.isSuccessful()) {
                    IngoBot.sendMessageToRaw(ChatColor.RED + result.toString(), discord, false, sender.getName());
                    if (result.isUnhandledException()) {
                        result.printStackTrace(discord);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                CoreCommands.printCommandList(sender.getName(), false, discord);
            }
        }
        return true;
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if (event.isAsynchronous()) {
            while (cThread.queueUsed()); // Wait
            chatQueue.add(new ChatMessage(event.getMessage(), event.getPlayer().getName()));
            discord.sendChat("<" + event.getPlayer().getName() + "> " + event.getMessage(), false);
        }
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
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Mob)) return;
        if (event.getDamage() >= 999999999) return;
        Mob entity = ((Mob)event.getEntity());
        if(entity.getHealth() - event.getFinalDamage() <= 0.0d) {
            double x = event.getEntity().getLocation().getX();
            double y = event.getEntity().getLocation().getY();
            double z = event.getEntity().getLocation().getZ();
            if (x > 155.5 && x < 165.5 &&  y > 50 && y < 60 && z > 203.5 && z < 213.5) {
                try {
                    Method getHandle = entity.getClass().getMethod("getHandle");
                    Object eo = getHandle.invoke(entity);
                    Field field = eo.getClass().getField("Q" /*noPhysics*/);
                    field.setAccessible(true);
                    field.setBoolean(eo, true);
                    sacrifices.add(entity);
                    sacrificeAges.add(0);
                    entity.setGravity(false);
                    entity.setAware(false);
                    addSacrifice();
                    if(event instanceof EntityDamageByEntityEvent) {
                        Entity attacker = ((EntityDamageByEntityEvent)event).getDamager();
                        if (attacker instanceof Player) {
                            addSacrificeFor((Player)attacker);
                        }
                    }
                    event.setCancelled(true);
                } catch (Exception ignore) {}
            }
        }
    }

    private void addSacrifice() {
        Document q = new Document().append("_id", "sacrifices");
        Bson u = Updates.combine(Updates.inc("count", 1));
        mongo.update("meta", q, u, true);
    }

    private void addSacrificeFor(Player player) {
        Document q = new Document().append("_UUID", player.getUniqueId().toString());
        Bson u = Updates.combine(Updates.inc("_sacrifices", 1));
        mongo.update("players", q, u, true);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null && event.getEntity().getKiller().equals(ingobotNPC.getBukkitEntity())) {
            event.getDrops().clear();
            event.setDroppedExp(0);
            for (Entity en : ingobotNPC.getBukkitEntity().getNearbyEntities(10, 10, 10)) {
                if (en instanceof Player) {
                    playAttackAnimationFor(ingobotNPC, (Player) en);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        createNPCFor(ingobotNPC, p);
        Document q = new Document().append("_UUID", event.getPlayer().getUniqueId().toString());
        Bson u = Updates.combine(Updates.set("_username", event.getPlayer().getName()),
                                 Updates.currentTimestamp("_last_online"));
        mongo.update("players", q, u, true);
        PlayerListHandler.updatePlayerList(config, discord, new LinkedList<>());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Document q = new Document().append("_UUID", event.getPlayer().getUniqueId().toString());
        Bson u = Updates.combine(Updates.set("_username", event.getPlayer().getName()),
                                 Updates.currentTimestamp("_last_online"));
        mongo.update("players", q, u, true);
        LinkedList<String> exclude = new LinkedList<>();
        exclude.add(event.getPlayer().getName());
        PlayerListHandler.updatePlayerList(config, discord, exclude);
    }

    @EventHandler
    public void onBroadcastMessage(BroadcastMessageEvent event) {
        // For Discord messages
        String msg = event.getMessage();
        if (msg.contains("#") && msg.contains("<") && msg.contains("> ") && msg.contains("!")) {
            String cmd = StringUtils.substringAfter(msg, "> ");
            if (cmd.charAt(0) == '!' && cmd.length() > 1) {
                String sender = StringUtils.substringBetween(msg, "<", ">");
                while (cThread.queueUsed()); // Wait
                chatQueue.add(new ChatMessage(cmd, sender));
                discord.sendChat(event.getMessage(), false);
            }
        }
    }

    public void createNPCFor(EntityPlayer npc, Player target) {
        PlayerConnection connection = ((CraftPlayer)target).getHandle().b; /* playerconnection */
        connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a /* add */, npc));
        connection.a(new PacketPlayOutNamedEntitySpawn(npc));
        connection.a(new PacketPlayOutEntityHeadRotation(npc, (byte)(64)));
    }

    public void playAttackAnimationFor(EntityPlayer npc, Player target) {
        PlayerConnection connection = ((CraftPlayer)target).getHandle().b; /* playerconnection */
        connection.a(new PacketPlayOutAnimation(npc, 0));
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
                discord.sendTo(discord.getChannels().spreadsheetChannel, "Failed to whitelist " + ign + ", (Exception: " + e.getMessage() + ")");
                wThread.add(new Query(Type.COLOR, null, new String[]{ign, "255", "0", "255"}, false));
            }
        }

    }

    public Mongo getMongo() {
        return mongo;
    }

    public MinecraftServer getNmsServer() {
        return nmsServer;
    }

    public WorldServer getNmsWorld() {
        return nmsWorld;
    }

    public EntityPlayer getIngobotNPC() {
        return ingobotNPC;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }
}