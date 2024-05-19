package com.github.yuqingliu.extraenchants.enchantment;

import com.github.yuqingliu.extraenchants.item.ItemUtils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@Getter
public class EnchantmentOffer {
    private ItemUtils itemUtils;
    private final Enchantment enchantment;
    private int level;
    private int requiredLevel;
    private int cost;

    public EnchantmentOffer(ItemUtils itemUtils, Enchantment enchantment, int level, int requiredLevel, int cost) {
        this.itemUtils = itemUtils;
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
        ItemStack finalItem = enchantment.applyEnchantment(item, finalLevel);
        if(itemUtils.getEnchantments(finalItem).isEmpty()) {
            if(finalItem.getType() == Material.ENCHANTED_BOOK) {
                return new ItemStack(Material.BOOK);
            } else {
                return new ItemStack(finalItem.getType());
            }
        }
        return finalItem;
    }
}
