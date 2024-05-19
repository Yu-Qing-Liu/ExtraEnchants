package com.github.yuqingliu.extraenchants.item.lore.implementations;

import java.util.ArrayList;
import java.util.List;

import com.github.yuqingliu.extraenchants.item.lore.AbstractLoreSection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class EnchantmentSection extends AbstractLoreSection {
    private Component comma = Component.text(", ", NamedTextColor.DARK_BLUE);
    
    public EnchantmentSection(List<Component> itemLore) {
        super(itemLore);
    }
    
    @Override
    public List<Component> fetchSection() {
        //TODO
        return itemLore;
    }

    public void addOrUpdateEnchantmentFromSection(Component enchant, Component eLevel) {
        int maxComponents = 7;
        boolean enchantmentFound = false;

        if (lore.isEmpty()) {
            Component currentLine = Component.empty().append(enchant).append(eLevel);
            lore.add(currentLine);
            return;
        } else {
            // Scan components to find if the enchantment exists; if it exists, update its level
            for (int i = 0; i < lore.size(); i++) {
                Component line = lore.get(i);
                List<Component> newChildren = new ArrayList<>();
                boolean found = false;

                for (Component child : line.children()) {
                    if (child.equals(enchant)) {
                        found = true;
                        newChildren.add(enchant);
                        newChildren.add(eLevel);
                    } else if (!found) {
                        newChildren.add(child);
                    }
                }

                if (found) {
                    Component newLine = Component.empty();
                    for (Component child : newChildren) {
                        newLine = newLine.append(child);
                    }
                    lore.set(i, newLine);
                    enchantmentFound = true;
                    break; // Exit the loop once the enchantment is found and updated
                }
            }

            if (!enchantmentFound) {
                // Last line has space, append the component at the end of it
                Component currentLine = lore.get(lore.size() - 1);
                if (getComponentSize(currentLine) < maxComponents) {
                    Component newLine = currentLine.append(Component.text(", ")).append(enchant).append(eLevel);
                    lore.set(lore.size() - 1, newLine);
                } else {
                    // Did not find component and last line is full, add a new line with the enchantment as its first element
                    Component newLine = Component.empty().append(enchant).append(eLevel);
                    lore.add(newLine);
                }
            }
        }
    }

    public void removeEnchantmentFromSection(Component enchant) {
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
        this.lore = newLore;
    }
}
