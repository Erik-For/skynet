package se.skynet.skyserverbase.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.packet.PacketConstructor;
import se.skynet.skyserverbase.packet.PacketUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class NPC {

    private final int id;
    private final PacketPlayOutPlayerInfo info;
    private final PacketPlayOutNamedEntitySpawn spawn;
    private final PacketPlayOutEntityHeadRotation headRotation;
    private final PacketPlayOutPlayerInfo removeTab;
    private final PacketPlayOutEntityMetadata entityMetaData;
    private final PacketPlayOutScoreboardTeam teamPacket;

    private final NPCClick clickHandler;
    public NPC(SkyServerBase plugin, String name, String displayName, ChatColor nameColor, UUID uuid, String texture, String signature, Location location, NPCClick clickHandler) {
        this.clickHandler = clickHandler;

        MinecraftServer minecraftserver = MinecraftServer.getServer();
        GameProfile gameprofile = new GameProfile(uuid, displayName);
        gameprofile.getProperties().put("textures", new Property("textures", texture, signature));
        WorldServer worldserver = ((CraftWorld) location.getWorld()).getHandle();
        EntityPlayer npc = new EntityPlayer(minecraftserver, worldserver, gameprofile, new PlayerInteractManager(worldserver));

        id = npc.getId();
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        String teamName = "1" + UUID.randomUUID().toString().split("-")[0];

        teamPacket = PacketConstructor.createTeamPacket(teamName, teamName, "", "", ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS, Arrays.asList(displayName));


        info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc);
        spawn = new PacketPlayOutNamedEntitySpawn(npc);
        headRotation = new PacketPlayOutEntityHeadRotation(npc, (byte) ((npc.yaw * 256.0F) / 360.0F));
        DataWatcher dataWatcher = npc.getDataWatcher();
        dataWatcher.watch(10, (byte)0xFF);
        entityMetaData = new PacketPlayOutEntityMetadata(id, dataWatcher, true);

        PacketPlayOutPlayerInfo temp = new PacketPlayOutPlayerInfo();
        temp.new PlayerInfoData(new GameProfile(uuid, "thing"), -1, null, null);
        PacketUtils.setField(temp, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        PacketUtils.setField(temp, "b", Collections.singletonList(temp.new PlayerInfoData(new GameProfile(uuid, "temp"), -1, WorldSettings.EnumGamemode.NOT_SET, null)));
        removeTab = temp;
    }

    public int getId(){
        return id;
    }

    public void onInteract(NPCClickEvent npcClickEvent){
        clickHandler.onClick(npcClickEvent);
    }
    public List<Packet<?>> getPackets(){
        return Arrays.asList(info, spawn, teamPacket, headRotation, entityMetaData);
    }

    public PacketPlayOutPlayerInfo getRemoveTab() {
        return removeTab;
    }
}
