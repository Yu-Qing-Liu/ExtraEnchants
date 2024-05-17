package com.github.yuqingliu.extraenchants.enchantment.implementations;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class CustomEnchantment extends AbstractEnchantment {
    private TextUtils textUtils = new TextUtils();

    public CustomEnchantment(Component name, int maxLevel, Component description, List<Material> applicable, List<Component> applicableDisplayNames, String levelFormula, String costFormula) {
        super(name, maxLevel, description, applicable, applicableDisplayNames, levelFormula, costFormula);
    }

    public int getEnchantmentLevel(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        int level = 0;
        if(nbtItem != null && nbtItem.hasTag("extra-enchants."+name)) {
            level = nbtItem.getInteger("extra-enchants."+name);
        }
        return level;
    }

    public boolean canEnchant(ItemStack item) {
        Component displayName = item.displayName();
        if(item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) {
            return true;
        } 
        return applicable.contains(item.getType()) && applicableDisplayNames.contains(displayName);
    }

    public ItemStack applyEnchantment(ItemStack item, int level) {
        if(level <= maxLevel) {
            if(item.getType() == Material.BOOK) {
                item = new ItemStack(Material.ENCHANTED_BOOK);
            }
            ItemMeta meta = item.getItemMeta();
            if(meta != null) {
                List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
                List<Component> newLore = new ArrayList<>();
                boolean found = false;
                for (int i = 0; i < existingLore.size(); i++) {
                    Component current = existingLore.get(i);
                    if(!found && current.contains(name)) {
                        i++; // Skip description
                        found = true;
                    } else {
                        newLore.add(current);
                    }
                }
                existingLore = newLore;
                Component enchantName = name.append(Component.text(" " + textUtils.toRoman(level), name.color()));
                existingLore.add(enchantName);
                existingLore.add(description);
                meta.lore(existingLore);
                item.setItemMeta(meta);
                NBTItem nbtItem = new NBTItem(item);
                nbtItem.setInteger("extra-enchants."+name, level);
                return nbtItem.getItem();
            }
        }
        return item;
    }

    public ItemStack removeEnchantment(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
        List<Component> newLore = new ArrayList<>();
        boolean found = false;
        for (int i = 0; i < existingLore.size(); i++) {
            Component current = existingLore.get(i);
            if(!found && current.contains(name)) {
                i++; // Skip description
                found = true;
            } else {
                newLore.add(current);
            }
        }
        existingLore = newLore;
        meta.lore(existingLore);
        item.setItemMeta(meta);
        NBTItem nbtItem = new NBTItem(item);
        if(nbtItem.hasTag("extra-enchants."+name)) {
            nbtItem.removeKey("extra-enchants."+name);
        }
        return nbtItem.getItem();
    }
}
