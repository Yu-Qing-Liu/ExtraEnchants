package com.github.yuqingliu.extraenchants.item.lore.implementations;

import java.util.ArrayList;
import java.util.List;

import com.github.yuqingliu.extraenchants.item.lore.AbstractLoreSection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AbilitySection extends AbstractLoreSection {
    private Component sectionTitle = Component.text("Abilities:", NamedTextColor.GREEN);
    private Component root = Component.text("Item Ability: ", NamedTextColor.GRAY);
    public AbilitySection(int position, int[] sectionSizes, List<Component> itemLore) {
        super(position, sectionSizes, itemLore);
    }
    
    public void addOrUpdateAbilityFromSection(Component enchant, Component eLevel, Component action, Component description) {
        if(lore.isEmpty()) {
            lore.add(sectionTitle);
            lore.add(root.append(enchant).append(eLevel).append(action));
            lore.add(description);
        } else {
            lore.add(root.append(enchant).append(eLevel).append(action));
            lore.add(description);
        }
    }

    public void removeAbilityFromSection(Component enchant) {
        List<Component> newLore = new ArrayList<>();
        for (int i = 1; i < lore.size(); i++) {
            Component line = lore.get(i);
            boolean found = false;
            for(Component child : line.children()) {
                if(child.equals(enchant)) {
                    found = true;
                } 
            }
            if(found) {
                i++; // Skip description
            } else {
                newLore.add(line);
            }
        }
        if(!newLore.isEmpty()) {
            newLore.add(0, sectionTitle);
        }
        lore = newLore;
    }
}
