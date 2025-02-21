package se.skynet.skyserverbase.command;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.database.DatabaseMethods;
import se.skynet.skyserverbase.packet.PacketConstructor;
import se.skynet.skyserverbase.packet.PacketUtils;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

import java.util.List;

public class UnnickCommand extends Command{

    public UnnickCommand(SkyServerBase plugin) {
        super(plugin, Rank.MODERATOR);
    }
    @Override
    protected boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        if(playerData.isHidden()) {
            player.sendMessage("§cYou cannot unnick while hidden.");
            return false;
        }
        if(playerData.getNick() == null){
            player.sendMessage("§cYou are not nicked.");
            return false;
        }
        int priority = playerData.getNick().getNickRank().getPriority();
        new DatabaseMethods(this.getPlugin().getDatabaseConnectionManager()).unnick(player.getUniqueId());
        playerData.setNick(null);
        GameProfile profile = ((CraftPlayer) player).getHandle().getProfile();
        String textures = profile.getProperties().get("textures").iterator().next().getValue();
        String signature = profile.getProperties().get("textures").iterator().next().getSignature();
        List<Packet<?>> packets = PacketConstructor.renickPlayer(player, priority, player.getName(), playerData.getRank(), textures, signature);
        getPlugin().getServer().getOnlinePlayers().forEach((p) -> {
            if(p.equals(player)) return;
            PacketUtils.sendPacket(p, packets);
        });
        packets.remove(4);
        PacketUtils.sendPacket(player, packets);
        PacketUtils.sendPacket(player, PacketConstructor.fixPlayer(player));

        return true;
    }
}
