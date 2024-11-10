package com.github.yuqingliu.extraenchants.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.managers.ConfigurationManager;
import com.google.inject.Inject;

import lombok.Getter;

@Getter
public class ConfigurationManagerImpl implements ConfigurationManager {
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private boolean enableEtable = false;
    private boolean enableAnvil = false;
    private boolean enableGrindstone = false;
    private double anvilRepairCost = 1.5;
    private double anvilUpgradeCost = 2;
    
    @Inject
    public ConfigurationManagerImpl(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Inject
    public void postConstruct() {
        enableEtable = setConstant("EnableEtableGUIEverywhere", enableEtable, Boolean.class);
        enableAnvil = setConstant("EnableAnvilGUIEverywhere", enableAnvil, Boolean.class);
        enableGrindstone = setConstant("EnableGrindstoneGUIEverywhere", enableAnvil, Boolean.class);
        anvilRepairCost = setConstant("AnvilRepairCostPerMaterial", anvilRepairCost, Double.class);
        anvilUpgradeCost = setConstant("AnvilLevelUpgradeCost", anvilUpgradeCost, Double.class);
        plugin.saveConfig();
    }

    @Override
    public boolean enableEtable() {
        return this.enableEtable;
    }

    @Override
    public boolean enableAnvil() {
        return this.enableAnvil;
    }

    @Override
    public boolean enableGrindstone() {
        return this.enableGrindstone;
    }
    
    @Override
    public <T> T setConstant(String key, T value, Class<T> clazz) {
        if(!config.isSet(key)) {
            config.set(key, value);
            return value;
        }
        return clazz.cast(config.get(key));
    }
}
