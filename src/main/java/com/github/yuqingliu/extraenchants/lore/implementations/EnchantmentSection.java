package com.github.yuqingliu.extraenchants.lore.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.managers.TextManager;
import com.github.yuqingliu.extraenchants.lore.AbstractLoreSection;
import com.google.inject.Inject;

import net.kyori.adventure.text.Component;

public class EnchantmentSection extends AbstractLoreSection {
    private final int maxComponents = 3;

    @Inject
    public EnchantmentSection(int position, TextManager textManager, int[] sectionSizes, List<Component> itemLore) {
        super(position, textManager, sectionSizes, itemLore);
        name = this.getClass().getSimpleName();
    }

    public void addOrUpdateEnchantmentFromSection(Map<Enchantment, Integer> sortedEnchants) {
        List<Component> newLore = new ArrayList<>();
        Component line = Component.empty();
        int componentCount = 0;
        Enchantment prev = null;
        for (Map.Entry<Enchantment, Integer> entry : sortedEnchants.entrySet()) {
            Component enchant = entry.getKey().getLeveledName(entry.getValue());
            if (componentCount < maxComponents) {
                if (componentCount != 0) {
                    line = line.append(prev.getComma(sortedEnchants.get(prev)));
                }
                line = line.append(enchant);
                componentCount++;
            } else {
                newLore.add(line);
                line = Component.empty().append(enchant);
                componentCount = 1;
            }
            prev = entry.getKey();
        }
        if (!line.equals(Component.empty())) {
            newLore.add(line);
        }
        this.lore = newLore;
    }
}
