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

    @NotNull TextColor getDescriptionColor();

    @NotNull List<TextColor> getLeveledColors();

    @NotNull int getMaxLevel();

    @NotNull Component getDescription();

    @NotNull List<Material> getApplicable();

    @NotNull List<Component> getApplicableDisplayNames();

    @NotNull String getRequiredLevelFormula();

    @NotNull String getCostFormula();

    @NotNull Component getLeveledDescription(int level);

    @NotNull int getEnchantmentLevel(ItemStack item);
    
    void setName(Component name);

    void setDescription(Component description);

    void setApplicable(List<Material> applicable);

    void setApplicableDisplayNames(List<Component> names);

    void setRequiredLevelFormula(String formula);

    void setCostFormula(String formula);

    void setMaxLevel(int level);

    void setLeveledColors(List<TextColor> colors);

    @NotNull boolean canEnchant(ItemStack item);

    @NotNull ItemStack applyEnchantment(ItemStack item, int level);

    @NotNull ItemStack removeEnchantment(ItemStack item);
}
