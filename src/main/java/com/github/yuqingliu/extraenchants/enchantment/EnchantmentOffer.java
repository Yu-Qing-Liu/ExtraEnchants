package com.github.yuqingliu.extraenchants.enchantment;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.managers.SoundManager;
import com.google.inject.Inject;

import lombok.Getter;

@Getter
public class EnchantmentOffer {
    private final SoundManager soundManager;
    private final Enchantment enchantment;
    private int level;
    private int requiredLevel;
    private int cost;
    
    @Inject
    public EnchantmentOffer(SoundManager soundManager, Enchantment enchantment, int level, int requiredLevel, int cost) {
        this.soundManager = soundManager;
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
        soundManager.playEnchantmentSound(player);
        return enchantment.applyEnchantment(item, finalLevel);
    }
}
