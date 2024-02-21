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
                    "CREATE TABLE IF NOT EXISTS player " +
                            "(UUID VARCHAR(36) PRIMARY KEY, " +
                            "username VARCHAR(16), " +
                            "rank ENUM('DEFAULT', 'MVP', 'MODERATOR', 'ADMIN', 'MANAGEMENT') DEFAULT 'DEFAULT'," +
                            "playtime INT DEFAULT 0," +
                            "first_join DATETIME DEFAULT NOW()," +
                            "last_seen DATETIME DEFAULT NOW()," +
                            "banned BOOLEAN DEFAULT false " +
                            ")"
            );
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("FATAL: Could not create the player table");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public boolean isBanned(UUID uuid){
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "SELECT banned FROM player WHERE UUID = ?"
            );
            ps.setString(1, uuid.toString());
            ps.executeQuery();
            ResultSet resultSet = ps.getResultSet();
            if(!resultSet.next()){
                return false;
            }
            return resultSet.getBoolean("banned");
        } catch (SQLException e) {
            System.out.println("FATAL: Could not get the player data");
            System.exit(1);
        }
        return false;
    }

    public void setBanned(UUID uuid, boolean banned){
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "UPDATE player SET banned = ? WHERE UUID = ?"
            );
            ps.setBoolean(1, banned);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("FATAL: Could not get the player data");
            System.exit(1);
        }
    }
    public void createPlayer(UUID uuid, String username) {
        // make a method that inserts a new player into the database
        PreparedStatement ps;
        try {
            ps = this.databaseManager.getConnection().prepareStatement(
                    "INSERT IGNORE INTO player (UUID, username) VALUES (?, ?)"
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
                    "SELECT * FROM player WHERE UUID = ?"
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
}