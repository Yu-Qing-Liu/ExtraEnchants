package com.github.yuqingliu.extraenchants.item.lore.implementations;

import java.util.ArrayList;
import java.util.List;

import com.github.yuqingliu.extraenchants.item.lore.AbstractLoreSection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class EnchantmentSection extends AbstractLoreSection {
    private Component comma = Component.text(", ", NamedTextColor.DARK_BLUE);
    private int maxComponents = 3;
    
    public EnchantmentSection(double position, List<Component> itemLore) {
        super(position, itemLore);
    }
    
    public void addOrUpdateEnchantmentFromSection(Component enchant, Component eLevel) {
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
        
        filteredComponents.add(enchant);
        filteredComponents.add(eLevel);
        // Buffer
        filteredComponents.add(Component.empty());
        filteredComponents.add(Component.empty());
        filteredComponents.add(Component.empty());

        // Reconstruct the final lore from filtered components, ensuring max 8 components per line
        Component line = Component.empty();
        int componentCount = 0;
        for (int i = 0; i < filteredComponents.size(); i+=2) {
            Component component = filteredComponents.get(i);
            Component level = filteredComponents.get(i + 1);
            if(component.equals(Component.empty()) || level.equals(Component.empty())) {
                break;
            }

            if (componentCount < maxComponents) {
                if(componentCount != 0) {
                    line = line.append(comma);
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
        this.lore = newLore;
    }

    public void removeEnchantmentFromSection(Component enchant) {
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
        
        // Buffer
        filteredComponents.add(Component.empty());
        filteredComponents.add(Component.empty());
        filteredComponents.add(Component.empty());

        // Reconstruct the final lore from filtered components, ensuring max 8 components per line
        Component line = Component.empty();
        int componentCount = 0;
        for (int i = 0; i < filteredComponents.size(); i+=2) {
            Component component = filteredComponents.get(i);
            Component level = filteredComponents.get(i + 1);
            if(component.equals(Component.empty()) || level.equals(Component.empty())) {
                break;
            }

            if (componentCount < maxComponents) {
                if(componentCount != 0) {
                    line = line.append(comma);
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
        this.lore = newLore;
    }
}
