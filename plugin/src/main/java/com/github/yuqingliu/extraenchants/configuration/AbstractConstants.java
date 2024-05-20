package com.github.yuqingliu.extraenchants.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractConstants {
    private JavaPlugin plugin;
    private FileConfiguration config;

    public AbstractConstants(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    protected Object setConstant(String key, Object value) {
        if(!config.isSet(key)) {
            config.set(key, value);
            return value;
        }
        value = plugin.getConfig().get(key);
        return value;
    }

    public abstract void registerConstants();
}
