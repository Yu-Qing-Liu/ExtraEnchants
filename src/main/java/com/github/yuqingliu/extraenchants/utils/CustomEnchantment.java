package com.github.yuqingliu.extraenchants.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;

import java.util.List;

public class CustomEnchantment {
    private String name;
    private int maxLevel;
    private List<Material> applicable;

    public CustomEnchantment(JavaPlugin plugin, String name, int maxLevel, List<Material> applicable) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.applicable = applicable;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public String getName() {
        return name;
    }

    public boolean canEnchant(ItemStack item) {
        if(item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) return true;
        return applicable.contains(item.getType());
    }
}

