package se.skynet.skyserverbase.command;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import se.skynet.skyserverbase.Rank;
import se.skynet.skyserverbase.SkyServerBase;
import se.skynet.skyserverbase.playerdata.CustomPlayerData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigCreatorCommand extends Command {

    private HashMap<String, YamlConfiguration> configs = new HashMap<>();
    private HashMap<UUID, String> activeConfig = new HashMap<>();
    public ConfigCreatorCommand(SkyServerBase plugin) {
        super(plugin, Rank.ADMIN);
    }

    // command that is used to create block location configs
    // /configcreator select <configname>
    // /configcreator add <key> takes the player pos and adds it to the config at key
    // the xyz coordinates are the location of the player
    // /configcreator remove <key>
    // /configcreator save <configname>
    //

    @Override
    protected boolean executeCommand(Player player, CustomPlayerData playerData, Command command, String s, String[] strings) {
        if(strings.length == 0){
            player.sendMessage("§cUsage: /configcreator <select | add | remove> <configname | key>");
            return false;
        }
        if(strings[0].equalsIgnoreCase("select")){
            if(strings.length != 2){
                player.sendMessage("§cUsage: /configcreator select <configname>");
                return false;
            }
            if(configs.containsKey(strings[1])){
                activeConfig.put(player.getUniqueId(), strings[1]);
                player.sendMessage("§aSelected config: " + strings[1]);
                return true;
            } else {
                YamlConfiguration config = new YamlConfiguration();
                configs.put(strings[1], config);
                activeConfig.put(player.getUniqueId(), strings[1]);
                player.sendMessage("§aCreated config: " + strings[1]);
                return true;
            }
        } else if(strings[0].equalsIgnoreCase("add")){
            if(strings.length != 2){
                player.sendMessage("§cUsage: /configcreator add <key>");
                return false;
            }
            if(!activeConfig.containsKey(player.getUniqueId())){
                player.sendMessage("§cYou need to select a config first");
                return false;
            }
            YamlConfiguration config = configs.get(activeConfig.get(player.getUniqueId()));
            Map<String, Integer> coords = new HashMap<>();
            coords.put("x", player.getLocation().getBlockX());
            coords.put("y", player.getLocation().getBlockY());
            coords.put("z", player.getLocation().getBlockZ());

            List<Map<?, ?>> mapList = config.getMapList(strings[1]);
            mapList.add(coords);
            config.set(strings[1], mapList);
            player.sendMessage("§aAdded location to config");
            return true;
        } else if(strings[0].equalsIgnoreCase("remove")){
            if(strings.length != 2){
                player.sendMessage("§cUsage: /configcreator remove <key>");
                return false;
            }
            if(!activeConfig.containsKey(player.getUniqueId())){
                player.sendMessage("§cYou need to select a config first");
                return false;
            }
            YamlConfiguration config = configs.get(activeConfig.get(player.getUniqueId()));
            config.set(strings[1], null);
            player.sendMessage("§aRemoved location from config");
            return true;
        } else if(strings[0].equalsIgnoreCase("save")){
            if(!activeConfig.containsKey(player.getUniqueId())){
                player.sendMessage("§cYou need to select a config first");
                return false;
            }
            String yamlString = configs.get(activeConfig.get(player.getUniqueId())).saveToString();

            try {
                URL url = new URL("https://pastebin.com/api/api_post.php");
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;

                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setDoInput(true);
                Map<String,String> arguments = new HashMap<>();

                arguments.put("api_dev_key", "AyS5KTJ7psr07j9xEDrgAeRsmhApUaUe");
                arguments.put("api_paste_code", yamlString);
                arguments.put("api_option", "paste");
                arguments.put("api_paste_format", "yaml");
                arguments.put("api_paste_expire_date", "10M");

                StringJoiner sj = new StringJoiner("&");
                for(Map.Entry<String,String> entry : arguments.entrySet())
                    sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                            + URLEncoder.encode(entry.getValue(), "UTF-8"));
                byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
                int length = out.length;
                http.setFixedLengthStreamingMode(length);
                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                http.connect();
                OutputStream os = http.getOutputStream();
                os.write(out);
                InputStream is = http.getInputStream();
                String text = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                player.sendMessage("pastebin url: " + text);
            } catch (Exception e){
                e.printStackTrace();
                player.sendMessage(ChatColor.RED + "Failed to upload to pastebin");
            }

        }
        return false;
    }

}
