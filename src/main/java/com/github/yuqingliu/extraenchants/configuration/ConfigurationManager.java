package com.github.yuqingliu.extraenchants.configuration;

import com.github.yuqingliu.extraenchants.configuration.implementations.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationManager {
    protected JavaPlugin plugin;
    private Map<String, AbstractConstants> constantsRegistry = new LinkedHashMap<>();

    public ConfigurationManager(JavaPlugin plugin) {
        this.plugin = plugin;
        initializeConstants();
        registerConstants();
    }

    private void initializeConstants() {
        constantsRegistry.put(GlobalConstants.class.getSimpleName(), new GlobalConstants(plugin));
        constantsRegistry.put(AnvilConstants.class.getSimpleName(), new AnvilConstants(plugin));
        constantsRegistry.put(CooldownConstants.class.getSimpleName(), new CooldownConstants(plugin));
    }

    public Map<String, AbstractConstants> getConstants() {
        return this.constantsRegistry;  
    }

    private void registerConstants() {
        for(AbstractConstants constants : constantsRegistry.values()) {
            constants.registerConstants();
        }
        plugin.saveConfig();
    }
}
