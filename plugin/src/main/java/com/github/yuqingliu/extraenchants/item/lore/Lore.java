package com.github.yuqingliu.extraenchants.item.lore;

import com.github.yuqingliu.extraenchants.item.lore.implementations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;

import lombok.Getter;

@Getter
public class Lore {
    private ItemStack item;
    private List<Component> lore = new ArrayList<>();
    private List<Component> itemLore;
    private final Map<String, LoreSection> loreMap = new HashMap<>();
    private final Component seperator = Component.empty();
    
    public Lore(ItemStack item) {
        this.item = item;
        this.itemLore = item.lore() != null ? item.lore() : new ArrayList<>();
        loreMap.put(EnchantmentSection.class.getSimpleName(), new LoreSection(new EnchantmentSection(0.0, itemLore)));
        loreMap.put(AbilitySection.class.getSimpleName(), new LoreSection(new AbilitySection(1.0, itemLore)));
    }

    public LoreSection getLoreSection(String sectionName) {
        return loreMap.get(sectionName);
    }

    public List<Component> getLore() {
        for(LoreSection section : loreMap.values()) {
            List<Component> sectionLore = section.getLore();
            if(!sectionLore.isEmpty()) {
                lore.add(seperator);
                lore.addAll(sectionLore);
                lore.add(seperator);
            } else {
                lore.add(seperator);
                lore.add(seperator);
            } 
        }
        return lore;
    }

    public ItemStack applyLore() {
        item.lore(getLore());
        return item;
    }
}
