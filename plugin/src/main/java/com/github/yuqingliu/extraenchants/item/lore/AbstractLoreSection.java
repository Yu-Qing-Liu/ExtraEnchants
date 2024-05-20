package com.github.yuqingliu.extraenchants.item.lore;

import java.util.ArrayList;
import java.util.List;

import net.kyori.adventure.text.Component;

public abstract class AbstractLoreSection {
    protected final double position;
    protected List<Component> lore = new ArrayList<>();
    protected List<Component> itemLore;
    protected Component seperator = Component.empty();

    public AbstractLoreSection(double position, List<Component> itemLore) {
        this.position = position;
        this.itemLore = itemLore;
        fetchSection();
    }

    protected int getComponentSize(Component component) {
        return component.children().size();
    }

    public List<Component> getLore() {
        return this.lore;
    }

    protected void fetchSection() {
        double pos = 0;
        boolean found = false;
        for(Component component : itemLore) {
            if(found) {
                lore.add(component);
            }
            if(component.equals(seperator)) {
                if(found) {
                    found = false;
                    lore.remove(lore.size() - 1);
                    return;
                }
                if(pos == position) {
                    found = true;
                }
                pos += 0.5;
            }
        }
    }
}
