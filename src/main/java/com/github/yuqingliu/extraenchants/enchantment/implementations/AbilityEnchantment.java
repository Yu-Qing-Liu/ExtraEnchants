package com.github.yuqingliu.extraenchants.enchantment.implementations;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import net.kyori.adventure.text.Component;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.item.ItemImpl;
import com.github.yuqingliu.extraenchants.lore.implementations.AbilitySection;

public abstract class AbilityEnchantment extends AbstractEnchantment {
    private Component action;
    private NamespacedKey key;

    public AbilityEnchantment(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, EnchantID id, Component name, Component description, int maxLevel, Set<Item> applicable, Set<EnchantID> conflicting, String requiredLevelFormula, String costFormula, Component action, Duration cooldown) {
        super(managerRepository, enchantmentRepository, id, name, description, maxLevel, applicable, conflicting, requiredLevelFormula, costFormula);
        this.action = action;
        super.cooldown = cooldown;
        this.key = keyManager.getEnchantKey(id);
    }

    private ItemStack addOrUpdateAbilityLore(ItemStack item, Component enchant, Component eLevel) {
        AbilitySection abilitySection = (AbilitySection) loreManager.getLoreSection(AbilitySection.class.getSimpleName(), item);
        abilitySection.addOrUpdateAbilityFromSection(enchant, eLevel, action, description);
        return loreManager.applyLore(item, abilitySection);
    }

    private ItemStack removeAbilityLore(ItemStack item, Component enchant) {
        AbilitySection abilitySection = (AbilitySection) loreManager.getLoreSection(AbilitySection.class.getSimpleName(), item);
        abilitySection.removeAbilityFromSection(enchant);
        return loreManager.applyLore(item, abilitySection);
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
                item = addOrUpdateAbilityLore(item, getName(level), getLevel(level));
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
            item = removeAbilityLore(item, name);
        }
        return item;
    }
}
