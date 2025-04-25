package se.skynet.skyserverbase.command;

import com.google.common.collect.Lists;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

import java.util.List;
import java.util.stream.Collectors;

public class ExecuteAsCommand extends Command {

    private SkyServerBase plugin;

    public ExecuteAsCommand(SkyServerBase plugin) {
        super(plugin, Rank.ADMIN);
        this.plugin = plugin;
    }
    @Override
    protected boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] args) {
        if(args.length < 2) {
            player.sendMessage("§cPlease use /executeas <player> <command>");
            return true;
        }

        String targetPlayerName = args[0];
        if(targetPlayerName.equalsIgnoreCase(player.getName())) {
            player.sendMessage("§cYou cannot execute commands as yourself.");
            return true;
        }
        if(player.getServer().getPlayer(targetPlayerName) == null) {
            player.sendMessage("§cPlayer " + targetPlayerName + " is not online.");
            return true;
        }
        Player targetPlayer = player.getServer().getPlayer(targetPlayerName);


        String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, commandArgs, 0, args.length - 1);

        String commandToExecute = Strings.join(commandArgs, " ");

        targetPlayer.performCommand(commandToExecute);

        return true;

    }

    @Override
    protected List<String> tabComplete(Player player, CustomPlayerData playerData, Command command, String s, String[] args) {
        if(args.length == 1) {
            return plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> !p.getName().equalsIgnoreCase(player.getName()))
                    .filter(p -> p.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
