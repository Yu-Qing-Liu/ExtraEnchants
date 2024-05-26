package com.github.yuqingliu.extraenchants.item.lore;

import java.util.ArrayList;
import java.util.List;

import com.github.yuqingliu.extraenchants.api.item.lore.LoreSection;

import net.kyori.adventure.text.Component;

public abstract class AbstractLoreSection implements LoreSection {
    protected final int position;
    protected List<Component> lore = new ArrayList<>();
    private List<Component> itemLore;
    private int[] sectionSizes;
    protected Component seperator = Component.empty();

    public AbstractLoreSection(int position, int[] sectionSizes, List<Component> itemLore) {
        this.position = position;
        this.itemLore = itemLore;
        this.sectionSizes = sectionSizes;
        fetchSection();
    }
    
    @Override
    public int getPosition() {
        return this.position;
    }
    
    @Override
    public int getSize() {
        return lore.size();
    }
    
    @Override
    public List<Component> getLore() {
        return this.lore;
    }

    private void fetchSection() {
        int start = 0;
        int sectionSize = 0;
        if (sectionSizes == null || getPosition() >= sectionSizes.length) {
            return;
        }
        for (int i = 0; i < getPosition(); i++) {
            start += sectionSizes[i];
        }
        sectionSize = sectionSizes[getPosition()];
        collectLore(start, sectionSize);
    }

    private void collectLore(int start, int sectionSize) {
        int end = start + sectionSize;
        for (int i = start; i < end && i < itemLore.size(); i++) {
            Component line = itemLore.get(i);
            lore.add(line);
        }
    }
}
