package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.server.Server;
import se.skynet.skynetproxy.server.ServerType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LobbyCommand extends SkynetCommand {

    private final SkyProxy plugin;
    public LobbyCommand(SkyProxy plugin) {
        super("lobby", Rank.DEFAULT, plugin, Arrays.asList("hub", "l"));
        this.plugin = plugin;
    }

    @Override
    public void executeCommand(ProxiedPlayer player, Rank rank, String[] args) {
        List<Server> servers = plugin.getServerManager().getServers(ServerType.LOBBY);
        Random rand = new Random();
        ServerInfo randomLobby = servers.get(rand.nextInt(servers.size())).getBungeeCordServerObject(plugin);
        player.connect(randomLobby);
    }

    @Override
    public Iterable<String> tabComplete(ProxiedPlayer player, Rank rank, String[] args) {
        return Collections.emptyList();
    }
}
