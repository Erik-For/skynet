package se.skynet.skynetproxy.database;

import org.apache.commons.lang3.EnumUtils;
import se.skynet.skynetproxy.Rank;
import se.skynet.skynetproxy.playerdata.CustomPlayerData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseMethods {

    private final DatabaseConnectionManager databaseManager;

    public DatabaseMethods(DatabaseConnectionManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void createPlayerTable() {
        // make a create table method that makes a player table with a UUID ,username, rank, and playtime, first_join, last_seen columns
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS players " +
                            "(uuid VARCHAR(36) PRIMARY KEY, " +
                            "username VARCHAR(16), " +
                            "rank ENUM('DEFAULT', 'MVP', 'MODERATOR', 'ADMIN', 'MANAGEMENT', 'WEAK_ADMIN') DEFAULT 'DEFAULT'," +
                            "playtime INT DEFAULT 0," +
                            "first_join DATETIME DEFAULT NOW()," +
                            "last_seen DATETIME DEFAULT NOW()," +
                            "banned BOOLEAN DEFAULT false," +
                            "nickname VARCHAR(16) DEFAULT NULL," +
                            "nicked_rank ENUM('DEFAULT', 'MVP', 'MODERATOR', 'ADMIN', 'MANAGEMENT', 'WEAK_ADMIN') DEFAULT NULL," +
                            "nick_texture TEXT DEFAULT NULL," +
                            "nick_signature TEXT DEFAULT NULL" +
                            ")"
            );
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("FATAL: Could not create the players table");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public boolean isBanned(UUID uuid){
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "SELECT banned FROM players WHERE uuid = ?"
            );
            ps.setString(1, uuid.toString());
            ps.executeQuery();
            ResultSet resultSet = ps.getResultSet();
            if(!resultSet.next()){
                return false;
            }
            return resultSet.getBoolean("banned");
        } catch (SQLException e) {
            System.out.println("FATAL: Could not get the players data");
            System.exit(1);
        }
        return false;
    }

    public void setBanned(UUID uuid, boolean banned){
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "UPDATE players SET banned = ? WHERE uuid = ?"
            );
            ps.setBoolean(1, banned);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("FATAL: Could not get the player data");
            System.exit(1);
        }
    }

    public UUID getUUID(String username) {
        // make a method that gets the UUID from the database
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "SELECT uuid FROM players WHERE username = ?"
            );
            ps.setString(1, username);
            ps.executeQuery();
            ResultSet resultSet = ps.getResultSet();
            if(!resultSet.next()){
                return null;
            }
            return UUID.fromString(resultSet.getString("UUID"));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FATAL: Could not get the player data");
            System.exit(1);
        }
        return null;
    }
    public void createPlayer(UUID uuid, String username) {
        // make a method that inserts a new player into the database
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "INSERT IGNORE INTO players (uuid, username) VALUES (?, ?)"
            );
            ps.setString(1, uuid.toString());
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("FATAL: Could not create the player");
            System.exit(1);
        }
    }
    public CustomPlayerData getPlayerRank(UUID uuid) {
        // make a method that gets the player data from the database
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "SELECT * FROM players WHERE uuid = ?"
            );
            ps.setString(1, uuid.toString());
            ps.executeQuery();
            ResultSet resultSet = ps.getResultSet();
            if(!resultSet.next()){
                return null;
            }
            String rankString = resultSet.getString("rank");
            if (EnumUtils.isValidEnum(Rank.class, rankString)) {
                return new CustomPlayerData(Rank.valueOf(rankString));
            } else {
                System.out.println("FATAL: Could not get the player data");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.out.println("FATAL: Could not get the player data");
            System.exit(1);
        }
        return null;
    }

    public void setRank(UUID uniqueId, Rank targetRank) {
        // make a method that sets the rank of a player in the database
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "UPDATE players SET rank = ? WHERE uuid = ?"
            );
            ps.setString(1, targetRank.toString());
            ps.setString(2, uniqueId.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("FATAL: Could not set the player rank");
            System.exit(1);
        }
    }
}