package tv.ingoh.minecraft.plugins.ingobotcore;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.damagesource.DamageSource;
import tv.ingoh.minecraft.plugins.ingobotcore.discord.DiscordInterface;

public class ListeningNPC extends EntityPlayer {

    Main main;
    DiscordInterface discord;

    public ListeningNPC(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, Main main, DiscordInterface discord) {
        super(minecraftserver, worldserver, gameprofile);
        this.main = main;
        this.discord = discord;
    }

    @Override
    public void a(IChatBaseComponent ichatbasecomponent, ChatMessageType cType, UUID uuid) {
        super.a(ichatbasecomponent, cType, uuid);
        for (IChatBaseComponent component : ichatbasecomponent) {
            discord.sendChat(ichatbasecomponent.getString());
        }
    }
}
