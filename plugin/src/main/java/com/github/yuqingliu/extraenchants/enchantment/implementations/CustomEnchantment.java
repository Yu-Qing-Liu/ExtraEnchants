package com.github.yuqingliu.extraenchants.enchantment.implementations;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;
import java.util.regex.Pattern;
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
        if(nbtItem != null && nbtItem.hasTag("extra-enchants."+name)) {
            level = nbtItem.getInteger("extra-enchants."+name);
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
                Component enchantmentSection;
                if (existingLore.isEmpty()) {
                    enchantmentSection = Component.empty();
                    existingLore.add(enchantmentSection);
                } else {
                    enchantmentSection = existingLore.get(0);
                }
                enchantmentSection = addOrUpdateEnchantmentFromSection(enchantmentSection, name, eLevel);
                existingLore.set(0, enchantmentSection);
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
            Component enchantName = name.append(Component.text(" " + TextUtils.toRoman(getEnchantmentLevel(item)), name.color()));
            List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();

            if (!existingLore.isEmpty()) {
                Component enchantmentSection = existingLore.get(0);
                enchantmentSection = removeEnchantmentFromSection(enchantmentSection, enchantName);
                existingLore.set(0, enchantmentSection);
            }

            meta.lore(existingLore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private Component addOrUpdateEnchantmentFromSection(Component enchantmentSection, Component enchant, Component eLevel) {
        if (enchantmentSection == null || enchantmentSection.equals(Component.empty())) {
            return enchant.append(eLevel);
        }
        Component newComponent = Component.empty();
        boolean found = false;
        for (Component child : enchantmentSection.children()) {
            if(found) {
                break;
            }
            if (child.equals(enchant)) {
                newComponent = newComponent.append(enchant).append(eLevel);
                found = true;
            } else {
                newComponent = newComponent.append(child);
            }
            newComponent = newComponent.append(Component.text(" , ", NamedTextColor.BLUE));
        }
        if (!found) {
            newComponent = newComponent.append(enchant).append(eLevel);
        }
        return newComponent;
    }

    public static Component removeEnchantmentFromSection(Component component, Component enchantment) {
        if (component == null) {
            return Component.empty();
        }
        Component newComponent = Component.empty();
        List<Component> children = component.children();
        for (int i = 0; i < children.size(); i++) {
            Component child = children.get(i);
            if (child.equals(enchantment)) {
                continue; // Skip this enchantment
            }
            newComponent = newComponent.append(child);
            // Append a comma if this is not the last component
            if (i < children.size() - 1) {
                Component nextChild = children.get(i + 1);
                if (!nextChild.equals(enchantment)) {
                    newComponent = newComponent.append(Component.text(", ", NamedTextColor.BLUE));
                }
            }
        }
        // Remove trailing comma if it exists
        String plainText = PlainTextComponentSerializer.plainText().serialize(newComponent);
        if (plainText.endsWith(", ")) {
            plainText = plainText.substring(0, plainText.length() - 2);
            newComponent = Component.text(plainText, newComponent.style());
        }
        return newComponent;
    }

}
