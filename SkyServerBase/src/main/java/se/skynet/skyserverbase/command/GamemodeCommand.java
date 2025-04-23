package se.skynet.skyserverbase.command;

import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

public class GamemodeCommand extends Command {


    private final SkyServerBase plugin;
    public GamemodeCommand(SkyServerBase plugin) {
        super(plugin, Rank.MODERATOR);
        this.plugin = plugin;
    }
    @Override
    protected boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            player.sendMessage("Please specify a gamemode.");
            return false;
        }
        String gamemode = strings[0];
        switch (gamemode.toLowerCase()) {
            case "0":
            case "s":
            case "survival":
                player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                break;
            case "1":
            case "c":
            case "creative":
                player.setGameMode(org.bukkit.GameMode.CREATIVE);
                break;
            case "2":
            case "a":
            case "adventure":
                player.setGameMode(org.bukkit.GameMode.ADVENTURE);
                break;
            case "3":
            case "sp":
            case "spectator":
                player.setGameMode(org.bukkit.GameMode.SPECTATOR);
                break;
            default:
                player.sendMessage("Invalid gamemode. Please use survival, creative, adventure, or spectator.");
                return false;
        }
        player.sendMessage("Gamemode set to " + gamemode + ".");
        return true;
    }
}
