package com.github.yuqingliu.extraenchants.managers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.lore.implementations.AbilitySection;
import com.github.yuqingliu.extraenchants.lore.implementations.EnchantmentSection;
import com.google.inject.Inject;

import net.kyori.adventure.text.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
    
    private Map<String, LoreSection> initializeSections(int[] sectionSizes, List<Component> itemLore) {
        Map<String, LoreSection> loreMap = new LinkedHashMap<>();
        loreMap.put(EnchantmentSection.class.getSimpleName(), new EnchantmentSection(1, textManager, sectionSizes, itemLore));
        loreMap.put(AbilitySection.class.getSimpleName(), new AbilitySection(2, sectionSizes, itemLore));
        return loreMap;
    }
    
    private List<Component> getLore(Map<String, LoreSection> loreMap) {
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

    private void serializeLore(ItemStack item, Map<String, LoreSection> loreMap) {
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
        Map<String, LoreSection> loreMap = initializeSections(sectionSizes, itemLore);
        return loreMap.get(sectionName);
    }
    
    @Override
    public ItemStack applyLore(ItemStack item, Map<String, LoreSection> updates) {
        int[] sectionSizes = deserializeLore(item);        
        List<Component> itemLore = getCleanLore(item);
        Map<String, LoreSection> loreMap = initializeSections(sectionSizes, itemLore);
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
        Map<String, LoreSection> loreMap = initializeSections(sectionSizes, itemLore);
        loreMap.put(update.getName(), update);
        serializeLore(item, loreMap);
        ItemMeta meta = item.getItemMeta();
        meta.lore(getLore(loreMap));
        item.setItemMeta(meta);
        return item;
    }
}
