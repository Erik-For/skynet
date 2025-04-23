package se.skynet.skyserverbase.manager.headless;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import se.skynet.skyserverbase.SkyServerBase;

public class DisableFeatureManager implements Listener {

    private final SkyServerBase plugin;
    public DisableFeatureManager(SkyServerBase plugin) {
        this.plugin = plugin;
        // Disable all features that are not needed in the lobby

    }

    // Disable weather change
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }
}
