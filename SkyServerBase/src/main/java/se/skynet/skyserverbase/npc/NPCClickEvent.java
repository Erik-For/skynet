package se.skynet.skyserverbase.npc;

import org.bukkit.entity.Player;

public class NPCClickEvent {

    public enum Action {
        LEFT,
        RIGHT
    }
    private final Player player;
    private final NPC npc;
    private final Action mouse;

    public NPCClickEvent(Player player, NPC npc, Action mouse) {
        this.player = player;
        this.npc = npc;
        this.mouse = mouse;
    }

    public Action getAction() {
        return mouse;
    }

    public NPC getNpc() {
        return npc;
    }

    public Player getPlayer() {
        return player;
    }
}
