package com.github.yuqingliu.extraenchants.enchantment.implementations;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import net.kyori.adventure.text.Component;

import java.util.List;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.yuqingliu.extraenchants.Keys;
import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

public class CustomEnchantment extends AbstractEnchantment {
    private NamespacedKey key = Keys.itemEnchant(TextUtils.componentToString(name));

    public CustomEnchantment(Component name, int maxLevel, Component description, List<Material> applicable, List<Component> applicableDisplayNames, String levelFormula, String costFormula) {
        super(name, maxLevel, description, applicable, applicableDisplayNames, levelFormula, costFormula);
    }
    
    @Override
    public int getEnchantmentLevel(ItemStack item) {
        if(item == null || item.getType() == Material.AIR) {
            return 0;
        }
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(key, PersistentDataType.INTEGER)) {
            return container.get(key, PersistentDataType.INTEGER);
        }
        return 0;
    }
    
    @Override
    public boolean canEnchant(ItemStack item) {
        if(item == null || item.getType() == Material.AIR) {
            return false;
        }
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
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(key, PersistentDataType.INTEGER, level);
            item.setItemMeta(meta);
            meta = item.getItemMeta();
            if (meta != null) {
                Component eLevel = Component.text(" " + TextUtils.toRoman(level), name.color());
                item = addOrUpdateEnchantmentLore(item, name, eLevel);
            }
        }
        return item;
    }
    
    @Override
    public ItemStack removeEnchantment(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(container.has(key, PersistentDataType.INTEGER)) {
            container.remove(key);
        }
        item.setItemMeta(meta);
        meta = item.getItemMeta();
        if (meta != null) {
            item = removeEnchantmentLore(item, name);
        }
        return item;
    }
}
