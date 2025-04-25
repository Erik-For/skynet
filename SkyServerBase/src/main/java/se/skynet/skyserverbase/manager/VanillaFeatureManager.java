package se.skynet.skyserverbase.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import se.skynet.skyserverbase.SkyServerBase;

public class VanillaFeatureManager implements Listener {

    private final SkyServerBase plugin;

    public VanillaFeatureManager(SkyServerBase plugin) {
        this.plugin = plugin;
    }

    private boolean hungerEnabled = false;
    public void setHungerEnabled(boolean hungerEnabled) {
        this.hungerEnabled = hungerEnabled;
    }

    @EventHandler
    public void playerHungerEvent(FoodLevelChangeEvent event) {
        if (!hungerEnabled && event.getEntity() instanceof Player) {
            event.setCancelled(true);
            Player player = (Player) event.getEntity();
            player.setFoodLevel(20);
            player.setSaturation(20);
        }
    }

    private boolean weatherChangeEnabled = false;

    public void setWeatherChangeEnabled(boolean weatherChangeEnabled) {
        this.weatherChangeEnabled = weatherChangeEnabled;
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!weatherChangeEnabled) {
            event.setCancelled(true);
        }
    }

    private boolean dayNightCycleEnabled = false;

    public void setDayNightCycleEnabled(boolean dayNightCycleEnabled) {
        this.dayNightCycleEnabled = dayNightCycleEnabled;
    }

    @EventHandler
    public void onDayNightCycleChange(WeatherChangeEvent event) {
        if (!dayNightCycleEnabled) {
            event.setCancelled(true);
        }
    }

    private boolean pvpEnabled = false;

    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    @EventHandler
    public void onPvPChange(EntityDamageByEntityEvent event) {
        if (!pvpEnabled && event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }
}
