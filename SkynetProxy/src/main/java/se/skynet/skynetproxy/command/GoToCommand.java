package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;

public class GoToCommand extends SkynetCommand {
    private SkyProxy plugin;
    public GoToCommand(SkyProxy plugin) {
        super("goto", Rank.ADMIN, plugin);
        this.plugin = plugin;
    }

    @Override
    public void executeCommand(ProxiedPlayer player, Rank rank, String[] args) {
        if(args.length < 1) {
            player.sendMessage("Usage: /goto <username>");
            return;
        }

    }

    @Override
    public Iterable<String> tabComplete(ProxiedPlayer player, Rank rank, String[] args) {
        return null;
    }
}
