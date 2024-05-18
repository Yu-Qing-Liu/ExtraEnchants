package com.github.yuqingliu.extraenchants.enchantment.implementations;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;

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
    public Component getDescription() {
        Pattern replace = Pattern.compile("(\\d+%?)");
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
        .match(replace)
        .replacement((matchResult, builder) -> {
            String numberStr = matchResult.group(1); // Get the captured number as a string
            String percentage = matchResult.groupCount() > 1 ? matchResult.group(2) : ""; // Get the percentage symbol if it exists
            return Component.text(numberStr + percentage, NamedTextColor.YELLOW); // Replace with multiplied value, preserving style
        })
        .build();

        Component finalComponent = description.replaceText(replacementConfig);
        return finalComponent;
    }

    @Override
    public Component getLeveledDescription(int level) {
        Pattern remove = Pattern.compile("per level");
        Component leveledDescription = description.replaceText(builder -> builder.match(remove).replacement(Component.empty()));
        
        Pattern replace = Pattern.compile("(\\d+%?)");
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
        .match(replace)
        .replacement((matchResult, builder) -> {
            String numberStr = matchResult.group(1); // Get the captured number as a string
            String percentage = matchResult.groupCount() > 1 ? matchResult.group(2) : ""; // Get the percentage symbol if it exists
            int number = Integer.parseInt(numberStr); // Convert the string to an integer
            int multipliedValue = number * level; // Multiply by the level
            return Component.text(String.valueOf(multipliedValue) + percentage, NamedTextColor.YELLOW); // Replace with multiplied value, including percentage symbol if present
        })
        .build();

        Component finalComponent = leveledDescription.replaceText(replacementConfig);

        return finalComponent;
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
        if(level <= maxLevel) {
            if(item.getType() == Material.BOOK) {
                item = new ItemStack(Material.ENCHANTED_BOOK);
            }
            ItemMeta meta = item.getItemMeta();
            if(meta != null) {
                List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
                List<Component> newLore = new ArrayList<>();
                boolean found = false;
                for (int i = 0; i < existingLore.size(); i++) {
                    Component current = existingLore.get(i);
                    if(!found && current.contains(name)) {
                        i++; // Skip description
                        found = true;
                    } else {
                        newLore.add(current);
                    }
                }
                existingLore = newLore;
                Component enchantName = name.append(Component.text(" " + TextUtils.toRoman(level), name.color()));
                existingLore.add(enchantName);
                existingLore.add(description);
                meta.lore(existingLore);
                item.setItemMeta(meta);
                NBTItem nbtItem = new NBTItem(item);
                nbtItem.setInteger("extra-enchants."+name, level);
                return nbtItem.getItem();
            }
        }
        return item;
    }
    
    @Override
    public ItemStack removeEnchantment(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
        List<Component> newLore = new ArrayList<>();
        boolean found = false;
        for (int i = 0; i < existingLore.size(); i++) {
            Component current = existingLore.get(i);
            if(!found && current.contains(name)) {
                i++; // Skip description
                found = true;
            } else {
                newLore.add(current);
            }
        }
        existingLore = newLore;
        meta.lore(existingLore);
        item.setItemMeta(meta);
        NBTItem nbtItem = new NBTItem(item);
        if(nbtItem.hasTag("extra-enchants."+name)) {
            nbtItem.removeKey("extra-enchants."+name);
        }
        return nbtItem.getItem();
    }
}
