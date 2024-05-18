package com.github.yuqingliu.extraenchants.configuration;

import com.github.yuqingliu.extraenchants.configuration.implementations.*;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationManager {
    protected JavaPlugin plugin;
    private Map<String, AbstractConstants> constantsRegistry = new HashMap<>();

    public ConfigurationManager(JavaPlugin plugin) {
        this.plugin = plugin;
        constantsRegistry.put(GlobalConstants.class.getSimpleName(), new GlobalConstants(plugin));
        constantsRegistry.put(AnvilConstants.class.getSimpleName(), new AnvilConstants(plugin));
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
