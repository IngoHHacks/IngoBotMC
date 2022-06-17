package tv.ingoh.minecraft.plugins.ingobotcore.web;

import net.md_5.bungee.api.ChatColor;

public class HistoryEntry {

    public String fsInput = "";
    public String fsOutput = "";
    public String string;
    public String user;
    public String model;
    public boolean isFinish;
    public boolean isInput;

    public HistoryEntry(String string, String user, String model, boolean isFinish, boolean isInput, String fsInput, String fsOutput) {
        this.string = string;
        this.user = user;
        this.model = model;
        this.isFinish = isFinish;
        this.isInput = isInput;
        this.fsInput = fsInput;
        this.fsOutput = fsOutput;
    }

    public HistoryEntry(String string, String user, String model, boolean isFinish, boolean isInput) {
        this.string = string;
        this.user = user;
        this.model = model;
        this.isFinish = isFinish;
        this.isInput = isInput;
    }

    public String formattedString() {
        return isFinish ? (ChatColor.DARK_PURPLE + fsInput + ChatColor.LIGHT_PURPLE + fsOutput) : (isInput ? (ChatColor.DARK_PURPLE + string.substring(0, string.length()-1)) : (ChatColor.LIGHT_PURPLE + string.substring(0, string.length()-1)));
    }

}
