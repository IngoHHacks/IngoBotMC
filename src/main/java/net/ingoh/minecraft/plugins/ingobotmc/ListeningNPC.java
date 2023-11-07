package net.ingoh.minecraft.plugins.ingobotmc;

import com.mojang.authlib.GameProfile;

import net.ingoh.minecraft.plugins.ingobotmc.discord.DiscordInterface;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;

public class ListeningNPC extends ServerPlayer {

    Main main;
    DiscordInterface discord;

    public ListeningNPC(MinecraftServer minecraftserver, ServerLevel worldserver, GameProfile gameprofile, Main main, DiscordInterface discord) {
        super(minecraftserver, worldserver, gameprofile, new ClientInformation("en_us", 2, ChatVisiblity.HIDDEN, true, 0, HumanoidArm.RIGHT, false, false));
        this.main = main;
        this.discord = discord;
    }

    @Override
    public void displayClientMessage(Component ichatbasecomponent, boolean a) {
        super.displayClientMessage(ichatbasecomponent, a);
        discord.sendChat(ichatbasecomponent.getString(), false);
    }
}
