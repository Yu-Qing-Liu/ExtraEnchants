package com.github.yuqingliu.extraenchants.enchantment;

import lombok.Getter;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Enchantment {
    @Getter private final AbstractEnchantment definition;

    public Enchantment(AbstractEnchantment enchantment) {
        this.definition = enchantment;
    }

    public Component getName() {
        return definition.getName();
    }

    public TextColor getNameColor() {
        return definition.getNameColor();
    }

    public TextColor getDescriptionColor() {
        return definition.getDescriptionColor();
    }

    public Component getDescription() {
        return definition.getDescription();
    }

    public Component getLeveledDescription(int level) {
        return definition.getLeveledDescription(level);
    }

    public int getMaxLevel() {
        return definition.getMaxLevel();
    }

    public List<Material> getApplicableItems() {
        return definition.getApplicable();
    }

    public List<Component> getApplicableDisplayNames() {
        return definition.getApplicableDisplayNames();
    }

    public String getRequiredLevelFormula() {
        return definition.getRequiredLevelFormula();
    }

    public String getCostFormula() {
        return definition.getCostFormula();
    }

    public int getEnchantmentLevel(ItemStack item) {
        return definition.getEnchantmentLevel(item);
    }

    public boolean canEnchant(ItemStack item) {
        return definition.canEnchant(item);
    }

    public ItemStack applyEnchantment(ItemStack item, int level) {
        return definition.applyEnchantment(item, level);
    }

    public ItemStack removeEnchantment(ItemStack item) {
        return definition.removeEnchantment(item);
    }
}
