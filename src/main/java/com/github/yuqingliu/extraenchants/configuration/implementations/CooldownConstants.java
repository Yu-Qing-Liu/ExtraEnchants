package com.github.yuqingliu.extraenchants.configuration.implementations;

import com.github.yuqingliu.extraenchants.configuration.AbstractConstants;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class CooldownConstants extends AbstractConstants {
    private Map<String, Integer> cooldownRegistry = new HashMap<>();
    private FileConfiguration config;

    public CooldownConstants(JavaPlugin plugin) {
        super(plugin);
        this.config = plugin.getConfig();
        cooldownRegistry.put("SonicBoom", 10);
        cooldownRegistry.put("RapidFire", 10);
    }
    
    @Override
    public void registerConstants() {
        for(Map.Entry<String, Integer> entry : cooldownRegistry.entrySet()) {
            String key = entry.getKey();
            String path = "Cooldowns." + key;
            int value = entry.getValue();
            if(!config.isSet(path)) {
                config.set(path, value);
            } else {
                int newValue = config.getInt(path);
                cooldownRegistry.put(key, newValue);
            }
        }
    }
}
