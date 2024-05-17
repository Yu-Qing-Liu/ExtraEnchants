package com.github.yuqingliu.extraenchants.enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.custom.*;
import com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla.*;
import com.github.yuqingliu.extraenchants.enchantment.implementations.*;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantmentManager {
    protected JavaPlugin plugin;
    protected FileConfiguration config;
    private NamedTextColor vanilla = NamedTextColor.GRAY;
    private NamedTextColor descriptionColor = NamedTextColor.GRAY;
    private NamedTextColor legendary = NamedTextColor.GOLD;
    private Map<String, Enchantment> enchantmentRegistry = Map.ofEntries (
        // Vanilla enchants
        Map.entry(SwiftSneak.class.getSimpleName(), new Enchantment(new SwiftSneak(vanilla))),
        // Custom enchants
        Map.entry(Homing.class.getSimpleName(), new Enchantment(new Homing(legendary, descriptionColor)))
    );

    public EnchantmentManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public Map<String, Enchantment> getEnchantments() {
        return this.enchantmentRegistry;
    }

    public void registerEnchants() {
        insertEnchants();
        plugin.saveConfig();
        updateEnchants();
    }

    private void insertEnchants() {
        for(Map.Entry<String, Enchantment> entry : enchantmentRegistry.entrySet()) {
            String key = entry.getKey();
            Enchantment enchantment = entry.getValue();
            List<Material> applicableItems = enchantment.getApplicableItems();
            List<Component> applicableNames = enchantment.getApplicableDisplayNames();
            List<String> applicableMaterials = new ArrayList<>();
            List<String[]> applicableDisplayNames = new ArrayList<>();
            for(Material material : applicableItems) {
                applicableMaterials.add(material.name());
            }
            for(Component name : applicableNames) {
                String[] data = new String[2];
                data[0] = TextUtils.componentToString(name);
                data[1] = name.color().asHexString();
                applicableDisplayNames.add(data);
            }
            String path = "Enchantments." + key;
            if(!config.isSet(path)) {
                List<Object> defaultOptions = Arrays.asList(
                    TextUtils.componentToString(enchantment.getName()),
                    enchantment.getNameColor().asHexString(),
                    enchantment.getMaxLevel(),
                    enchantment.getRequiredLevelFormula(),
                    enchantment.getCostFormula(),
                    TextUtils.componentToString(enchantment.getDescription()),
                    enchantment.getDescriptionColor().asHexString(),
                    applicableMaterials,
                    applicableDisplayNames
                );
                config.set(path, defaultOptions);
            }
        }
    }

    private void updateEnchants() {
        ConfigurationSection enchantmentsSection = config.getConfigurationSection("Enchantments");
        if(enchantmentsSection != null) {
            for(String key : enchantmentsSection.getKeys(false)) {
                Object value = enchantmentsSection.get(key);
                Enchantment enchantment = createEnchantment(value);
                if(enchantment != null) {
                    enchantmentRegistry.put(key, enchantment);
                }
            }
        }
    }

    private Enchantment createEnchantment(Object data) {
        if(data instanceof List) {
            List<?> info = (List<?>) data;
            String sname = (String) info.get(0);
            String snameColor = (String) info.get(1);
            TextColor nameColor = TextColor.fromHexString(snameColor);
            Component name = Component.text(sname, nameColor);
            int maxLevel = (int) info.get(2);
            String levelFormula = (String) info.get(3);
            String costFormula = (String) info.get(4);
            String sdescription = (String) info.get(5);
            String sdescriptionColor = (String) info.get(6);
            TextColor descriptionColor = TextColor.fromHexString(sdescriptionColor);
            Component description = Component.text(sdescription, descriptionColor);
            List<?> applicableMaterials = (List<?>) info.get(7);
            List<?> applicableDisplayNames = (List<?>) info.get(7);
            List<Material> applicableItems = parseMaterialList(applicableMaterials);
            List<Component> applicableNames = parseNamesList(applicableDisplayNames);
            for(org.bukkit.enchantments.Enchantment enchantment : Registry.ENCHANTMENT) {
                if(enchantment.getKey().getKey().equals(sdescription)) {
                    return new Enchantment(
                        new VanillaEnchantment(
                            enchantment,
                            name,
                            maxLevel,
                            description,
                            applicableItems,
                            applicableNames,
                            levelFormula,
                            costFormula
                        )
                    );
                }
            }
            return new Enchantment(
                new CustomEnchantment(
                    name,
                    maxLevel,
                    description,
                    applicableItems,
                    applicableNames,
                    levelFormula,
                    costFormula
                )
            );
        }
        return null;
    }

    private List<Material> parseMaterialList(List<?> data) {
        List<Material> applicable = new ArrayList<>();
        for(Object T : data) {
            String t = (String) T;
            Material material = Material.matchMaterial(t);
            applicable.add(material);
        }
        return applicable;
    }

    private List<Component> parseNamesList(List<?> data) {
        List<Component> applicable = new ArrayList<>();
        for(Object T : data) {
            String[] t = (String[]) T;
            String name = t[0];
            TextColor color = TextColor.fromHexString(t[1]);
            Component displayName = Component.text(name,color);
            applicable.add(displayName);
        }
        return applicable;
    }
}
