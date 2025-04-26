package se.skynet.skyblock.managers;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import se.skynet.skyblock.Skyblock;
import se.skynet.skyblock.SkyblockPlayer;
import se.skynet.skyblock.items.*;
import se.skynet.skyblock.items.items.Ability;
import se.skynet.skyblock.playerdata.Stat;
import se.skynet.skyserverbase.util.NBTHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyblockItemManager implements Listener {

    private final Skyblock plugin;
    private final HashMap<SkyblockItemType, SkyblockItemEvents> items = new HashMap<>();
    private static final HashMap<UUID, Map<Ability, AbilityCooldown>> itemAbilityCooldownMap = new HashMap<>();

    public SkyblockItemManager(Skyblock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void itemPickupEvent(PlayerPickupItemEvent event) {
        SkyblockPlayer skyblockPlayer = plugin.getPlayerManager().getSkyblockPlayer(event.getPlayer());
        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();

        if (!SkyblockItem.isSkyblockItem(itemStack)) {
            ItemStack render = new VanillaItem(itemStack.getType(), "", itemStack.getAmount(), itemStack.getMaxStackSize())
                    .render(skyblockPlayer);
            event.getPlayer().getInventory().addItem(render);
            item.remove();
            event.setCancelled(true);
            return;
        }

        String typeKey = NBTHelper.getString(itemStack, "s_type", "VANILLA");
        SkyblockItemType type = typeKey.equals("VANILLA") ? SkyblockItemType.VANILLA : SkyblockItemType.valueOf(typeKey);
        if (type == SkyblockItemType.VANILLA) return;

        SkyblockItem skyblockItem = SkyblockItem.constructSkyblockItem(type.getItemClass(), itemStack);
        event.setCancelled(true);
        item.remove();
        event.getPlayer().getInventory().addItem(skyblockItem.render(skyblockPlayer));

        if (skyblockItem instanceof SkyblockItemEvents) {
            // Future usage: SkyblockItemEvents itemEvent = (SkyblockItemEvents) skyblockItem;
        }
    }

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        if (event.getItem() == null || event.getItem().getType() == Material.AIR) return;

        SkyblockPlayer skyblockPlayer = plugin.getPlayerManager().getSkyblockPlayer(event.getPlayer());
        if (!SkyblockItem.isSkyblockItem(event.getItem()) || skyblockPlayer == null) return;

        String typeKey = NBTHelper.getString(event.getItem(), "s_type", "VANILLA");
        SkyblockItemType type = typeKey.equals("VANILLA") ? SkyblockItemType.VANILLA : SkyblockItemType.valueOf(typeKey);
        if (type == SkyblockItemType.VANILLA) return;

        SkyblockItem skyblockItem = SkyblockItem.constructSkyblockItem(type.getItemClass(), event.getItem());

        for (ItemAbility ability : skyblockItem.getAbilities()) {
            boolean matches = ability.getAbility().getAbilityAction()
                    == AbilityAction.fromBukkitAction(event.getAction(), event.getPlayer().isSneaking());
            if (!matches) continue;

            UUID id = skyblockItem.getId();
            AbilityCooldown cooldown = itemAbilityCooldownMap
                    .getOrDefault(id, new HashMap<>())
                    .getOrDefault(ability.getAbility(), new AbilityCooldown(ability.getAbility()));

            if (cooldown.isOnCooldown()) {
                event.getPlayer().sendMessage("§cThis ability is on cooldown! for another " + cooldown.getSecondsLeft() + " seconds");
                return;
            }

            int manacost = ability.getAbility().getManacost();
            if (manacost > 0 && skyblockPlayer.getStat(Stat.INTELLIGENCE) < manacost) {
                event.getPlayer().sendMessage("§cYou do not have enough mana to use this ability!");
                return;
            }

            if (manacost > 0) {
                skyblockPlayer.removeFromStat(Stat.INTELLIGENCE, manacost);
            }

            if (ability.getImplementation().onUse(ability.getAbility().getAbilityAction(), ability.getAbility(),
                    skyblockItem, skyblockPlayer, event.getItem())) {
                cooldown.useAbility();
            }

            itemAbilityCooldownMap.computeIfAbsent(id, k -> new HashMap<>()).put(ability.getAbility(), cooldown);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if (!SkyblockItem.isSkyblockItem(itemStack)) return;

        String typeKey = NBTHelper.getString(itemStack, "s_type", "VANILLA");
        SkyblockItemType type = typeKey.equals("VANILLA") ? SkyblockItemType.VANILLA : SkyblockItemType.valueOf(typeKey);
        if (type == SkyblockItemType.VANILLA) return;

        SkyblockItem skyblockItem = SkyblockItem.constructSkyblockItem(type.getItemClass(), itemStack);
        if (skyblockItem instanceof SkyblockItemEvents) {
            ((SkyblockItemEvents) skyblockItem).onItemDrop(skyblockItem, event);
        }
    }

    @EventHandler
    public void onItemDurability(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }
}
