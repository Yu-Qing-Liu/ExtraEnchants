package com.github.yuqingliu.extraenchants.managers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.lore.LoreSection;
import com.github.yuqingliu.extraenchants.api.managers.LoreManager;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.api.managers.TextManager;
import com.github.yuqingliu.extraenchants.lore.implementations.AbilitySection;
import com.github.yuqingliu.extraenchants.lore.implementations.EnchantmentSection;
import com.google.inject.Inject;

import net.kyori.adventure.text.Component;

import lombok.Getter;

@Getter
public class LoreManagerImpl implements LoreManager {
    private final JavaPlugin plugin;
    private final TextManager textManager;
    private List<Component> lore = new ArrayList<>();
    private final Component seperator = Component.empty();
    private final NamespacedKey loreKey;
    
    @Inject
    public LoreManagerImpl(JavaPlugin plugin, TextManager textManager, NameSpacedKeyManager keyManager) {
        this.plugin = plugin;
        this.textManager = textManager;
        this.loreKey = keyManager.getLoreKey();
    }
    
    private Map<Integer, LoreSection> initializeSections(int[] sectionSizes, List<Component> itemLore) {
        Map<Integer, LoreSection> loreMap = new TreeMap<>();
        loreMap.put(0, new EnchantmentSection(0, textManager, sectionSizes, itemLore));
        loreMap.put(1, new AbilitySection(1, sectionSizes, itemLore));
        return loreMap;
    }
    
    private List<Component> getLore(Map<Integer, LoreSection> loreMap) {
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

    private List<Component> getCleanLore(ItemStack item) {
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

    private void serializeLore(ItemStack item, Map<Integer, LoreSection> loreMap) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int[] sizes = loreMap.values().stream().map(section -> section.getSize()).mapToInt(Integer::intValue).toArray();
        container.set(loreKey, PersistentDataType.INTEGER_ARRAY, sizes);
        item.setItemMeta(meta);
    }

    private int[] deserializeLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(loreKey, PersistentDataType.INTEGER_ARRAY)) {
            return container.get(loreKey, PersistentDataType.INTEGER_ARRAY); 
        }
        return null;
    }
    
    @Override
    public LoreSection getLoreSection(String sectionName, ItemStack item) {
        int[] sectionSizes = deserializeLore(item);        
        List<Component> itemLore = getCleanLore(item);
        Map<Integer, LoreSection> loreMap = initializeSections(sectionSizes, itemLore);
        return loreMap.values().stream().filter(section -> section.getName().equals(sectionName)).findFirst().orElse(null);
    }
    
    @Override
    public ItemStack applyLore(ItemStack item, Map<Integer, LoreSection> updates) {
        int[] sectionSizes = deserializeLore(item);        
        List<Component> itemLore = getCleanLore(item);
        Map<Integer, LoreSection> loreMap = initializeSections(sectionSizes, itemLore);
        loreMap.putAll(updates);
        serializeLore(item, loreMap);
        ItemMeta meta = item.getItemMeta();
        meta.lore(getLore(loreMap));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack applyLore(ItemStack item, LoreSection update) {
        int[] sectionSizes = deserializeLore(item);        
        List<Component> itemLore = getCleanLore(item);
        Map<Integer, LoreSection> loreMap = initializeSections(sectionSizes, itemLore);
        loreMap.put(update.getPosition(), update);
        serializeLore(item, loreMap);
        ItemMeta meta = item.getItemMeta();
        meta.lore(getLore(loreMap));
        item.setItemMeta(meta);
        return item;
    }
}
