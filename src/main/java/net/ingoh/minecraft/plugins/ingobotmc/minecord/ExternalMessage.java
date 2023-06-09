package net.ingoh.minecraft.plugins.ingobotmc.minecord;

import io.github.starsdown64.minecord.api.ExternalMessageEvent;

public class ExternalMessage {

    ExternalMessageEvent event;

    public ExternalMessage(String message) {
        event = new ExternalMessageEvent(message);
    }

    public ExternalMessageEvent getEvent() {
        return event;
    }
}
