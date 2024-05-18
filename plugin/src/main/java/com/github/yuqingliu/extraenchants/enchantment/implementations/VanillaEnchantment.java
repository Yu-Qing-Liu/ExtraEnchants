package com.github.yuqingliu.extraenchants.enchantment.implementations;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import net.kyori.adventure.text.Component;

import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;

public class VanillaEnchantment extends AbstractEnchantment {
    private Enchantment enchantment;  
    
    public VanillaEnchantment(Enchantment enchantment, Component name, int maxLevel, Component description, List<Material> applicable, List<Component> applicableDisplayNames, String levelFormula, String costFormula) {
        super(name, maxLevel, description, applicable, applicableDisplayNames, levelFormula, costFormula);
        this.enchantment = enchantment;
    }

    @Override
    public int getEnchantmentLevel(ItemStack item) {
        return item.getEnchantmentLevel(enchantment);
    }
    
    @Override
    public boolean canEnchant(ItemStack item) {
        Component displayName = item.displayName();
        return enchantment.canEnchantItem(item) || applicable.contains(item.getType()) || applicableDisplayNames.contains(displayName);
    }
    
    @Override
    public ItemStack applyEnchantment(ItemStack item, int level) {
        if(level <= maxLevel) {
            if(item.getType() == Material.BOOK) {
                item = new ItemStack(Material.ENCHANTED_BOOK);
            }
            if(item.getItemMeta() instanceof EnchantmentStorageMeta) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                meta.addStoredEnchant(enchantment, level, true);
                item.setItemMeta(meta);
                return item;
            }
            item.addUnsafeEnchantment(enchantment, level);
        }
        return item;
    }
    
    @Override
    public ItemStack removeEnchantment(ItemStack item) {
        if(item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            meta.removeEnchant(enchantment);
            item.setItemMeta(meta);
            return item;
        }
        item.removeEnchantment(enchantment);
        return item;
    }
}
