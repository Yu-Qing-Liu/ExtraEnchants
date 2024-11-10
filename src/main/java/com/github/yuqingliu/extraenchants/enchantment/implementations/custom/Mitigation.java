package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.api.repositories.ItemRepository.ItemCategory;
import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Mitigation extends CustomEnchantment {
    public Mitigation(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, ItemRepository itemRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.MITIGATION,
            Component.text("Mitigation", nameColor),
            Component.text("1% chance to negate damage per level", descriptionColor),
            5,
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
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack[] armors = player.getInventory().getArmorContents();
            int totalMitigationLevels = 0;
            for (ItemStack armor : armors) {
                if (armor != null) {
                    int mitigationLevel = this.getEnchantmentLevel(armor);
                    totalMitigationLevels += mitigationLevel;
                }
            }
            double chanceToNegate = totalMitigationLevels * 0.01;
            if (Math.random() < chanceToNegate) {
                event.setCancelled(true);
                soundManager.playMitigationSound(player);
            }
        }
    }
}
