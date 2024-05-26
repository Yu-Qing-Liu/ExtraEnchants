package com.github.yuqingliu.extraenchants.enchants.armor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;

import com.github.yuqingliu.extraenchants.api.Scheduler;

import lombok.RequiredArgsConstructor;

import java.time.Duration;

import org.bukkit.attribute.Attribute;

@RequiredArgsConstructor
public class Growth implements Listener {
    private final Enchantment enchant;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Scheduler.runLater(task -> updatePlayerHealth((Player) event.getWhoClicked()), Duration.ofMillis(50));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayerHealth(event.getPlayer());
    }

    private void updatePlayerHealth(Player player) {
        double extraHearts = 0;
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null) {
                int level = enchant.getEnchantmentLevel(armor);
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

