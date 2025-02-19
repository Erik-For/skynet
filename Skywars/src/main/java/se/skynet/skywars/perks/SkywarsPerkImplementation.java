package se.skynet.skywars.perks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SkywarsPerkImplementation {

    public void onPlayerKill(Player killer, Player killed);
    public void onPlayerDeath(Player player);

    public void onPickup(Player player, ItemStack item);
    public void onDrop(Player player, ItemStack item);
    public void onBlockBreak(Player player, Block block);
    public void onBlockPlace(Player player, Block block);

    public void onGameStart(Player player);
    public void onGameEnd(Player player);

}
