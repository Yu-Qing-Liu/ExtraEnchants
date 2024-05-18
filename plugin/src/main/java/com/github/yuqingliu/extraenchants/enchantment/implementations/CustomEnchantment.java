package com.github.yuqingliu.extraenchants.enchantment.implementations;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;
import java.util.ArrayDeque;
import java.util.ArrayList;

import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class CustomEnchantment extends AbstractEnchantment {
    private Component comma = Component.text(", ", NamedTextColor.BLUE);

    public CustomEnchantment(Component name, int maxLevel, Component description, List<Material> applicable, List<Component> applicableDisplayNames, String levelFormula, String costFormula) {
        super(name, maxLevel, description, applicable, applicableDisplayNames, levelFormula, costFormula);
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
                List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
                existingLore = addOrUpdateEnchantmentFromSection(existingLore, name, eLevel);
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
            List<Component> existingLore = meta.lore() != null ? meta.lore() : new ArrayList<>();
            existingLore = removeEnchantmentFromSection(existingLore, name);
            meta.lore(existingLore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private int getComponentSize(Component component) {
        return component.children().size();
    }

    private List<Component> addOrUpdateEnchantmentFromSection(List<Component> lore, Component enchant, Component eLevel) {
        int maxComponents = 7;

        if (lore.isEmpty()) {
            Component currentLine = Component.empty().append(enchant).append(eLevel);
            lore.add(currentLine);
            return lore;
        } else {
            // Scan component to find if the enchantment exists, if it exists, update its level;
            for (int i = 0; i < lore.size(); i++) {
                Component line = lore.get(i);
                List<Component> newChildren = new ArrayList<>();
                boolean found = false;

                for (Component child : line.children()) {
                    if (found) {
                        newChildren.add(eLevel);
                        found = false; // reset after adding the level
                    } else {
                        if (child.equals(enchant)) {
                            newChildren.add(enchant);
                            found = true; // mark found and add level in the next iteration
                        } else {
                            newChildren.add(child);
                        }
                    }
                }

                if (found) {
                    Component newLine = Component.empty();
                    for (Component child : newChildren) {
                        newLine = newLine.append(child);
                    }
                    lore.set(i, newLine);
                    return lore;
                }
            }

            // Last line has space, append the component at the end of it
            Component currentLine = lore.get(lore.size() - 1);
            if (getComponentSize(currentLine) < maxComponents) {
                Component newLine = currentLine.append(comma).append(enchant).append(eLevel);
                lore.set(lore.size() - 1, newLine);
                return lore;
            }

            // Did not find component and last line is full, add a new line with the enchantment as its first element
            Component newLine = Component.empty().append(enchant).append(eLevel);
            lore.add(newLine);
            return lore;
        }
    }

    public List<Component> removeEnchantmentFromSection(List<Component> lore, Component enchant) {
        int maxComponents = 3;
        List<Component> newLore = new ArrayList<>();
        List<Component> filteredComponents = new ArrayList<>();

        // Flatten and filter the lore components in one go, removing all commas
        for (Component line : lore) {
            List<Component> children = line.children();
            for (int i = 0; i < children.size(); i++) {
                Component child = children.get(i);
                if (child.equals(enchant)) {
                    i++;
                } else if (!child.equals(comma) && !child.equals(Component.empty())) {
                    filteredComponents.add(child);
                }
            }
        }

        // Reconstruct the final lore from filtered components, ensuring max 8 components per line
        Component line = Component.empty();
        int componentCount = 0;
        for (int i = 1; i < filteredComponents.size(); i+=2) {
            Component component = filteredComponents.get(i - 1);
            Component level = filteredComponents.get(i);

            if (componentCount < maxComponents) {
                if (componentCount > 0) {
                    line = line.append(comma); // Append comma between components
                }
                line = line.append(component);
                line = line.append(level);
                componentCount++;
            } else {
                newLore.add(line);
                line = Component.empty().append(component);
                line = line.append(level);
                componentCount = 1;
            }
        }
        if (!line.equals(Component.empty())) {
            newLore.add(line);
        }

        return newLore;
    }
}
