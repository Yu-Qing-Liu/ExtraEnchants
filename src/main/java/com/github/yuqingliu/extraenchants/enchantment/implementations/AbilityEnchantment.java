package com.github.yuqingliu.extraenchants.enchantment.implementations;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import net.kyori.adventure.text.Component;

import java.util.List;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.yuqingliu.extraenchants.api.Keys;
import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.item.lore.Lore;
import com.github.yuqingliu.extraenchants.item.lore.implementations.AbilitySection;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

public class AbilityEnchantment extends AbstractEnchantment {
    private Component action;
    private NamespacedKey key = Keys.itemEnchant(TextUtils.componentToString(name));

    public AbilityEnchantment(Component action, Component name, int maxLevel, Component description, List<Material> applicable, List<Component> applicableDisplayNames, String levelFormula, String costFormula) {
        super(name, maxLevel, description, applicable, applicableDisplayNames, levelFormula, costFormula);
        this.action = action;
    }

    private ItemStack addOrUpdateAbilityLore(ItemStack item, Component enchant, Component eLevel) {
        Lore lore = new Lore(item);
        AbilitySection abilitySection = (AbilitySection) lore.getLoreSection("AbilitySection");
        abilitySection.addOrUpdateAbilityFromSection(enchant, eLevel, action, description);
        return lore.applyLore();
    }

    private ItemStack removeAbilityLore(ItemStack item, Component enchant) {
        Lore lore = new Lore(item);
        AbilitySection abilitySection = (AbilitySection) lore.getLoreSection("AbilitySection");
        abilitySection.removeAbilityFromSection(enchant);
        return lore.applyLore();
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
                item = addOrUpdateAbilityLore(item, name, eLevel);
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
