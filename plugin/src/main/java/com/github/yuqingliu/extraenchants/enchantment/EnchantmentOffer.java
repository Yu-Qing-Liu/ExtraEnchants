package com.github.yuqingliu.extraenchants.enchantment;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@Getter
public class EnchantmentOffer {
    private final Enchantment enchantment;
    private int level;
    private int requiredLevel;
    private int cost;

    public EnchantmentOffer(Enchantment enchantment, int level, int requiredLevel, int cost) {
        this.enchantment = enchantment;
        this.level = level;
        this.requiredLevel = requiredLevel;
        this.cost = cost;
    }

    public ItemStack applyOffer(Player player, ItemStack item) {
        int prevLevel = enchantment.getEnchantmentLevel(item);
        int finalLevel = level;
        if(player.getLevel() < requiredLevel) {
            return item;
        } 
        if(prevLevel == level && prevLevel == enchantment.getMaxLevel()) {
            return item;
        }
        if(prevLevel == level) {
            finalLevel++;
        }
        player.setLevel(player.getLevel() - cost);
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
        return enchantment.applyEnchantment(item, finalLevel);
    }
}
