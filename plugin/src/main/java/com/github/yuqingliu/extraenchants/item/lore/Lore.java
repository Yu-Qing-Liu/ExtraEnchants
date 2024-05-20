package com.github.yuqingliu.extraenchants.item.lore;

import com.github.yuqingliu.extraenchants.item.lore.implementations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.Keys;
import net.kyori.adventure.text.Component;

import lombok.Getter;

@Getter
public class Lore {
    private JavaPlugin plugin;
    private ItemStack item;
    private List<Component> lore = new ArrayList<>();
    private List<Component> itemLore;
    private int[] sectionSizes;
    private final Map<String, LoreSection> loreMap = new LinkedHashMap<>();
    private final Component seperator = Component.empty();
    private final NamespacedKey loreKey = Keys.getItemLore();

    public Lore(ItemStack item) {
        this.item = item;
        this.itemLore = getCleanLore();
        this.sectionSizes = deserializeLore();
        initializeSections();
    }

    private void initializeSections() {
        loreMap.put(EnchantmentSection.class.getSimpleName(), new LoreSection(new EnchantmentSection(0, sectionSizes, itemLore)));
        loreMap.put(AbilitySection.class.getSimpleName(), new LoreSection(new AbilitySection(1, sectionSizes, itemLore)));
    }

    private List<Component> getLore() {
        for(LoreSection section : loreMap.values()) {
            List<Component> sectionLore = section.getLore();
            if(!sectionLore.isEmpty()) {
                lore.addAll(sectionLore);
                lore.add(seperator);
            } 
        }

        if(!lore.isEmpty()) {
            lore.remove(lore.size() - 1);
        }
        return lore;
    }

    private List<Component> getCleanLore() {
        List<Component> cleanLore = new ArrayList<>();
        if(item.lore() == null) {
            return cleanLore;
        }
        for(Component line : item.lore()) {
            if(!line.equals(seperator)) {
                cleanLore.add(line);
            }
        }
        return cleanLore;
    }

    private void serializeLore() {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int[] sizes = loreMap.values().stream().map(section -> section.getSize()).mapToInt(Integer::intValue).toArray();
        container.set(loreKey, PersistentDataType.INTEGER_ARRAY, sizes);
        item.setItemMeta(meta);
    }

    private int[] deserializeLore() {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(loreKey, PersistentDataType.INTEGER_ARRAY)) {
            return container.get(loreKey, PersistentDataType.INTEGER_ARRAY); 
        }
        return null;
    }

    public LoreSection getLoreSection(String sectionName) {
        return loreMap.get(sectionName);
    }

    public ItemStack applyLore() {
        serializeLore();
        ItemMeta meta = item.getItemMeta();
        meta.lore(getLore());
        item.setItemMeta(meta);
        return item;
    }
}
