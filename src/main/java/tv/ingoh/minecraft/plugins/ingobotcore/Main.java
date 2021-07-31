package tv.ingoh.minecraft.plugins.ingobotcore;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import tv.ingoh.minecraft.plugins.ingobotcore.chat.ChatThread;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ChatMessage;
import tv.ingoh.minecraft.plugins.ingobotcore.command.CommandResult;
import tv.ingoh.minecraft.plugins.ingobotcore.command.CoreCommands;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ResultType;
import tv.ingoh.minecraft.plugins.ingobotcore.command.ScheduledCommand;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;
import tv.ingoh.minecraft.plugins.ingobotcore.web.WebThread;

public class Main extends JavaPlugin implements Listener {

    JavaPlugin plugin;
    Config config;
    public DiscordInterface discord;
    LinkedList<ChatMessage> chatQueue;
    WebThread wThread;
    ChatThread cThread;
    LinkedList<Message> outputQueue;
    LinkedList<ScheduledCommand> commandQueue;
    MainLoop mainLoop;

    @Override
    public void onEnable() {
        chatQueue = new LinkedList<>();
        plugin = this;
        config = new Config(this);
        config.load();
        discord = new DiscordInterface(config);
        try {
            discord.start();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("i").setTabCompleter(new TabCompleter() {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
                return null;
            }
        });

        wThread = new WebThread();
        wThread.run(this);

        cThread = new ChatThread();
        cThread.run(this, chatQueue, wThread);

        outputQueue = new LinkedList<>();
        commandQueue = new LinkedList<>();
        
        mainLoop = new MainLoop(outputQueue, commandQueue, discord);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, mainLoop, 0, 1);

    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getScheduler().cancelTasks(this);
        wThread.end();
        cThread.end();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        discord.sendChat("<" + sender.getName() + "> /" + label + " " + argsToString(args));
        if (label.equalsIgnoreCase("i")) {
            CommandResult result;
            if (args.length >= 1)  {
                result = CoreCommands.executeCommand(args[0], Arrays.copyOfRange(args, 1, args.length), sender.getName(), wThread, false, discord);
            } else {
                result = new CommandResult(ResultType.TOOFEWARGUMENTSEXCEPTION, "0", "1+");
            }
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
            chatQueue.add(new ChatMessage(event.getMessage(), event.getPlayer()));
            discord.sendChat("<" + event.getPlayer().getName() + "> " + event.getMessage());
        }
        return true;
    }

    public void scheduleCommand(ScheduledCommand scheduledCommand) {
        while (mainLoop.queueUsed); // Wait until queue is not used
        commandQueue.add(scheduledCommand);
    }

    public void scheduleMessage(Message message) {
        while (mainLoop.queueUsed); // Wait until queue is not used
        outputQueue.add(message);
    }

    public static String argsToString(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s + " ");
        }
        return sb.toString().trim();
    }

}