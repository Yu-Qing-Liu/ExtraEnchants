package com.github.yuqingliu.extraenchants.item.lore;

import com.github.yuqingliu.extraenchants.item.lore.implementations.*;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

import de.tr7zw.changeme.nbtapi.NBTItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

import lombok.Getter;

@Getter
public class Lore {
    private ItemStack item;
    private List<Component> lore = new ArrayList<>();
    private List<Component> itemLore;
    private final Map<String, LoreSection> loreMap = new LinkedHashMap<>();
    private final Component seperator = Component.empty();

    public Lore(ItemStack item) {
        this.item = item;
        this.itemLore = deserializeLore();
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
        List<Component> data = getLore();
        serializeLore(data);
        List<Component> cleanLore = cleanLore(data);
        ItemMeta meta = item.getItemMeta();
        meta.lore(cleanLore);
        item.setItemMeta(meta);
        return item;
    }

    private void serializeLore(List<Component> data) {
        NBTItem nbtItem = new NBTItem(item);
        StringBuilder componentData = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            Component line = data.get(i);
            componentData.append(TextUtils.componentToJson(line));
            if (i < data.size() - 1) {
                componentData.append("+"); // Add delimiter between components
            }
        }
        nbtItem.setString("extra-enchants.lore", componentData.toString());
        item = nbtItem.getItem();
    }

    private List<Component> deserializeLore() {
        List<Component> prevLore = new ArrayList<>();
        NBTItem nbtItem = new NBTItem(item);
        String componentData = nbtItem.getString("extra-enchants.lore");
        String[] parts = componentData.split("\\+");
        if(!componentData.isBlank()) {
            for (String part : parts) {
                Component line = TextUtils.jsonToComponent(part);
                prevLore.add(line);
            }
        }
        return prevLore;
    }

    private List<Component> cleanLore(List<Component> data) {
        List<Component> cleanLore = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Component line = data.get(i);
            if(line.equals(Component.empty())) {
                if((i + 1) < data.size() && data.get(i + 1).equals(Component.empty())) {
                    continue;
                }
            }
            cleanLore.add(line);
        }
        if(!cleanLore.isEmpty()) {
            cleanLore.remove(cleanLore.size() - 1);
        }
        if(!cleanLore.isEmpty()) {
            cleanLore.remove(0);
        }
        return cleanLore;
    }
}
