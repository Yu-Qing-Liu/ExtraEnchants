package com.github.yuqingliu.extraenchants.enchants.armor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.attribute.Attribute;

import com.github.yuqingliu.extraenchants.utils.UtilityMethods;

public class Growth implements Listener {
    private final JavaPlugin plugin;

    public Growth(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            // Delay the update to next tick to ensure the inventory change is processed
            plugin.getServer().getScheduler().runTask(plugin, () -> updatePlayerHealth((Player) event.getWhoClicked()));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayerHealth(event.getPlayer());
    }

    private void updatePlayerHealth(Player player) {
        double extraHearts = 0;
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null) {
                int level = UtilityMethods.getEnchantmentLevel(item, "Growth");
                extraHearts += level;
            }
        }

        // Calculate the total extra health to add
        double extraHealth = extraHearts;
        double baseHealth = 20.0; // Default maximum health without enchantments
        double newMaxHealth = baseHealth + extraHealth;

        // Use the Attribute system to update the player's max health
        if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);
        }
    }
}

