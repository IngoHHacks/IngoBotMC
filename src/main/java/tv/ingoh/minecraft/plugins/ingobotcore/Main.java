package tv.ingoh.minecraft.plugins.ingobotcore;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import io.github.starsdown64.minecord.api.ExternalMessageEvent;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
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

    final static String INGOFOX_VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTY1NTQ4ODg0Nzg4NiwKICAicHJvZmlsZUlkIiA6ICI1NDY1NWQzZTQ3YWU0M2Y4ODEwODMxMDk2NzU5MDc3OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJJbmdvQm90IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2JjMDVlNTc0YjMxM2M3NmRlODZiMmRkM2Q0MWFkMGU1NzlkMjFmMGM2OWM5NGQyNjFjNDc0OWFhMDdlODFlMzUiCiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIzNDBjMGUwM2RkMjRhMTFiMTVhOGIzM2MyYTdlOWUzMmFiYjIwNTFiMjQ4MWQwYmE3ZGVmZDYzNWNhN2E5MzMiCiAgICB9CiAgfQp9";
    final static String INGOFOX_SIGNATURE = "cXir+S8fTfedFpidNDpdK83ISJY5yXWURI5A6Z01L4G+69ftNV4ftgWfixov9hP13sKQDmgR1CUuaAdqVyY0sqG/lg6VLq6bsnse+U2ZnbC5og+SHHwo33hEVWIrMEuAOJvqZliy04TtSFVyshJLnmEoQyHR7l89AOWbvjNx53CRv2n7nkdA1cDXQ/1RkfrVienr57ooNRPJBidq1mMilV+yaBeLq6cK5Hb5k/bzqKl7ct1wayeGj3FR4EVJV8zUBq8DRin8HiSqRVc+SisK7fcdVcDxVfEUL0XKxTjuLV/2q5Apfx+W/weEELkrnibGWvSbereZ1M/JX+FWEaABm8EMAGBfD9beJg15P9US6ZXhl56EFPGV+ddalkqehsDFPyMyi1NhGoBTLdXUIPX7L9HrmGTFKbbE2axVAdPOjZhfeSYYFEgHLII7sNY6SA1ncACYmZD2iajLyyXsKLWoqUVNl9iCwBOkgudEb0GHplyNZQl5X2oyJmjCPzNOI7tiH2bHWb1s6WWPGCl7iKYB0KhnuhukAARjMZGQ6HZlbUsAikFrkwYh+11/Vzn44LDNsK6Vx6dNGKHy3kLKeMAwwCvXVf6eTvLXaT2I8PngdcXojbu4lekoiEy/M9CuJyBJ7LL3OIRM/hQuIIlHBHvVaSsYYBw1tZRwPaElBx1MBNY=";


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
        gameProfile.getProperties().put("textures", new Property("textures", INGOFOX_VALUE, INGOFOX_SIGNATURE));

        ingobotNPC = new ListeningNPC(nmsServer, nmsWorld, gameProfile, this, discord);

        ingobotNPC.m(true); // Invulnerable

        Field f2;
        try {
            f2 = EntityPlayer.class.getField("bO"); // DATA_PLAYER_MODE_CUSTOMISATION 
            f2.setAccessible(true);
            DataWatcherObject<Byte> dpmc = (DataWatcherObject<Byte>) f2.get(null);
            ingobotNPC.ai().b(dpmc, (byte) 126); // Enable second layer (no cape)
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            Bukkit.getLogger().warning("[IngoBotCore] bO field not found");
        }

        try {
            InetSocketAddress address = new InetSocketAddress(config.getFakeConnectionHost(), config.getFakeConnectionPort());

            NetworkManager conn = NetworkManager.a(address, true);

            ingobotNPC.getBukkitEntity().setSleepingIgnored(true);

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
        connection.a(new PacketPlayOutEntityMetadata(npc.ae(), npc.ai(), true));
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

    public boolean scheduleCountdown(Player senderP, double time, boolean uppies) {

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
        countdowns.add(new Countdown(discord, senderP, time, uppies));

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

    public void watchTick() {
        double dist = 10;
        Location li = ingobotNPC.getBukkitEntity().getLocation().clone().add(0, 1.6, 0);
        Location l = li.clone().add(-1, 0, 0);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.equals(ingobotNPC.getBukkitEntity()) || (player.getWorld() != ingobotNPC.getBukkitEntity().getWorld())) continue;
            if (player.getGameMode() != GameMode.SPECTATOR) {
                double nd = player.getLocation().clone().add(0, 1.6, 0).distance(li);
                if (nd < dist) {
                    l = player.getLocation().clone().add(0, 1.6, 0);
                    dist = nd;
                }
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.equals(ingobotNPC.getBukkitEntity()) || (player.getWorld() != ingobotNPC.getBukkitEntity().getWorld())) continue;
            if (player.getLocation().clone().add(0, 1.6, 0).distance(li) < 50) {
                PlayerConnection connection = ((CraftPlayer)player).getHandle().b; /* playerconnection */
                Tuple<Double, Double> rotation = rotateTowards(li, l);
                connection.a(new PacketPlayOutEntityHeadRotation(ingobotNPC, (byte)(rotation.a() * (256.0 / 360.0))));
                connection.a(new PacketPlayOutEntityLook(ingobotNPC.ae(), (byte)(rotation.a() * (256.0 / 360.0)), (byte)(rotation.b() * (256.0 / 360.0)), true));
            }
        }
    }

    public void bonk() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location l = player.getLocation();
            if (l.getX() > 155.5 && l.getX() < 165.5 &&  l.getY() > 50 && l.getY() < 60 && l.getZ() > 203.5 && l.getZ() < 213.5) {
                PlayerConnection connection = ((CraftPlayer)player).getHandle().b; /* playerconnection */
                connection.a(new PacketPlayOutAnimation(ingobotNPC, 1));
            }
        }
    }

    private Tuple<Double, Double> rotateTowards(Location from, Location to) {
        double dx = to.getX()-from.getX();
        double dy = to.getY()-from.getY();
        double dz = to.getZ()-from.getZ();
        double r = Math.sqrt(dx*dx + dy*dy + dz*dz);
        double yaw = -Math.atan2(dx,dz)/Math.PI*180;
        if (yaw < 0) {
            yaw = 360 + yaw;
        }
        double pitch = -Math.asin(dy/r)/Math.PI*180;
        return new Tuple<Double,Double>(yaw, pitch);
    }

    public void addSWC(String sender, boolean isPublic, String user, int amount, boolean addToTotal) {

        Document q = new Document().append("_username", user);
        Function<FindIterable<Document>, String> postpost = new Function<FindIterable<Document>,String>() {
            @Override
            public String apply(FindIterable<Document> doc) {
                Document docf = doc.first();
                if (docf != null) {
                    int swc = (Integer) docf.get("_SWC");
                    Object ts = docf.get("_Total_SWC");
                    if (ts != null) {
                        int tswc = (Integer) ts;
                        IngoBot.sendMessageTo(ChatColor.BLUE + "They now have " + swc  + " SWC (" + tswc + " total).", discord, isPublic, sender);
                    } else {
                        IngoBot.sendMessageTo(ChatColor.BLUE + "They now have " + swc  + " SWC.", discord, isPublic, sender);
                    }
                }
                return "";
            }
        };
        Function<UpdateResult, String> post = new Function<UpdateResult,String>() {
            @Override
            public String apply(UpdateResult t) {
                if (t.wasAcknowledged() && t.getModifiedCount() >= 1) {
                    if (amount >= 0) IngoBot.sendMessageTo(ChatColor.BLUE + "Added " + amount + " SWC to " + user, discord, isPublic, sender);
                    else IngoBot.sendMessageTo(ChatColor.BLUE + "Removed " + -amount + " SWC from " + user, discord, isPublic, sender);
                    mongo.find("players", q, postpost);
                } else {
                    IngoBot.sendMessageTo(ChatColor.RED + "Something went wrong updating this player's SWC", discord, isPublic, sender);
                }
                return "";
            }
        };   
        Function<FindIterable<Document>, String> first = new Function<FindIterable<Document>,String>() {
            @Override
            public String apply(FindIterable<Document> doc) {
                if (doc.first() != null) {
                    if (addToTotal) {
                        Bson u = Updates.combine(Updates.inc("_SWC", amount), Updates.inc("_Total_SWC", amount));
                        mongo.update("players", q, u, false, post);
                    } else {
                        Bson u = Updates.combine(Updates.inc("_SWC", amount));
                        mongo.update("players", q, u, false, post);
                    }
                } else {
                    IngoBot.sendMessageTo(ChatColor.RED + "This player does not exist in the database, idiot.", discord, isPublic, sender);
                }
                return "";
            }
        };
        mongo.find("players", q, first);
    }

    public void getSWC(String sender, boolean isPublic, String user, boolean b) {
        Document q = new Document().append("_username", user);
        Function<FindIterable<Document>, String> first = new Function<FindIterable<Document>,String>() {
            @Override
            public String apply(FindIterable<Document> doc) {
                Document docf = doc.first();
                if (docf != null) {
                    Object rs = docf.get("_SWC");
                    int swc = (rs == null ? 0 : (Integer) rs);
                    Object ts = docf.get("_Total_SWC");
                    int tswc = (ts == null ? 0 : (Integer) ts);
                    if (tswc != 0) {
                        if (sender.equals(user)) IngoBot.sendMessageTo(ChatColor.BLUE + "You have " + swc  + " SWC (" + tswc + " total).", discord, isPublic, sender);
                        else IngoBot.sendMessageTo(ChatColor.BLUE + user + " has " + swc  + " SWC (" + tswc + " total).", discord, isPublic, sender);
                    } else {
                        if (sender.equals(user)) IngoBot.sendMessageTo(ChatColor.BLUE + "You have " + swc  + " SWC.", discord, isPublic, sender);
                        else IngoBot.sendMessageTo(ChatColor.BLUE + user + " has " + swc  + " SWC.", discord, isPublic, sender);
                    }
                } else {
                    IngoBot.sendMessageTo(ChatColor.RED + "This player does not exist in the database, idiot.", discord, isPublic, sender);
                }
                return "";
            }
        };
        mongo.find("players", q, first);
    }

    public void claimMilestones(boolean isPublic, Player senderP) {
        String user = senderP.getName();
        int empty = getEmptySlotCount(senderP.getInventory());
        Document q = new Document().append("_username", user);
        List<ItemReward> rewards = new LinkedList<>();
        List<Bson> updates = new LinkedList<>();
        Function<UpdateResult, String> post = new Function<UpdateResult,String>() {
            @Override
            public String apply(UpdateResult t) {
                if (t.wasAcknowledged() && t.getModifiedCount() >= 1) {
                    for (ItemReward reward : rewards) {
                        senderP.getInventory().addItem(reward.getReward());
                    }
                    IngoBot.sendMessageTo("Claimed " + rewards.size() + " reward(s).", discord, isPublic, user);
                } else {
                    IngoBot.sendMessageTo(ChatColor.RED + "Something went wrong claiming your reward(s)...", discord, isPublic, user);
                }
                return "";
            }
        };   
        Function<FindIterable<Document>, String> first = new Function<FindIterable<Document>,String>() {
            @Override
            public String apply(FindIterable<Document> doc) {
                Document docf = doc.first();
                if (docf != null) {
                    Object ts = docf.get("_Total_SWC");
                    int tswc = (ts == null ? 0 : (Integer) ts);
                    if (tswc >= 50) {
                        Object ms3 = docf.get("_milestone1_claimed");
                        if (ms3 == null) {
                            updates.add(Updates.set("_milestone1_claimed", true));
                            rewards.add(ItemReward.from(ItemReward.Reward.MILESTONE1));
                        }
                        if (tswc >= 300) {
                            Object ms2 = docf.get("_milestone2_claimed");
                            if (ms2 == null) {
                                updates.add(Updates.set("_milestone2_claimed", true));
                                rewards.add(ItemReward.from(ItemReward.Reward.MILESTONE2));
                            }
                            if (tswc >= 1500) {
                                Object ms = docf.get("_milestone3_claimed");
                                if (ms == null) {
                                    updates.add(Updates.set("_milestone3_claimed", true));
                                    rewards.add(ItemReward.from(ItemReward.Reward.MILESTONE3));
                                }
                            }
                        }
                    }
                    if (rewards.size() > 0) {
                        if (rewards.size() <= empty) {
                            Bson u = Updates.combine(updates);
                            mongo.update("players", q, u, false, post);
                        } else {
                            IngoBot.sendMessageTo(ChatColor.RED + "Your inventory is full, idiot. You need at least " + rewards.size() + " empty slot(s), idiot.", discord, isPublic, user);
                        }
                    } else {
                        IngoBot.sendMessageTo("You have no rewards to claim, idiot.", discord, isPublic, user);
                    }
                } else {
                    IngoBot.sendMessageTo(ChatColor.RED + "This player does not exist in the database, idiot.", discord, isPublic, user);
                }
                return "";
            }
        };
        mongo.find("players", q, first);
    }

    public int getEmptySlotCount(Inventory inventory) {
        int ct = 0;
            for (org.bukkit.inventory.ItemStack stack : inventory.getStorageContents()) {
                if (stack == null) ct++;
            }
        return ct;
    }
}