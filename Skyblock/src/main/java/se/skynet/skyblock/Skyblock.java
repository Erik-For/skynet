package se.skynet.skyblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import se.skynet.skyblock.commands.EquipArmorCommand;
import se.skynet.skyblock.commands.ItemCommand;
import se.skynet.skyblock.commands.SkyblockCommand;
import se.skynet.skyblock.guis.npc.BankerMenu;
import se.skynet.skyblock.managers.*;
import se.skynet.skyblock.mobs.SkyblockMobEventHandler;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.manager.headless.NpcManager;
import se.skynet.skyserverbase.npc.NPC;
import se.skynet.skyserverbase.npc.NPCClick;
import se.skynet.skyserverbase.npc.NPCClickEvent;

import java.util.UUID;

import static jdk.internal.icu.text.UCharacterIterator.getInstance;

public final class Skyblock extends JavaPlugin {

    private final SkyServerBase parentPlugin = SkyServerBase.getPlugin(SkyServerBase.class);
    private SkyblockPlayerManager playerManager;
    private SkyblockDatabaseMethods databaseMethods;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new SkyblockMobEventHandler(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockManager(), this);
        this.getServer().getPluginManager().registerEvents(new SkyblockMenuItemManager(this), this);
        this.getServer().getPluginManager().registerEvents( new SkyblockItemManager(this), this);
        this.getServer().getPluginManager().registerEvents(new ScoreboardManager(this), this);
        this.getServer().getPluginManager().registerEvents(new SpawnManagerTemp(this), this);
        this.playerManager = new SkyblockPlayerManager(this);
        this.getServer().getPluginManager().registerEvents(this.playerManager, this);
        SkyblockCommand.registerCommand(this.getCommand("skyblock"), new SkyblockCommand(this));
        SkyblockCommand.registerCommand(this.getCommand("item"), new ItemCommand(this));
        SkyblockCommand.registerCommand(this.getCommand("armor"), new EquipArmorCommand(this));


        this.databaseMethods = new SkyblockDatabaseMethods(this.getParentPlugin().getDatabaseConnectionManager());
        this.databaseMethods.createProfileTable();

        this.getParentPlugin().getVanillaFeatureManager().setDayNightCycleEnabled(true);

        this.getServer().getWorlds().get(0).getEntities().forEach(Entity::remove);

        NpcManager.addNpc(new NPC(getParentPlugin(), "banker", "Banker", UUID.randomUUID(), "ewogICJ0aW1lc3RhbXAiIDogMTc0NTk3MDc0MDAzMiwKICAicHJvZmlsZUlkIiA6ICI2Njg5MDJmYjI1YTY0NDBhODBmM2Y2MjZhYTk0MzBmYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiYW5rZXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA5ZDllNjljMzhiNzQ1MmEyNmFhN2I3ZDhjNTI0NjIwOWMwMDZjYjMzMmVhODg4ZjJlM2EzYmI1Mjc1NjQ1OCIKICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTdkZmVhMTZkYzgzYzk3ZGYwMWExMmZhYmJkMTIxNjM1OWMwY2QwZWE0MmY5OTk5YjZlOTdjNTg0OTYzZTk4MCIKICAgIH0KICB9Cn0=", "wpGInRHGcJVyU+mr9V2nV/IYQBkUShJP+ZHYSmTwryIASbavnOeliIN0v7JuKqY3ALUYg5wOa4re5qp0CNHw2+r8rfq2zv5jSSMmYt1l2/JpOlru9jp8UzHRLUZIYbXepwrEASjo26uRCYd5zLi58kxi8tdpXSBhWPvijbV4jGDBqug/vQjA91QL7BQvfvZrMSfd/VglAslMVadvVd7RAPOpwZZnrV27H8beQPZRVy1kHoqvQhRjiEKXPSkbrM64mn7Ykd4p5WoXU2Gd9s9SbXFpTw0ZmJLwFqPbI4gABl5AiiBncjVjtrbURvsbShdwnHk2wmrzurL7wXvnBxFpXDqNrHxh4D7jfWcU9RjRdZsoDpd3tGF4K5NzwmKwl9ckHOVJk3ngXPBkly+8rzrpgbfaaUp2mjO7bgbxU/rtbEWGJhpwscfuGZ8PgV2fdXtbrRNtscg5ADuTRbkJ9y1k3pORhq2eqjiHfu4bavGG9BRRMywX1Hnvas4WK19yO7vL7MoPC5SIQOBt3Fm40kJF4/JoDlGFllpMGqOv03FGkxkZJ4oIFh5onndqe5OtNmb4jkke/0hFgW3rHDHQZGBX/+Qdy9J363N7UsvCIpsZyJGUnARH4o3Pylndm59ch9ubagXOrFofx9m/p3lJMp8W1KWaRDiWG9obs5QDXNesmlQ=", new Location(this.getServer().getWorlds().get(0), -24.5, 71, -58.5, 180, 0), new NPCClick() {
            @Override
            public void onClick(NPCClickEvent npcClickEvent) {
                System.out.println(npcClickEvent.getPlayer());
                SkyblockPlayer player = getInstance().getPlayerManager().getSkyblockPlayer(npcClickEvent.getPlayer());
                player.getPlayer().openInventory(new BankerMenu(getInstance(), player).getInventory());
            }
        }));
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public SkyServerBase getParentPlugin() {
        return parentPlugin;
    }

    public SkyblockPlayerManager getPlayerManager() {
        return playerManager;
    }

    public SkyblockDatabaseMethods getDatabaseMethods() {
        return databaseMethods;
    }
    public static Skyblock getInstance() {
        return getPlugin(Skyblock.class);
    }
}
