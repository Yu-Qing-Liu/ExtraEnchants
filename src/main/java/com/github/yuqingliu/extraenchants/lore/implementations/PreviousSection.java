package com.github.yuqingliu.extraenchants.lore.implementations;

import java.util.List;

import com.github.yuqingliu.extraenchants.lore.AbstractLoreSection;

import net.kyori.adventure.text.Component;

public class PreviousSection extends AbstractLoreSection {
    public PreviousSection(int position, int[] sectionSizes, List<Component> itemLore) {
        super(position, null, sectionSizes, itemLore);
        name = this.getClass().getSimpleName();
    }

    public void initialize(List<Component> prevLore) {
        this.lore = prevLore;
    }
}
