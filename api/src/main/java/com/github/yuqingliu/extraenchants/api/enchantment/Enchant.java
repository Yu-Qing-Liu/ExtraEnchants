package com.github.yuqingliu.extraenchants.api.enchantment;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public interface Enchant {
    @NotNull Component getName();

    @NotNull Component getLeveledName(int level);

    @NotNull TextColor getLevelColor(int level);

    @NotNull public TextColor getDescriptionColor();

    @NotNull int getMaxLevel();

    @NotNull void setMaxLevel(int level);

    @NotNull Component getDescription();

    @NotNull List<Material> getApplicable();

    @NotNull List<Component> getApplicableDisplayNames();

    @NotNull String getRequiredLevelFormula();

    @NotNull String getCostFormula();

    @NotNull public Component getLeveledDescription(int level);

    @NotNull abstract int getEnchantmentLevel(ItemStack item);

    @NotNull abstract boolean canEnchant(ItemStack item);

    @NotNull abstract ItemStack applyEnchantment(ItemStack item, int level);

    @NotNull abstract ItemStack removeEnchantment(ItemStack item);
}
