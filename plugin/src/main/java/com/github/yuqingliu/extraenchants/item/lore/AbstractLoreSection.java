package com.github.yuqingliu.extraenchants.item.lore;

import java.util.ArrayList;
import java.util.List;

import net.kyori.adventure.text.Component;

public abstract class AbstractLoreSection {
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

    public int getSize() {
        return lore.size();
    }

    public List<Component> getLore() {
        return this.lore;
    }

    private void fetchSection() {
        int start = 0;
        int sectionSize = 0;
        if (sectionSizes == null || position >= sectionSizes.length) {
            return;
        }
        for (int i = 0; i < position; i++) {
            start += sectionSizes[i];
        }
        sectionSize = sectionSizes[position];
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
