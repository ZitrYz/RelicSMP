package events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import RelicSmp.Main;
import RelicSmp.PersistentDataValues;
import RelicSmp.RelicPassiveManager;
import RelicSmp.UsefullFunctions;
import net.md_5.bungee.api.ChatColor;
import relics.Relic;
import relics.RelicTypes;

public class EquipRelicHandlers implements Listener {

    private static final NamespacedKey IS_RELIC_KEY = new NamespacedKey(Main.plugin, PersistentDataValues.IS_RELIC.name());
    private static final NamespacedKey RELIC_TYPE_KEY = new NamespacedKey(Main.plugin, PersistentDataValues.RELIC_TYPE.name());

    public static final Map<Player, List<Relic>> relics_equipped = Main.relics_equipped;
	
    public EquipRelicHandlers() {
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);

        Bukkit.getScheduler().runTaskTimer(Main.plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                syncEquippedRelics(player);
            }
        }, 0L, 100L); 
    }


    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!UsefullFunctions.checkIfCanUse()) return;

        Player player = (Player) event.getEntity();
        ItemStack item = event.getItem().getItemStack();

        if (!isRelicItem(item)) return;

        Relic relic = getRelicFromItem(item);
        if (relic == null) return;

        List<Relic> equipped = relics_equipped.getOrDefault(player, new ArrayList<>());

        if (equipped.contains(relic) || equipped.size() >= 2) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You already have this relic or reached the relic limit!");

            ItemStack clone = item.clone();
            event.getItem().remove();
            player.getWorld().dropItemNaturally(player.getLocation(), clone);
            return;
        }

        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> syncEquippedRelics(player), 1L);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!UsefullFunctions.checkIfCanUse()) return;
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> syncEquippedRelics(event.getPlayer()), 1L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> syncEquippedRelics((Player) event.getWhoClicked()), 1L);
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> syncEquippedRelics(event.getPlayer()), 1L);
    }

    @EventHandler
    public void onHotbarChange(PlayerItemHeldEvent event) {
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> syncEquippedRelics(event.getPlayer()), 1L);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/give") || event.getMessage().startsWith("/i ")) {
            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> syncEquippedRelics(event.getPlayer()), 1L);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        syncEquippedRelics(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> syncEquippedRelics(event.getPlayer()), 10L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        relics_equipped.remove(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        relics_equipped.remove(event.getEntity());
        for (Relic relic : Relic.values()) {
            RelicPassiveManager.removePassive(event.getEntity(), relic);
        }
    }


    private void syncEquippedRelics(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        Set<Relic> found = new HashSet<>();
        List<Relic> valid = new ArrayList<>();

        List<Relic> previouslyEquipped = relics_equipped.getOrDefault(player, new ArrayList<>());
        Set<Relic> previouslyEquippedSet = new HashSet<>(previouslyEquipped);

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (!isRelicItem(item)) continue;

            Relic relic = getRelicFromItem(item);
            if (relic == null) continue;

            if (found.contains(relic) || valid.size() >= 2) {
                player.getWorld().dropItemNaturally(player.getLocation(), item.clone());
                contents[i] = null;
            } else {
                found.add(relic);
                valid.add(relic);
            }
        }

        player.getInventory().setContents(contents);

        // Fire unequip events
        for (Relic relic : previouslyEquippedSet) {
            if (!valid.contains(relic)) {
                Bukkit.getPluginManager().callEvent(new RelicUnequipEvent(player, relic));
            }
        }

        // Fire equip events
        for (Relic relic : valid) {
            if (!previouslyEquippedSet.contains(relic)) {
                Bukkit.getPluginManager().callEvent(new RelicEquipEvent(player, relic));
            }
        }

        if (valid.isEmpty()) {
            relics_equipped.remove(player);
        } else {
            relics_equipped.put(player, valid);
        }
    }

    private boolean isRelicItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.has(IS_RELIC_KEY, PersistentDataType.BOOLEAN)
                && Boolean.TRUE.equals(container.get(IS_RELIC_KEY, PersistentDataType.BOOLEAN));
    }

    private Relic getRelicFromItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String type = container.get(RELIC_TYPE_KEY, PersistentDataType.STRING);
        if (type == null) return null;
        try {
            return RelicTypes.valueOf(type).getRelic();
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}

