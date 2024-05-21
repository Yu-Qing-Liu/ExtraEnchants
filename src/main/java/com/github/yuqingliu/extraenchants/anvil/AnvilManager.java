package com.github.yuqingliu.extraenchants.anvil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

public class AnvilManager {
    protected JavaPlugin plugin;
    protected FileConfiguration config; 
    private File file;
    @Getter private Map<Material, List<Material>> anvilRegistry = new HashMap<>();

    public AnvilManager(JavaPlugin plugin) {
        this.plugin = plugin;
        File dir = plugin.getDataFolder();
        File file = new File(dir, "anvil.yml");
        if (!dir.exists()) {
            dir.mkdir();
        } 
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                plugin.getLogger().warning("Could not create anvil.yml configuration file.");
            }
        } 
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
        initializeCombinations();
    }

    public void registerCombinations() {
        insertCombinations();
        try {
            config.save(file);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not save anvil.yml configuration file.");
        }
        updateCombinations();
    }

    public void initializeCombinations() {
        for(Material material : Registry.MATERIAL) {
            anvilRegistry.put(material, new ArrayList<>());
        }
    }

    private void insertCombinations() {
        for(Map.Entry<Material, List<Material>> entry : anvilRegistry.entrySet()) {
            Material key = entry.getKey();
            List<Material> value = entry.getValue();
            String path = "ExtraAnvilCombinations." + key.name();
            if(!config.isSet(path)) {
                List<Object> options = new ArrayList<>();
                for(Material m : value) {
                    options.add(m.name());
                }
                config.set(path, options);
            }
        }
    }

    private void updateCombinations() {
        ConfigurationSection anvilCombinationsSection = config.getConfigurationSection("ExtraAnvilCombinations");
        if(anvilCombinationsSection != null) {
            for(String key: anvilCombinationsSection.getKeys(false)) {
                Material item = Material.matchMaterial(key);
                Object value = anvilCombinationsSection.get(key);
                if(value instanceof List) {
                    List<?> data = (List<?>) value;
                    List<Material> allowed = convertToMaterialList(data);
                    if(!allowed.isEmpty()) {
                        anvilRegistry.put(item, allowed);
                    }
                }
            }
        }
    }

    private List<Material> convertToMaterialList(List<?> inputList) {
        List<Material> materialList = new ArrayList<>();
        for (Object item : inputList) {
            if (item instanceof String) {
                // Convert the string to a Material object
                Material material = Material.matchMaterial((String) item);
                if (material != null) {
                    materialList.add(material);
                } else {
                    plugin.getLogger().warning("Invalid material name: " + item);
                }
            } else {
                plugin.getLogger().warning("Invalid item in the input list: " + item);
            }
        }
        return materialList;
    }
}
