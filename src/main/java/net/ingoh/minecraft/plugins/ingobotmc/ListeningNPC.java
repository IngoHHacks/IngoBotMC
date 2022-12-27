package net.ingoh.minecraft.plugins.ingobotmc;

import com.mojang.authlib.GameProfile;

import net.ingoh.minecraft.plugins.ingobotmc.discord.DiscordInterface;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

public class ListeningNPC extends ServerPlayer {

    Main main;
    DiscordInterface discord;

    public ListeningNPC(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Main main, DiscordInterface discord) {
        super(minecraftserver, worldserver, gameprofile);
        this.main = main;
        this.discord = discord;
    }

    @Override
    public void displayClientMessage(Component ichatbasecomponent, boolean a) {
        super.displayClientMessage(ichatbasecomponent, a);
        discord.sendChat(ichatbasecomponent.getString(), false);
    }
}
