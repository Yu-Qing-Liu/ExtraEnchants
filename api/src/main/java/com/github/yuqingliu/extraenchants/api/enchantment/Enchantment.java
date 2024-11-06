package com.github.yuqingliu.extraenchants.api.enchantment;

import java.util.List;
import java.util.Set;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.item.Item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public interface Enchantment extends Listener {
    Component getName();

    Component getLeveledName(int level);

    TextColor getLevelColor(int level);

    TextColor getDescriptionColor();

    List<TextColor> getLeveledColors();

    int getMaxLevel();

    Component getDescription();

    Set<Item> getApplicable();

    String getRequiredLevelFormula();

    String getCostFormula();

    Component getLeveledDescription(int level);

    int getEnchantmentLevel(ItemStack item);

    void setName(Component name);

    void setDescription(Component description);

    void setApplicable(Set<Item> applicable);

    void setRequiredLevelFormula(String formula);

    void setCostFormula(String formula);

    void setMaxLevel(int level);

    void setLeveledColors(List<TextColor> colors);

    boolean canEnchant(ItemStack item);

    ItemStack applyEnchantment(ItemStack item, int level);

    ItemStack removeEnchantment(ItemStack item);
}
