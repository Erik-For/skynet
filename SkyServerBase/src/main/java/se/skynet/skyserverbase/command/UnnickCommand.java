package se.skynet.skyserverbase.command;

import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.database.DatabaseMethods;
import se.skynet.skyserverbase.packet.PacketConstructor;
import se.skynet.skyserverbase.packet.PacketUtils;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

public class UnnickCommand extends Command{

    public UnnickCommand(SkyServerBase plugin) {
        super(plugin, Rank.MODERATOR);
    }
    @Override
    protected boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        new DatabaseMethods(this.getPlugin().getDatabaseConnectionManager()).unnick(player.getUniqueId());
        playerData.setNick(null);

        PacketUtils.sendPacketAll(PacketConstructor.renickPlayer(player, player.getName(), playerData.getRank().getPrefix(), ""), this.getPlugin());
        return true;
    }
}
