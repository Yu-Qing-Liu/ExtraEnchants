package com.github.yuqingliu.extraenchants.item.lore;

import java.util.List;

import lombok.Getter;
import net.kyori.adventure.text.Component;

public class LoreSection {
    @Getter private final AbstractLoreSection definition;
    
    public LoreSection(AbstractLoreSection loreSection) {
        this.definition = loreSection;
    }

    public List<Component> getLore() {
        return definition.getLore();
    }
}
