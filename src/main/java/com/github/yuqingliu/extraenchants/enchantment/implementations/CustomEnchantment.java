package com.github.yuqingliu.extraenchants.enchantment.implementations;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import net.kyori.adventure.text.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.item.ItemImpl;

public abstract class CustomEnchantment extends AbstractEnchantment {
    private NamespacedKey key;

    public CustomEnchantment(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, EnchantID id, Component name, Component description, int maxLevel, Set<Item> applicable, Set<EnchantID> conflicting, String requiredLevelFormula, String costFormula) {
        super(managerRepository, enchantmentRepository, id, name, description, maxLevel, applicable, conflicting, requiredLevelFormula, costFormula);
        this.key = keyManager.getEnchantKey(id);
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
        if(item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) {
            return true;
        }
        Item i = new ItemImpl(item);
        Set<EnchantID> keySet = enchantmentRepository.getEnchantments(item).keySet().stream().map(enchant -> enchant.getId()).collect(Collectors.toSet());
        if (keySet.retainAll(conflicting) && keySet.size() > 0) {
            return false;
        }
        return applicable.contains(i);
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
                item = addOrUpdateEnchantmentLore(item, enchantmentRepository.getSortedEnchantments(item, this, level));
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
            Map<Enchantment, Integer> enchants = enchantmentRepository.getSortedEnchantments(item);
            enchants.remove(this);
            item = addOrUpdateEnchantmentLore(item, enchants);
        }
        return item;
    }
}
