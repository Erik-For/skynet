package se.skynet.skyserverbase.command;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.database.DatabaseMethods;
import se.skynet.skyserverbase.packet.PacketConstructor;
import se.skynet.skyserverbase.packet.PacketUtils;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;
import se.skynet.skyserverbase.playerdata.Nick;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NickCommand extends Command {

    public NickCommand(SkyServerBase plugin) {
        super(plugin, Rank.MODERATOR);
    }


    @Override
    protected boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            player.sendMessage("§cUsage: /nick <nickname> <rank (optional)> <skin owner (optional)>");
            return false;
        }
        String nick = strings[0];
        int priority = 0;
        if(playerData.getNick() != null) {
            priority = playerData.getNick().getNickRank().getPriority();
        } else {
            priority = playerData.getRank().getPriority();
        }
        String signature = null;
        String texture = null;
        Rank nickRank = playerData.getRank();
        if (nick.length() > 16) {
            player.sendMessage("§cNickname is too long.");
            return false;
        }
        if(strings.length > 1) {
            try {
                nickRank = Rank.valueOf(strings[1]);
            } catch (IllegalArgumentException e) {
                player.sendMessage("§cInvalid rank.");
                return false;
            }
        }
        UUID uuid;
        if(strings.length > 2) {
            uuid = getUUID(strings[2]);
        } else {
            uuid = player.getUniqueId();
        }
        if (uuid == null) {
            player.sendMessage("§cCould not find player with that name.");
            return false;
        }
        Tuple<String, String> skin = getSkin(uuid);
        if (skin == null) {
            player.sendMessage("§cCould not find skin for that player.");
            return false;
        }
        signature = skin.a();
        texture = skin.b();

        player.sendMessage("§aNickname set to " + nick + (nickRank != null ? " with rank " + nickRank : "") + (signature != null ? " with skin from " + strings[2] : ""));
        playerData.setNick(new Nick(nick, nickRank, signature, texture));
        new DatabaseMethods(getPlugin().getDatabaseConnectionManager()).setNick(player.getUniqueId(), new Nick(nick, nickRank, signature, texture));
        List<Packet<?>> packets = PacketConstructor.renickPlayer(player, priority, nick, nickRank, signature, texture);
        getPlugin().getServer().getOnlinePlayers().forEach((p) -> {
            if(p.equals(player)) return;
            PacketUtils.sendPacket(p, packets);
        });
        packets.remove(4);
        PacketUtils.sendPacket(player, packets);
        PacketUtils.sendPacket(player, PacketConstructor.fixPlayer(player));
        return true;
    }

    private Tuple<String, String> getSkin(UUID uuid) {
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString() + "?unsigned=false");
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.lines().collect(Collectors.joining());
            reader.close();
            JSONObject json = new JSONObject(line);
            JSONObject properties = json.getJSONArray("properties").getJSONObject(0);
            return new Tuple<>(properties.getString("signature"), properties.getString("value"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private UUID getUUID(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.lines().collect(Collectors.joining());
            reader.close();
            JSONObject json = new JSONObject(line);
            return fromTrimmed(json.getString("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException{
        if(trimmedUUID == null) throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        /* Backwards adding to avoid index adjustments */
        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
        } catch (StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException();
        }

        return UUID.fromString(builder.toString());
    }
}
