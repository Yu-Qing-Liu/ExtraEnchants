package com.github.yuqingliu.extraenchants.item.items;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.item.ItemUtils.Rarity;

import com.github.yuqingliu.extraenchants.api.utils.TextUtils;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ItemManager {
    private JavaPlugin plugin;
    private FileConfiguration config;
    private File file;
    @Getter private final Map<String, Item> itemRegistry = new HashMap<>();

    public ItemManager(JavaPlugin plugin) {
        this.plugin = plugin;
        File dir = plugin.getDataFolder();
        File file = new File(dir, "items.yml");
        if (!dir.exists()) {
            dir.mkdir();
        } 
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                plugin.getLogger().warning("Could not create items.yml configuration file.");
            }
        } 
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
        initializeItems();
        registerItems();
    }

    private void initializeItems() {
        for(Material material : Registry.MATERIAL) {
            itemRegistry.put(
                material.name(),
                new Item(
                    material,
                    Rarity.COMMON,
                    Component.text(TextUtils.formatName(material.name()), NamedTextColor.AQUA),
                    Component.text(TextUtils.formatName(material.name()), NamedTextColor.DARK_GRAY)
                )
            );
        }
    }

    private void registerItems() {
        insertItems();
        try {
            config.save(file);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not save items.yml configuration file.");
        }
        updateItems();
    }
    
    private void insertItems() {
        for(Map.Entry<String, Item> entry : itemRegistry.entrySet()) {
            String key = entry.getKey();
            Item value = entry.getValue();
            String path = "Items." + key;
            if(!config.isSet(path)) {
                List<Object> defaultOptions = Arrays.asList(
                    value.getRarity().name(),
                    TextUtils.componentToJson(value.getDisplayName()),
                    TextUtils.componentToJson(value.getDescription())
                );
                config.set(path, defaultOptions);
            }
        }
    }

    private void updateItems() {
        ConfigurationSection itemsSection = config.getConfigurationSection("Items");
        if(itemsSection != null) {
            for(String key : itemsSection.getKeys(false)) {
                Object value = itemsSection.get(key);
                Item item = createItem(value, key);
                if(item != null) {
                    itemRegistry.put(key, item);
                }
            }
        }
    }

    private Item createItem(Object data, String key) {
        if(data instanceof List) {
            List<?> info = (List<?>) data;
            String srarity = (String) info.get(0);
            String sdisplayName = (String) info.get(1);
            String sdescription = (String) info.get(2);
            Rarity rarity = Rarity.valueOf(srarity);
            Component displayName = TextUtils.jsonToComponent(sdisplayName);
            Component description = TextUtils.jsonToComponent(sdescription);
            Material material = Material.matchMaterial(key);
            return new Item(material, rarity, displayName, description);
        }
        return null;
    }
}
