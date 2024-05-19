package com.github.yuqingliu.extraenchants.enchantment.implementations;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import net.kyori.adventure.text.Component;

import java.util.List;

import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.item.lore.Lore;
import com.github.yuqingliu.extraenchants.item.lore.implementations.AbilitySection;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class AbilityEnchantment extends AbstractEnchantment {
    private Component action;

    public AbilityEnchantment(Component action, Component name, int maxLevel, Component description, List<Material> applicable, List<Component> applicableDisplayNames, String levelFormula, String costFormula) {
        super(name, maxLevel, description, applicable, applicableDisplayNames, levelFormula, costFormula);
        this.action = action;
    }

    private ItemStack addOrUpdateAbilityLore(ItemStack item, Component enchant, Component eLevel) {
        Lore lore = new Lore(item);
        AbilitySection abilitySection = (AbilitySection) lore.getLoreSection("AbilitySection").getDefinition();
        abilitySection.addOrUpdateAbilityFromSection(enchant, eLevel, action, description);
        return lore.applyLore();
    }

    private ItemStack removeAbilityLore(ItemStack item, Component enchant) {
        Lore lore = new Lore(item);
        AbilitySection abilitySection = (AbilitySection) lore.getLoreSection("AbilitySection").getDefinition();
        abilitySection.removeAbilityFromSection(enchant);
        return lore.applyLore();
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
                item = addOrUpdateAbilityLore(item, name, eLevel);
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
            item = removeAbilityLore(item, name);
        }
        return item;
    }
}
