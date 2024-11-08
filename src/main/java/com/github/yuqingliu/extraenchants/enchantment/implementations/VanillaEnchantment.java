package com.github.yuqingliu.extraenchants.enchantment.implementations;

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import net.kyori.adventure.text.Component;

import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.item.ItemImpl;

public class VanillaEnchantment extends AbstractEnchantment {
    private Enchantment enchantment;
    private NamespacedKey key;

    public VanillaEnchantment(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, EnchantID id, Component name, Component description, int maxLevel, Set<Item> applicable, Set<EnchantID> conflicting, String requiredLevelFormula, String costFormula, Enchantment enchantment) {
        super(managerRepository, enchantmentRepository, id, name, description, maxLevel, applicable, conflicting, requiredLevelFormula, costFormula);
        this.enchantment = enchantment;
        this.key = keyManager.getEnchantKey(id);
    }

    @Override
    public int getEnchantmentLevel(ItemStack item) {
        if(item == null || item.getType() == Material.AIR) {
            return 0;
        }
        if(item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            return meta.getStoredEnchantLevel(enchantment);
        }
        return item.getEnchantmentLevel(enchantment);
    }
    
    @Override
    public boolean canEnchant(ItemStack item) {
        if(item == null || item.getType() == Material.AIR) {
            return false;
        }
        if(item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) {
            return true;
        }
        Item i = new ItemImpl(item);
        Set<EnchantID> keySet = enchantmentRepository.getEnchantments(item).keySet().stream().map(enchant -> enchant.getId()).collect(Collectors.toSet());
        if (keySet.retainAll(conflicting) && keySet.size() > 0) {
            return false;
        }
        return enchantment.canEnchantItem(item) || applicable.contains(i);
    }
    
    @Override
    public ItemStack applyEnchantment(ItemStack item, int level) {
        if(level <= maxLevel) {
            if(item.getType() == Material.BOOK) {
                item = new ItemStack(Material.ENCHANTED_BOOK);
            }
            item = addOrUpdateEnchantmentLore(item, getName(level), getLevel(level));
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(key, PersistentDataType.INTEGER, level);
            item.setItemMeta(meta);
            if(item.getItemMeta() instanceof EnchantmentStorageMeta) {
                EnchantmentStorageMeta smeta = (EnchantmentStorageMeta) item.getItemMeta();
                smeta.addStoredEnchant(enchantment, level, true);
                smeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
                item.setItemMeta(smeta);
                return item;
            }
            item.addUnsafeEnchantment(enchantment, level);
            meta = item.getItemMeta();
            if (meta != null) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);
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
        item = removeEnchantmentLore(item, name);
        if(item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta smeta = (EnchantmentStorageMeta) item.getItemMeta();
            smeta.removeEnchant(enchantment);
            item.setItemMeta(smeta);
            return item;
        }
        item.removeEnchantment(enchantment);
        return item;
    }
}
