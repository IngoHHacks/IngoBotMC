package net.ingoh.minecraft.plugins.ingobotmc;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;

public class MockConnection extends Connection {
    public MockConnection(PacketFlow enumprotocoldirection) {
        super(enumprotocoldirection);
    }

    @Override
    public void send(Packet<?> packet) {
        // Ignore
    }

    @Override
    public void send(Packet<?> packet, @Nullable PacketSendListener packetsendlistener) {
        // Ignore
    }

    @Override
    public void send(Packet<?> packet, @Nullable PacketSendListener packetsendlistener, boolean flag) {
        // Ignore
    }
}
