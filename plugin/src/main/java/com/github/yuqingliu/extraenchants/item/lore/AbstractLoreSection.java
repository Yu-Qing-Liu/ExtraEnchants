package com.github.yuqingliu.extraenchants.item.lore;

import java.util.List;

import net.kyori.adventure.text.Component;

public abstract class AbstractLoreSection {
    protected List<Component> lore;
    protected List<Component> itemLore;

    public AbstractLoreSection(List<Component> itemLore) {
        this.itemLore = itemLore;
        this.lore = fetchSection();
    }

    protected int getComponentSize(Component component) {
        return component.children().size();
    }

    public List<Component> getLore() {
        return this.lore;
    }

    public abstract List<Component> fetchSection();
}
