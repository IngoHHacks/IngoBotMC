package net.ingoh.minecraft.plugins.ingobotmc;

public class Message {

    String message;
    String receiver;
    long delay;

    public Message(String message, String receiver, int delay) {
        this.message = message;
        this.receiver = receiver;
        this.delay = System.currentTimeMillis() + delay;
    }

    public Message(String message, String receiver) {
        this.message = message;
        this.receiver = receiver;
        delay = 0;
    }
}
