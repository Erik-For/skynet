package se.skynet.skynetproxy.party;

import com.google.gson.annotations.Expose;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import se.skynet.skynetproxy.SkyProxy;
import se.skynet.skynetproxy.playerdata.CustomPlayerData;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PartyInvite {
    @Expose(serialize = true, deserialize = false)
    private final UUID id;
    private final SkyProxy plugin;
    private final Party party;
    @Expose(serialize = true, deserialize = false)
    private final ProxiedPlayer invited;
    @Expose(serialize = true, deserialize = false)
    private final ProxiedPlayer inviter;
    private final ScheduledTask task;
    private long time;

    public PartyInvite(Party party, ProxiedPlayer invited, ProxiedPlayer inviter, SkyProxy plugin) {
        this.id = UUID.randomUUID();
        this.plugin = plugin;
        this.party = party;
        this.invited = invited;
        this.inviter = inviter;
        this.time = System.currentTimeMillis();
        this.task = plugin.getProxy().getScheduler().schedule(plugin, this::onTimeout, 60, TimeUnit.SECONDS);
    }

    private void onTimeout() {
        if (inviter != null && invited != null) {
            CustomPlayerData inviterData = plugin.getPlayerDataManager().getPlayerData(inviter.getUniqueId());
            PartyChatFormatting.sendMessage(invited, PartyChatFormatting.formatInviteTimeout(inviter, inviterData));
            plugin.getPartyManager().removeInvite(invited.getUniqueId());
        }
    }

    public UUID getId() {
        return id;
    }

    public Optional<Party> acceptInvite(ProxiedPlayer player) {
        if (player != invited) {
            return Optional.empty();
        }
        if (System.currentTimeMillis() - time > 60 * 1000) {
            return null;
        } else {
            task.cancel();
            this.time = 0;
            party.getPartyManager().removeInvite(id);
            return Optional.of(party);
        }
    }
//
//    public ProxiedPlayer getInviter() {
//        return inviter;
//    }
//
//    public ProxiedPlayer getInvited() {
//        return invited;
//    }
//
//    public void revokeDisband() {
//        task.cancel();
//        if (inviter != null) {
//            CustomPlayerData inviterData = plugin.getPlayerDataManager().getPlayerData(inviter.getUniqueId());
//            PartyChatFormatting.sendMessage(inviter, PartyChatFormatting.formatInviteExpirePartyDisband(inviter, inviterData));
//        }
//    }
//
//    public void revokeInviterLeft(ProxiedPlayer player) {
//        task.cancel();
//        CustomPlayerData inviterData = plugin.getPlayerDataManager().getPlayerData(inviter.getUniqueId());
//        PartyChatFormatting.sendMessage(inviter, PartyChatFormatting.formatInviterLeft(inviter, inviterData));
//    }
//
//    protected Party getParty() {
//        return party;
//    }
}