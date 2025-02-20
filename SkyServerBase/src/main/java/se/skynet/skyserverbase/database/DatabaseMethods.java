package se.skynet.skyserverbase.database;

import org.apache.commons.lang3.EnumUtils;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;
import se.skynet.skyserverbase.playerdata.Nick;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseMethods {

    private final DatabaseConnectionManager databaseManager;

    public DatabaseMethods(DatabaseConnectionManager databaseManager) {
        this.databaseManager = databaseManager;
    }
    public CustomPlayerData getPlayerData(UUID uuid) {
        // make a method that gets the player data from the database
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "SELECT rank, nickname, nicked_rank, nick_signature, nick_texture FROM player WHERE UUID = ?"
            );
            ps.setString(1, uuid.toString());
            ps.executeQuery();
            ResultSet resultSet = ps.getResultSet();
            if (!resultSet.next()) {
                return null;
            }
            String rankString = resultSet.getString("rank");
            String nickString = resultSet.getString("nickname");
            String nickRankString = resultSet.getString("nicked_rank");
            String signature = resultSet.getString("nick_signature");
            String texture = resultSet.getString("nick_texture");


            if (EnumUtils.isValidEnum(Rank.class, rankString)) {
                Rank rank = Rank.valueOf(rankString);
                CustomPlayerData customPlayerData = new CustomPlayerData(rank);
                if(nickString != null && nickRankString != null){
                    customPlayerData.setNick(new Nick(nickString, Rank.valueOf(nickRankString), signature, texture));
                }
                return customPlayerData;
            } else {
                System.out.println("FATAL: Could not get the player data");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.out.println("FATAL: Could not get the player data");
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public void setNick(UUID uuid, Nick nick) {
        // make a method that sets the nickname for a player
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "UPDATE player SET nickname = ?, nicked_rank = ?, nick_signature = ?, nick_texture = ? WHERE UUID = ?"
            );
            ps.setString(1, nick.getNickname());
            ps.setString(2, nick.getNickRank().name());
            ps.setString(3, nick.getSignature());
            ps.setString(4, nick.getTexture());
            ps.setString(5, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("FATAL: Could not set the nickname");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void unnick(UUID uuid) {
        // make a method that removes the nickname for a player
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "UPDATE player SET nickname = NULL, nicked_rank = NULL, nick_signature = NULL, nick_texture = NULL WHERE UUID = ?"
            );
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("FATAL: Could not unset the nickname");
            e.printStackTrace();
            System.exit(1);
        }
    }
}