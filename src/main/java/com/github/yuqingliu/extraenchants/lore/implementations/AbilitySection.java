package com.github.yuqingliu.extraenchants.lore.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.enchantment.implementations.AbilityEnchantment;
import com.github.yuqingliu.extraenchants.lore.AbstractLoreSection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AbilitySection extends AbstractLoreSection {
    private Component sectionTitle = Component.text("Abilities:", NamedTextColor.GREEN);
    private Component root = Component.text("Item Ability: ", NamedTextColor.GRAY);

    public AbilitySection(int position, int[] sectionSizes, List<Component> itemLore) {
        super(position, null, sectionSizes, itemLore);
        name = this.getClass().getSimpleName();
    }
    
    public void addOrUpdateAbilityFromSection(Map<Enchantment, Integer> abilityEnchantments) {
        List<Component> newLore = new ArrayList<>();
        for(Map.Entry<Enchantment, Integer> entry : abilityEnchantments.entrySet()) {
            AbilityEnchantment e = (AbilityEnchantment) entry.getKey();
            Component enchant = e.getLeveledName(entry.getValue());
            Component action = e.getAction();
            Component description = e.getLeveledDescription(entry.getValue());
            if(newLore.isEmpty()) {
                newLore.add(sectionTitle);
                newLore.add(root.append(enchant).append(action));
                newLore.add(description);
            } else {
                newLore.add(root.append(enchant).append(action));
                newLore.add(description);
            }
        }
        this.lore = newLore;
    }
}
