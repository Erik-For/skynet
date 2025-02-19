package se.skynet.skyserverbase.playerdata;

import org.bukkit.permissions.PermissionAttachment;
import se.skynet.skyserverbase.Rank;

public class CustomPlayerData {

    private final Rank rank;
    private Nick nick;

    private PermissionAttachment permissonAttachment;

    public CustomPlayerData(Rank rank) {
        this.rank = rank;
    }

    public void setNick(Nick nick) {
        this.nick = nick;
    }

    public Nick getNick() {
        return nick;
    }

    public boolean hasNick(){
        return nick != null;
    }

    public Rank getRank() {
        return rank;
    }

    protected void setPermissonAttachment(PermissionAttachment permissonAttachment) {
        this.permissonAttachment = permissonAttachment;
    }
    public PermissionAttachment getPermissonAttachment() {
        return permissonAttachment;
    }

}
