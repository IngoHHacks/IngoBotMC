package tv.ingoh.minecraft.plugins.ingobotcore;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
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

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mongodb.client.model.Updates;

import io.github.starsdown64.minecord.api.ExternalMessageEvent;
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

    final static String INGODOG_VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTY1NDEwNjgwMDQzNywKICAicHJvZmlsZUlkIiA6ICI2MTA4NWI1MTViZmQ0ZTRjOWNkM2U0OTRkZTNiMGFkMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJJbmdvSCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zOWM3OWRhNGU4Mjc4NWQ2OTJmNzIwZDgzMGVkYWNiNDExZGJiYzhlNDM0MWIwODkzZDdiNzk4ODBiNjE1OWEyIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xNzkxMjc5MGZmMTY0YjkzMTk2ZjA4YmE3MWQwZTYyMTI5MzA0Nzc2ZDBmMzQ3MzM0ZjhhNmVhZTUwOWY4YTU2IgogICAgfQogIH0KfQ==";
    final static String INGODOG_SIGNATURE = "qAcVd/ibckIQAjXp5GCwmbg5jvNJBgxwOMKy6mASRM6DBFHx2sBFqxa7/4igY+q1IAt1W6RVL6oyH13ld8PrtU6zQWlGaNNZXmuOyv1NiKZZRjInoYC9nv+IwqSsouOaUNf3oNsK8c7m4K/XKd5nSFGx4yoYdcx3lKbyAptqDHI8dLH+yIURw4nSea/69HwB4u7b47vCMMCUxCU6tEOUF5hycngUn+j1yso0ASq1rRlLWDrtFC+5QxF1lwgKNTi/oXTOfSxEZTE6g0Lizz8xp0EZ/bXjoayEQQLR/2bD3ANGWgIMglm57yf+ksyeBJ/CSMOIQjq0JwIoPMThf59u3oUAm7bgaZsy5k9iLxOJm0CP132U37OUnV29UFbF1gAGjrYJJaQMNfOGmOrLqfxSbON73GrZwgnnH6CPppRCuuNE2VC1gNcfY3pFw3zgEcajjtc+3XWd/nU9LDnxkco0iKSt5EZ12VKMAmt4GSX+OmzDM59IvyP2uZA7nZvIjuaFUVvtIJHUYDfgO4NiIGvPpp99r9dxjhm5b8rMuYzdz5h4jU1G+tszypMB2NRKAZEgpZSaDwkxB1asHX5GBlJBSnMR8mZuhRkAZJmbMjF89cfxo8Q5HX7Npjf/fQQol/U6938QX1DNtl9rxEMsBbgOXykK3x+MlIVDJXC/eQyuQiE=";
    final static String INGOFOX_VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTY1NDEwNjkxMTg5OSwKICAicHJvZmlsZUlkIiA6ICI1NDY1NWQzZTQ3YWU0M2Y4ODEwODMxMDk2NzU5MDc3OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJJbmdvQm90IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk2ZmJiZjM0YWRmMmNjMjdkYzU0MDBhNTNlYTdlMmM2ZGI4NGJjYjkzODJlOTUzMTQ2Yjk5YjFlODVmZTUyMzUiCiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIzNDBjMGUwM2RkMjRhMTFiMTVhOGIzM2MyYTdlOWUzMmFiYjIwNTFiMjQ4MWQwYmE3ZGVmZDYzNWNhN2E5MzMiCiAgICB9CiAgfQp9";
    final static String INGOFOX_SIGNATURE = "SAax+T4KD2MTUf/FBuPuUdNwmC7vNluKHVAIg6WZcqPsjZ1Qh000udD5k4DyBSj9z+P7p50Rdbyx3plK2m1NByk+sqfCvd9HcQ6cHZoDQf73r1h+EU4MUbCpXh0VU93htZVsH9/DeeY/gakp6Z/FB0eywlmunKKx3GSZ9wun3NshIDNerxqs8F5EYA5LLnJGC6J4lIY8yWbc8w3fJUFjFweJjC0o+YaeCzLrrDD751xDArE6pTRoU/wvxU0An9kvrDWvx2h2omD7NzL0T6VdV7i32Sz5GAZn2y8Zk3We9aKzzfNhS3LfZuEOKOyqSki+TwM0Q2ZLV5btcQ3pCEPTSmDVWQlFIaILMWSbfWf8NdpwDegGNW8y0OELI2EzdC2il+H1iwHzHiMhQ5/HEAZxnwdbVSuLhMfiIK5GrunP+cX50FHdLTDHProBYbEIu/7OY/Junrow7KEd0gcvJa8yoJi+WOCW9e5A4bJDTWNAObxB3TN0TRsuDFmj58y+njSQgtjBNvbCvGPBCTVp7I16qXWvDVb16sBhA9ZBEGK0L4RmBQz4eCnqeiPq7K3N/Odw7V1I9CKOjaYkaTYNp/16gBoj+awSBXhKrQ65qz1cKGwyThuETbaZtSEfefqOSYDVTtGdwDQmqhY7HJqBIoHj3ARz2gFnP0OykoyKVHnKlLU=";
    final static String INGOFOX_WITH_MAPMAKER_CAPE_VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTY1NDEwNzE5MDM2OSwKICAicHJvZmlsZUlkIiA6ICI2MTA4NWI1MTViZmQ0ZTRjOWNkM2U0OTRkZTNiMGFkMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJJbmdvSCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85NmZiYmYzNGFkZjJjYzI3ZGM1NDAwYTUzZWE3ZTJjNmRiODRiY2I5MzgyZTk1MzE0NmI5OWIxZTg1ZmU1MjM1IgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xNzkxMjc5MGZmMTY0YjkzMTk2ZjA4YmE3MWQwZTYyMTI5MzA0Nzc2ZDBmMzQ3MzM0ZjhhNmVhZTUwOWY4YTU2IgogICAgfQogIH0KfQ==";
    final static String INGOFOX_WITH_MAPMAKER_CAPE_SIGNATURE = "tta+gV+zvVB+I6YMWwuSCmbXUF/go+wMwdUREKd5GS0XxKpy4jchQbZqYb+iwIWf0GVAKdZxyR+7YxK3yctaF76C3joIDIqEodLShLC0cAWZE/mlmttMV5aLmhMsw6QrAFbXSjiSKpVpniWMUMVy7d8wvtkFkYVuAQNvxIgbJHsFj5xswBZ1k1uUntA6LPKx8jQlLdMbRb/GLHetlR4EnG3xY0VNtVjhjE+Qezs9NYsmN65jjlW8laXLGFTaZcuw5qWoEbmQsD71MDvCxemEk4e9ljI/jYkumo2XMU/JokenHAi8y+yoSsUq63kw4ihbiGG4z/I/kmBy6Om3W3xOUjzTxsXIP0JHO8UR7t68r1x/SmL3tuZUuEKHuaIRXC3d2JqKzqu8bU6L5odXElLfvk1EcSMOYVdb6dOQgSbYcsEGkmImCTE0i177u3ZVhJOfyTDo4LurOmEe4WUHPD8i1WH/f3okTr6PZuIFwCqOS7CbLja37YOysfH8Mddmhy7KsLmw1q5V67XJ5EBv/KVfQxGhWSR9AaZAbvWN2EBdSYL9ODm1JUM/n/oTO13ybj8OWbO8xwUFg1Dw1yDjBIUU8oP+a9MdIt9TzvuAujtlDUnukFcbsNR2iPKX5yBM2hu2YjdFiPDF0PxENqBxq4f+MzWxXnUsEVxf2w1JlJoNG3g=";

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
            f2 = EntityPlayer.class.getField("bP"); // DATA_PLAYER_MODE_CUSTOMISATION 
            f2.setAccessible(true);
            DataWatcherObject<Byte> dpmc = (DataWatcherObject<Byte>) f2.get(null);
            ingobotNPC.ai().b(dpmc, (byte) 126); // Enable second layer (no cape)
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            Bukkit.getLogger().warning("[IngoBotCore] bP field not found");
        }

        try {
            InetSocketAddress address = new InetSocketAddress(Bukkit.getIp(), Bukkit.getPort());

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
}