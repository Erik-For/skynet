package se.skynet.skywars;

import org.bukkit.entity.Player;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;
import se.skynet.skywars.perks.SkywarsPerk;

import java.util.HashMap;

public class SkywarsPlayer {

    private String formatedName;
    private Integer kills = 0;
    private Player player;
    private boolean alive;
    private Tag latestTag;
    private final String name;

    private final HashMap<SkywarsPerk, Integer> perks;

    public SkywarsPlayer(Player player, Game game) {
        this.alive = game.getGameState() == GameState.WAITING;
        CustomPlayerData data = game.getPlugin().getParentPlugin().getPlayerDataManager().getPlayerData(player.getUniqueId());
        this.formatedName = data.getRank().getRankColor() + player.getName();
        this.player = player;
        this.name = player.getName();
        this.latestTag = new Tag(null, null, 0);
        this.perks = new HashMap<>();
    }

    public String getFormattedName() {
        return formatedName;
    }

    public Integer getKills() {
        return kills;
    }
    public void addKill() {
        kills++;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setLatestTag(Tag tag) {
        this.latestTag = tag;
    }

    public Tag getLatestTag() {
        return latestTag;
    }

    public String getName() {
        return name;
    }
}
