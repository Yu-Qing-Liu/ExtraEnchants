package com.github.yuqingliu.extraenchants.enchantment.implementations;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import net.kyori.adventure.text.Component;

import java.util.Set;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.yuqingliu.extraenchants.api.enums.CustomEnchant;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.managers.ColorManager;
import com.github.yuqingliu.extraenchants.api.managers.LoreManager;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.api.managers.TextManager;
import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.item.ItemImpl;
import com.github.yuqingliu.extraenchants.lore.implementations.AbilitySection;

public class AbilityEnchantment extends AbstractEnchantment {
    private Component action;
    private NamespacedKey key;

    public AbilityEnchantment(TextManager textManager, LoreManager loreManager, ColorManager colorManager, NameSpacedKeyManager keyManager, Component name, Component description, int maxLevel, Set<Item> applicable, String requiredLevelFormula, String costFormula, Component action, CustomEnchant enchantment) {
        super(textManager, loreManager, colorManager, keyManager, name, description, maxLevel, applicable, requiredLevelFormula, costFormula);
        this.action = action;
        this.key = keyManager.getEnchantKey(enchantment);
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
                item = addOrUpdateAbilityLore(item, getName(1), getLevel(level, 1));
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
