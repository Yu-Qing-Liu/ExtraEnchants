package com.github.yuqingliu.extraenchants.item.lore.implementations;

import java.util.ArrayList;
import java.util.List;

import com.github.yuqingliu.extraenchants.item.lore.AbstractLoreSection;

import net.kyori.adventure.text.Component;

public class DescriptionSection extends AbstractLoreSection {
    public DescriptionSection(int position, int[] sectionSizes, List<Component> itemLore) {
        super(position, sectionSizes, itemLore);
    }

    public void updateSection(Component description) {
        lore = new ArrayList<>();
        lore.add(description);
    }
}

