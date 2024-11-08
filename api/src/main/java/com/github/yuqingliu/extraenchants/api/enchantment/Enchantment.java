package com.github.yuqingliu.extraenchants.api.enchantment;

import java.util.List;
import java.util.Set;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public interface Enchantment extends Listener {
    EnchantID getId();
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
    Set<EnchantID> getConflicting();
    void setName(Component name);
    void setDescription(Component description);
    void setApplicable(Set<Item> applicable);
    void setConflicting(Set<EnchantID> conflicting);
    void setRequiredLevelFormula(String formula);
    void setCostFormula(String formula);
    void setMaxLevel(int level);
    void setLeveledColors(List<TextColor> colors);
    boolean canEnchant(ItemStack item);
    boolean conflictsWith(EnchantID id);
    ItemStack applyEnchantment(ItemStack item, int level);
    ItemStack removeEnchantment(ItemStack item);
    void postConstruct();
}
