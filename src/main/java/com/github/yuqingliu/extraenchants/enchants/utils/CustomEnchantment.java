package com.github.yuqingliu.extraenchants.enchants.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.format.TextColor;

import org.bukkit.Material;

import java.util.List;
import java.util.Objects;

public class CustomEnchantment {
    private String name;
    private String identifier;
    private int maxLevel;
    private TextColor color;
    private List<Material> applicable;

    public CustomEnchantment(JavaPlugin plugin, String name, String identifier, int maxLevel, TextColor color, List<Material> applicable) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.applicable = applicable;
        this.color = color;
        this.identifier = identifier;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public TextColor getColor() {
        return color;
    }

    public boolean canEnchant(ItemStack item) {
        if(item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) return true;
        return applicable.contains(item.getType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomEnchantment that = (CustomEnchantment) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

