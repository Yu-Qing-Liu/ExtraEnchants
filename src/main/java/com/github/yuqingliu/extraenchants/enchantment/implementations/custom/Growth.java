package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.time.Duration;
import java.util.HashSet;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.Scheduler;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Growth extends CustomEnchantment {
    public Growth(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.GROWTH,
            Component.text("Growth", nameColor),
            Component.text("Adds 1 HP per level", descriptionColor),
            1,
            itemRepository.getItems().get(ItemCategory.ARMOR),
            new HashSet<>(),
            "x^2",
            "x"
        );
    }

    @Override
    public void postConstruct() {
        eventManager.registerEvent(this);
    }

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
                int level = this.getEnchantmentLevel(armor);
                extraHearts += level;
            }
        }
        double extraHealth = extraHearts;
        double baseHealth = 20.0;
        double newMaxHealth = baseHealth + extraHealth;
        if (player.getAttribute(Attribute.MAX_HEALTH) != null) {
            player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(newMaxHealth);
        }
    }
    
}
