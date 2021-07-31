package tv.ingoh.minecraft.plugins.ingobotcore.command;

import org.bukkit.entity.Player;

public class ChatMessage {

    public String message;
    public Player sender;

    public ChatMessage(String message, Player sender) {
        this.message = message;
        this.sender = sender;
    }

}
