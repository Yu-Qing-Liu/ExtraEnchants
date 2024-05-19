package com.github.yuqingliu.extraenchants.enchantment.implementations;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;
import java.util.ArrayDeque;
import java.util.ArrayList;

import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class CustomEnchantment extends AbstractEnchantment {

    public CustomEnchantment(Component name, int maxLevel, Component description, List<Material> applicable, List<Component> applicableDisplayNames, String levelFormula, String costFormula) {
        super(name, maxLevel, description, applicable, applicableDisplayNames, levelFormula, costFormula);
    }
    
    @Override
    public int getEnchantmentLevel(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        int level = 0;
        if(nbtItem != null && nbtItem.hasTag("extra-enchants." + name)) {
            level = nbtItem.getInteger("extra-enchants." + name);
        }
        return level;
    }
    
    @Override
    public boolean canEnchant(ItemStack item) {
        Component displayName = item.displayName();
        if(item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) {
            return true;
        } 
        return applicable.contains(item.getType()) || applicableDisplayNames.contains(displayName);
    }
    
    @Override
    public ItemStack applyEnchantment(ItemStack item, int level) {
        if (level <= maxLevel) {
            if (item.getType() == Material.BOOK) {
                item = new ItemStack(Material.ENCHANTED_BOOK);
            }
            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setInteger("extra-enchants." + name, level);
            item = nbtItem.getItem();
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                Component eLevel = Component.text(" " + TextUtils.toRoman(level), name.color());
                List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
                existingLore = addOrUpdateEnchantmentFromSection(existingLore, name, eLevel);
                meta.lore(existingLore);
                item.setItemMeta(meta);
            }
        }
        return item;
    }
    
    @Override
    public ItemStack removeEnchantment(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasTag("extra-enchants." + name)) {
            nbtItem.removeKey("extra-enchants." + name);
        }
        item = nbtItem.getItem();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
            existingLore = removeEnchantmentFromSection(existingLore, name);
            meta.lore(existingLore);
            item.setItemMeta(meta);
        }
        return item;
    }
}
