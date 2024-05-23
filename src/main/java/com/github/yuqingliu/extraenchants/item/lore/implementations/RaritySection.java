package com.github.yuqingliu.extraenchants.item.lore.implementations;

import java.util.ArrayList;
import java.util.List;

import com.github.yuqingliu.extraenchants.api.item.ItemUtils.Rarity;

import com.github.yuqingliu.extraenchants.item.lore.AbstractLoreSection;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

public class RaritySection extends AbstractLoreSection {
    public RaritySection(int position, int[] sectionSizes, List<Component> itemLore) {
        super(position, sectionSizes, itemLore);
    }

    public void updateSection(Rarity rarity) {
        lore = new ArrayList<>();
        lore.add(Component.text(rarity.name(), rarity.getColor()).decorate(TextDecoration.BOLD));
    }
}

