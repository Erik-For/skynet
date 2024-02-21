package se.skynet.skyserverbase.database;

import org.apache.commons.lang3.EnumUtils;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

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
                    "SELECT rank FROM player WHERE UUID = ?"
            );
            ps.setString(1, uuid.toString());
            ps.executeQuery();
            ResultSet resultSet = ps.getResultSet();
            if (!resultSet.next()) {
                return null;
            }
            String rankString = resultSet.getString("rank");
            if (EnumUtils.isValidEnum(Rank.class, rankString)) {
                Rank rank = Rank.valueOf(rankString);
                return new CustomPlayerData(rank);
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
}