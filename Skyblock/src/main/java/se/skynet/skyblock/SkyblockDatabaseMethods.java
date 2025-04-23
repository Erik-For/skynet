package se.skynet.skyblock;

import se.skynet.skyblock.playerdata.PlayerProfile;
import se.skynet.skyblock.playerdata.SkillType;
import se.skynet.skyblock.playerdata.SkillProgression;
import se.skynet.skyserverbase.database.DatabaseConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SkyblockDatabaseMethods {

    private final DatabaseConnectionManager databaseConnectionManager;

    public SkyblockDatabaseMethods(DatabaseConnectionManager databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
    }

    public void createProfileTable() {
        // Create the profile table
        String sql = "CREATE TABLE IF NOT EXISTS profiles" +
                "(uuid VARCHAR(36) PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        // Create PlayerProfile table
        String sqlPlayerProfile = "CREATE TABLE IF NOT EXISTS player_profiles" +
                "(uuid VARCHAR(36) PRIMARY KEY, " +
                "player_id VARCHAR(36), " +
                "profile_id VARCHAR(36), " +
                "FOREIGN KEY (player_id) REFERENCES players(uuid), " +
                "FOREIGN KEY (profile_id) REFERENCES profiles(uuid))";

        // Create Skills table
        String sqlSkillsTable = "CREATE TABLE IF NOT EXISTS skills" +
                "(skill_name VARCHAR(255), " +
                "level INT DEFAULT 0, " +
                "experience FLOAT DEFAULT 0.0, " +
                "player_profile_uuid VARCHAR(36), " +
                "FOREIGN KEY (player_profile_uuid) REFERENCES player_profiles(uuid), " +
                "UNIQUE (skill_name, player_profile_uuid))";

        // Create the tables
        PreparedStatement ps;
        try {
            ps = databaseConnectionManager.getConnection().prepareStatement(sql);
            ps.executeUpdate();
            ps.close();

            ps = databaseConnectionManager.getConnection().prepareStatement(sqlPlayerProfile);
            ps.executeUpdate();
            ps.close();

            ps = databaseConnectionManager.getConnection().prepareStatement(sqlSkillsTable);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createProfile(UUID player_uuid) {
        String createProfileSql = "INSERT INTO profiles (uuid, name) VALUES (?, ?)";
        String profileName = get_random_profile_name();
        String profileUuid = UUID.randomUUID().toString();

        try (PreparedStatement ps = this.databaseConnectionManager.getConnection().prepareStatement(createProfileSql)) {
            ps.setString(1, profileUuid);
            ps.setString(2, profileName);
            ps.executeUpdate(); // Ensure the profile is created first
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create profile in profiles table", e);
        }

        String createProfileMembership = "INSERT INTO player_profiles (uuid, player_id, profile_id) VALUES (?, ?, ?)";
        String playerProfileUuid = UUID.randomUUID().toString();

        try (PreparedStatement ps = this.databaseConnectionManager.getConnection().prepareStatement(createProfileMembership)) {
            ps.setString(1, playerProfileUuid);
            ps.setString(2, player_uuid.toString());
            ps.setString(3, profileUuid); // Use the profileUuid created above
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create profile membership in player_profiles table", e);
        }
    }

    public boolean hasProfile(UUID player_uuid) {
        String sql = "SELECT * FROM player_profiles WHERE player_id = ?";
        try {
            PreparedStatement ps = this.databaseConnectionManager.getConnection().prepareStatement(sql);
            ps.setString(1, player_uuid.toString());
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerProfile loadProfile(UUID player_uuid) {
        String sql = "SELECT * FROM player_profiles WHERE player_id = ?";
        try (PreparedStatement ps = this.databaseConnectionManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, player_uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String playerProfileUuid = rs.getString("uuid");
                    String skillSql = "SELECT * FROM skills WHERE player_profile_uuid = ?";
                    try (PreparedStatement ps2 = this.databaseConnectionManager.getConnection().prepareStatement(skillSql)) {
                        ps2.setString(1, playerProfileUuid);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            List<SkillProgression> skills = new ArrayList<>();
                            while (rs2.next()) {
                                String skillName = rs2.getString("skill_name");
                                int level = rs2.getInt("level");
                                float experience = rs2.getFloat("experience");
                                skills.add(new SkillProgression(SkillType.valueOf(skillName), experience, level));
                            }
                            return new PlayerProfile(UUID.fromString(playerProfileUuid), skills);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void saveProfileData(PlayerProfile playerProfile) {
        String sql = "INSERT INTO skills (skill_name, level, experience, player_profile_uuid) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE level = VALUES(level), experience = VALUES(experience)";
        playerProfile.getSkillsAsList().forEach(skill -> {
            try (PreparedStatement ps = this.databaseConnectionManager.getConnection().prepareStatement(sql)) {
                ps.setString(1, skill.getSkill().name());
                ps.setInt(2, skill.getLevel());
                ps.setFloat(3, skill.getExperience());
                ps.setString(4, playerProfile.getUuid().toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String get_random_profile_name() {
        List<String> firstWords = Arrays.asList(
                "Iron",
                "Crystal",
                "Shadow",
                "Echo",
                "Nova",
                "Pulse",
                "Storm",
                "Frost",
                "Rune",
                "Quantum",
                "Dark",
                "Ghost",
                "Flame",
                "Arc",
                "Phase",
                "Signal",
                "Cloud",
                "Dream",
                "Flux",
                "Cipher"
        );

        List<String> secondWords = Arrays.asList(
                "Core",
                "Node",
                "Spark",
                "Loop",
                "Drift",
                "Thread",
                "Dust",
                "Shell",
                "Tide",
                "Trace",
                "Shard",
                "Veil",
                "Root",
                "Glyph",
                "Bloom",
                "Scope",
                "Surge",
                "Fang",
                "Glow",
                "Burst"
        );

        // 2 random indexes
        int firstIndex = (int) (Math.random() * firstWords.size());
        int secondIndex = (int) (Math.random() * secondWords.size());
        return firstWords.get(firstIndex) + secondWords.get(secondIndex);
    }

}
