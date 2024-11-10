package com.github.yuqingliu.extraenchants.api.managers;

import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.lore.LoreSection;

public interface LoreManager {
    public LoreSection getLoreSection(String sectionName, ItemStack item);
    public ItemStack applyLore(ItemStack item, Map<Integer, LoreSection> updates);
    public ItemStack applyLore(ItemStack item, LoreSection update);
}
