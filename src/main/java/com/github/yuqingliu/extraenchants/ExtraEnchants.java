package com.github.yuqingliu.extraenchants;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.Registry;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.yuqingliu.extraenchants.enchants.bow.*;
import com.github.yuqingliu.extraenchants.enchants.*;
import com.github.yuqingliu.extraenchants.enchants.crossbow.*;
import com.github.yuqingliu.extraenchants.enchants.armor.*;
import com.github.yuqingliu.extraenchants.enchants.weapons.*;
import com.github.yuqingliu.extraenchants.enchants.melee.*;
import com.github.yuqingliu.extraenchants.enchants.tools.*;
import com.github.yuqingliu.extraenchants.events.*;
import com.github.yuqingliu.extraenchants.enchants.utils.*;
import com.github.yuqingliu.extraenchants.commands.*;

import org.bukkit.Material;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

public class ExtraEnchants extends JavaPlugin {
    @Override
    public void onEnable() {
        /*Database*/
        Database.registerEnchants(this);

        /*Configuration*/
        this.saveDefaultConfig();

        // Set a default config.yml if not present
        setDefaultEnchantmentsConfig();

        Constants.setRepairAnvilCostPerResource(
            this.getConfig().getDouble("RepairAnvilCostPerResource")
        );
        Constants.setAnvilCostPerLevel(
            this.getConfig().getInt("AnvilCostPerLevel")
        );

        loadEnchantmentsFromConfig();
        loadExtraEnchantsFromConfig();
        loadAnvilMapFromConfig();

        /*Commands*/
        this.getCommand("ee").setExecutor(new EECommand(this));

        /*Events*/
        getServer().getPluginManager().registerEvents(new PlayerInteractsWithEnchantmentTable(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractsWithGrindstone(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractsWithAnvil(this), this);
        getServer().getPluginManager().registerEvents(new Homing(this), this);
        getServer().getPluginManager().registerEvents(new Mitigation(this), this);
        getServer().getPluginManager().registerEvents(new Growth(this), this);
        getServer().getPluginManager().registerEvents(new Snipe(this), this);
        getServer().getPluginManager().registerEvents(new Flame(this), this);
        getServer().getPluginManager().registerEvents(new Power(this), this);
        getServer().getPluginManager().registerEvents(new Wither(this), this);
        getServer().getPluginManager().registerEvents(new Venom(this), this);
        getServer().getPluginManager().registerEvents(new Replant(this), this);
        getServer().getPluginManager().registerEvents(new SonicBoom(this), this);

    }

    @Override
    public void onDisable() {

    }

    private void setDefaultEnchantmentsConfig() {
        FileConfiguration config = this.getConfig();
        boolean changesMade = false;
        List<CustomEnchantment> customEnchantmentRegistry = Database.getCustomEnchantmentRegistry();

        if (!config.isSet("RepairAnvilCostPerResource")) {
            config.set("RepairAnvilCostPerResource", 1.5);
            changesMade = true;
        }

        if (!config.isSet("AnvilCostPerLevel")) {
            config.set("AnvilCostPerLevel", 3);
            changesMade = true;
        }

        // Check and set the default enchantments
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            NamespacedKey key = enchantment.getKey();
            String path = "Enchantments." + key.getKey();
            if (!config.isSet(path)) {
                List<Object> defaultOptionsVanilla = new ArrayList<>();
                defaultOptionsVanilla.add(enchantment.getMaxLevel());
                defaultOptionsVanilla.add("x^2");
                config.set(path, defaultOptionsVanilla);
                changesMade = true; // Mark that changes have been made
            }
        }

        // Check and set the custom enchantments
        for (CustomEnchantment enchantment : customEnchantmentRegistry) {
            String name = enchantment.getName();
            String path = "CustomEnchantments." + name;
            if (!config.isSet(path)) {
                List<Object> defaultOptionsCustom = new ArrayList<>();
                defaultOptionsCustom.add(enchantment.getMaxLevel());
                defaultOptionsCustom.add("x^2");
                config.set(path, defaultOptionsCustom);
                changesMade = true; // Mark that changes have been made
            }
        }
        
        // Check and set the extra enchants
        if (!config.isConfigurationSection("ExtraEnchantments")) {
            // If it doesn't exist, add default values
            List<Object> defaultEnchantments = new ArrayList<>();
            defaultEnchantments.add("<add enchant command>");
            defaultEnchantments.add("<remove enchant command>");
            defaultEnchantments.add(5); // Default level
            defaultEnchantments.add("x^2");
            defaultEnchantments.add("C133FF"); // Default color
            defaultEnchantments.add(Arrays.asList("DIAMOND_SWORD", "NETHERITE_SWORD", "STICK"));

            config.set("ExtraEnchantments.Test", defaultEnchantments);

            changesMade = true;
        }

        // Check and set the anvil map
        if (!config.isConfigurationSection("ExtraAnvilCombinations")) {
            // If it doesn't exist, add default values
            List<Object> defaultExample = Arrays.asList("NAME_TAG", "PAPER");
            config.set("ExtraAnvilCombinations.NETHERITE_SWORD", defaultExample);
            changesMade = true;
        }

        // Save the config if any changes have been made
        if (changesMade) {
            this.saveConfig();
        }
    }

    private void loadEnchantmentsFromConfig() {
        HashMap<NamespacedKey, List<Object>> enchantments = new HashMap<>();
        HashMap<String, List<Object>> customEnchantments = new HashMap<>();
        FileConfiguration config = this.getConfig();

        ConfigurationSection enchantmentsSection = config.getConfigurationSection("Enchantments");
        if (enchantmentsSection != null) {
            for (String key : enchantmentsSection.getKeys(false)) {
                Object value = enchantmentsSection.get(key);
                if(value instanceof List) {
                    List<?> data = (List<?>) value;
                    Object level = data.get(0);
                    Object expression = data.get(1);
                    enchantments.put(new NamespacedKey("minecraft", key), Arrays.asList(level, expression));
                }
            }
        }

        ConfigurationSection customEnchantmentsSection = config.getConfigurationSection("CustomEnchantments");
        if (customEnchantmentsSection != null) {
            for (String key : customEnchantmentsSection.getKeys(false)) {
                Object value = customEnchantmentsSection.get(key);
                if(value instanceof List) {
                    List<?> data = (List<?>) value;
                    Object level = data.get(0);
                    Object expression = data.get(1);
                    customEnchantments.put(key, Arrays.asList(level, expression));
                }
            }
        }

        Constants.setEnchantments(enchantments);
        Constants.setCustomEnchantments(customEnchantments);
    }

    public void loadExtraEnchantsFromConfig() {
        FileConfiguration config = this.getConfig();

        ConfigurationSection extraEnchantmentsSection = config.getConfigurationSection("ExtraEnchantments");
        if(extraEnchantmentsSection != null) {
            for (String key: extraEnchantmentsSection.getKeys(false)) {
                Object value = extraEnchantmentsSection.get(key);
                if (value instanceof List) {
                    List<?> enchantmentInfo = (List<?>) value;
                    String addCommand = (String) enchantmentInfo.get(0);
                    String removeCommand = (String) enchantmentInfo.get(1);
                    int level = (int) enchantmentInfo.get(2);
                    Object expression = enchantmentInfo.get(3);
                    String color = (String) enchantmentInfo.get(4);
                    List<?> applicable = (List<?>) enchantmentInfo.get(5);
                    registerEnchant(key, addCommand, removeCommand, level, expression, color, applicable);
                }
            }
        }
    }

    public void loadAnvilMapFromConfig() {
        FileConfiguration config = this.getConfig();
        HashMap<Material, List<Material>> anvilData = new HashMap<>();

        ConfigurationSection anvilMap = config.getConfigurationSection("ExtraAnvilCombinations");
        if(anvilMap != null) {
            for(String key : anvilMap.getKeys(false)) {
                Object value = anvilMap.get(key);
                if(value instanceof List) {
                    List<?> combinations = (List<?>) value;
                    Material item = Material.matchMaterial(key);
                    List<Material> applicable = convertToMaterialList(combinations);
                    if(item != null) {
                        anvilData.put(item, applicable);
                    }
                }
            }
        }

        Constants.setAnvilData(anvilData);
    }

    private void registerEnchant(String name, String addCommand, String removeCommand, int level, Object expression, String color, List<?> applicable) {
        TextColor col = TextColor.fromHexString("#"+color);
        CustomEnchantment enchant = new CustomEnchantment(this, name, addCommand, removeCommand, level, col, convertToMaterialList(applicable));
        Database.getCustomEnchantmentRegistry().add(enchant);
        List<Object> attributes = new ArrayList<>();
        attributes.add(level);
        attributes.add(expression);
        Constants.getCustomEnchantments().put(name, attributes);
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
                    getLogger().warning("Invalid material name: " + item);
                }
            } else {
                getLogger().warning("Invalid item in the input list: " + item);
            }
        }
        return materialList;
    }
}

