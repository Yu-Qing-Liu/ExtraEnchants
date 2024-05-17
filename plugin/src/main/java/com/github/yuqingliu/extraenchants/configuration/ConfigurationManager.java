package com.github.yuqingliu.extraenchants.configuration;

import com.github.yuqingliu.extraenchants.configuration.implementations.*;

import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationManager {
    protected JavaPlugin plugin;
    private final Map<String, AbstractConstants> constantsRegistry = Map.ofEntries(
        Map.entry(GlobalConstants.class.getSimpleName(), new GlobalConstants(plugin))
    );

    public ConfigurationManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Map<String, AbstractConstants> getConstants() {
        return this.constantsRegistry;  
    }

    public void registerConstants() {
        for(AbstractConstants constants : constantsRegistry.values()) {
            constants.registerConstants();
        }
        plugin.saveConfig();
    }
}
