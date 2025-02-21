package se.skynet.skyserverbase.manager.headless;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.gui.GUI;
import se.skynet.skyserverbase.npc.NPC;
import se.skynet.skyserverbase.npc.NPCClick;
import se.skynet.skyserverbase.npc.NPCClickEvent;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

public class WorldConfigManager implements Listener {

    //private final SkyServerBase plugin;
    private Location spawn = null;

    public WorldConfigManager(SkyServerBase plugin) {
        //this.plugin = plugin;


        File config = null;
        try {
            config = Paths.get(plugin.getServer().getWorldContainer().getCanonicalPath(), "world", "config.yml").toFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!config.exists()) {
            return;
        }
        // parse yml config for reading
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(config);
        if (yaml.contains("spawn")){
            double x = yaml.getDouble("spawn.x");
            double y = yaml.getDouble("spawn.y");
            double z = yaml.getDouble("spawn.z");
            float yaw = (float) yaml.getDouble("spawn.yaw");
            float pitch = (float) yaml.getDouble("spawn.pitch");
            this.spawn = new Location(plugin.getServer().getWorlds().get(0), x, y, z, yaw, pitch);
            plugin.getServer().getWorlds().get(0).setSpawnLocation((int)x, (int)y, (int)z);
        }
        if(yaml.contains("npcs")){
            for (String npcs : yaml.getConfigurationSection("npcs").getKeys(false)) {
                ConfigurationSection npcConfig = yaml.getConfigurationSection("npcs." + npcs);
                String displayName = npcConfig.getString("displayname");

                double x = npcConfig.getDouble("location.x");
                double y = npcConfig.getDouble("location.y");
                double z = npcConfig.getDouble("location.z");
                float yaw = (float) npcConfig.getDouble("location.yaw");
                float pitch = (float) npcConfig.getDouble("location.pitch");

                String skinSignature = npcConfig.getString("skin.signature");
                String skinTexture = npcConfig.getString("skin.texture");
                NPCClick clickAction = new NPCClick() {
                    public void onClick(NPCClickEvent event) {
                        return;
                    }
                };

                if(npcConfig.contains("click")) {
                    String mouse = npcConfig.getString("click.mouse");
                    if(npcConfig.contains("click.command")){
                        String command = npcConfig.getString("click.command");
                        clickAction = new NPCClick() {
                            public void onClick(NPCClickEvent event) {
                                if (mouse.equalsIgnoreCase("any") || event.getAction().name().equalsIgnoreCase(mouse)) {
                                    plugin.getServer().dispatchCommand(event.getPlayer(), command);
                                }
                            }
                        };
                    } else if(npcConfig.contains("click.gui")) {
                        String gui = npcConfig.getString("click.gui");
                        clickAction = new NPCClick() {
                            public void onClick(NPCClickEvent event){
                                if (mouse.equalsIgnoreCase("any") || event.getAction().name().equalsIgnoreCase(mouse)) {
                                    Class<? extends GUI> gui1 = GUIManager.getGUI(gui);
                                    GUI gui2 = null;
                                    try {
                                        gui2 = gui1.getConstructor(SkyServerBase.class).newInstance(plugin);
                                        event.getPlayer().openInventory(gui2.getInventory());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                    }

                }
                NPC npc = new NPC(plugin, npcs, displayName, UUID.randomUUID(), skinTexture, skinSignature, new Location(plugin.getServer().getWorlds().get(0), x, y, z, yaw, pitch), clickAction);
                NpcManager.addNpc(npc);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(spawn == null){
            return;
        }
        Player player = event.getPlayer();
        player.teleport(spawn);
    }
}
