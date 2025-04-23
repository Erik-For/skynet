package se.skynet.skyblock;

import org.bukkit.entity.Player;
import se.skynet.skyblock.playerdata.PlayerProfile;
import se.skynet.skyblock.playerdata.Profile;

public class SkyblockPlayer {

    private Player player;

    private PlayerProfile profile;

    public SkyblockPlayer(Player player, PlayerProfile profile) {
        this.player = player;
        this.profile = profile;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

}
