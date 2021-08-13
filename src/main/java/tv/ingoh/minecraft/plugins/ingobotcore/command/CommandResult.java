package tv.ingoh.minecraft.plugins.ingobotcore.command;

import org.bukkit.Bukkit;

import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;

public class CommandResult {

    ResultType type;
    String text;
    Exception exception;

    public CommandResult(ResultType type) {
        this.type = type;
        text = type.text;
    }

    public CommandResult(ResultType type, Exception exception) {
        this.type = type;
        text = type.text;
        this.exception = exception;
    }

    public CommandResult(ResultType type, String... replace) {
        this.type = type;
        text = type.text;
        for (int i = 0; i < replace.length; i++) {
            text = text.replace("%%" + i, replace[i]);
        }
        if (text.contains("%%")) {
            Bukkit.getLogger().warning(text + " might be improperly initialized.");
        }
    }

    public boolean isSuccessful() {
        return type.equals(ResultType.SUCCESS);
    }

    public boolean isUnhandledException() {
        return type.equals(ResultType.EXECUTIONERROREXCEPTION);
    }

    public void printStackTrace(DiscordInterface discord) {
        if (exception != null) {
            discord.sendDebug(exception.getMessage());
            discord.printStackTrace(exception.getStackTrace());
        }
    }

    @Override
    public String toString() {
        return text;
    }
    
}
