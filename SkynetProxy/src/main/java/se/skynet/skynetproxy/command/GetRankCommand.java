package se.skynet.skynetproxy.command;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.SkyProxy;

import java.util.Collections;

public class GetRankCommand extends SkynetCommand {


    public GetRankCommand(SkyProxy plugin){
        super("getrank", Rank.MODERATOR, plugin);
    }

    @Override
    public void executeCommand(ProxiedPlayer player, Rank rank, String[] args) {
        player.sendMessage(new ComponentBuilder().append("Your rank is: ").append(rank.getDisplayName()).color(rank.getRankColor()).create());
    }

    @Override
    public Iterable<String> tabComplete(ProxiedPlayer player, Rank rank, String[] args) {
        return Collections.emptyList();
    }
}
