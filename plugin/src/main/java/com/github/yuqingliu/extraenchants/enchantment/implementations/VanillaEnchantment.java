package com.github.yuqingliu.extraenchants.enchantment.implementations;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class VanillaEnchantment extends AbstractEnchantment {
    private Enchantment enchantment;  
    
    public VanillaEnchantment(Enchantment enchantment, Component name, int maxLevel, Component description, List<Material> applicable, List<Component> applicableDisplayNames, String levelFormula, String costFormula) {
        super(name, maxLevel, description, applicable, applicableDisplayNames, levelFormula, costFormula);
        this.enchantment = enchantment;
    }

    @Override
    public int getEnchantmentLevel(ItemStack item) {
        if(item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            return meta.getStoredEnchantLevel(enchantment);
        }
        return item.getEnchantmentLevel(enchantment);
    }
    
    @Override
    public boolean canEnchant(ItemStack item) {
        if(item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) {
            return true;
        } 
        Component displayName = item.displayName();
        return enchantment.canEnchantItem(item) || applicable.contains(item.getType()) || applicableDisplayNames.contains(displayName);
    }
    
    @Override
    public ItemStack applyEnchantment(ItemStack item, int level) {
        if(level <= maxLevel) {
            if(item.getType() == Material.BOOK) {
                item = new ItemStack(Material.ENCHANTED_BOOK);
            }
            Component eLevel = Component.text(" " + TextUtils.toRoman(level), name.color());
            item = addOrUpdateEnchantmentLore(item, name, eLevel);
            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setInteger("extra-enchants." + name, level);
            item = nbtItem.getItem();
            if(item.getItemMeta() instanceof EnchantmentStorageMeta) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                meta.addStoredEnchant(enchantment, level, true);
                meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
                item.setItemMeta(meta);
                return item;
            }
            item.addUnsafeEnchantment(enchantment, level);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);
            }
        }
        return item;
    }
    
    @Override
    public ItemStack removeEnchantment(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        if(nbtItem.hasTag("extra-enchants." + name)) {
            nbtItem.removeKey("extra-enchants." + name);
        }
        item = nbtItem.getItem();
        item = removeEnchantmentLore(item, name);
        if(item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            meta.removeEnchant(enchantment);
            item.setItemMeta(meta);
            return item;
        }
        item.removeEnchantment(enchantment);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            item.setItemMeta(meta);
        }
        return item;
    }
}
