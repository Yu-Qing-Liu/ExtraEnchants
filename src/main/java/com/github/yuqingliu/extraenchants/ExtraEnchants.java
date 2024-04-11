package com.github.yuqingliu.extraenchants;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.Registry;

import java.util.HashMap;
import java.util.List;

import com.github.yuqingliu.extraenchants.enchants.bow.*;
import com.github.yuqingliu.extraenchants.enchants.*;
import com.github.yuqingliu.extraenchants.enchants.crossbow.*;
import com.github.yuqingliu.extraenchants.enchants.armor.*;
import com.github.yuqingliu.extraenchants.enchants.weapons.*;
import com.github.yuqingliu.extraenchants.enchants.tools.*;
import com.github.yuqingliu.extraenchants.events.*;
import com.github.yuqingliu.extraenchants.enchants.utils.*;
import com.github.yuqingliu.extraenchants.commands.*;

public class ExtraEnchants extends JavaPlugin {
    @Override
    public void onEnable() {
        /*Database*/
        Database.registerEnchants(this);

        /*Configuration*/
        this.saveDefaultConfig();

        // Set a default config.yml if not present
        setDefaultEnchantmentsConfig();

        Constants.setBookshelfMultiplier(
            this.getConfig().getInt("BookshelfMultiplier")
        );
        Constants.setRepairAnvilCostPerResource(
            this.getConfig().getDouble("RepairAnvilCostPerResource")
        );
        Constants.setVanillaAnvilCostPerLevel(
            this.getConfig().getInt("VanillaAnvilCostPerLevel")
        );
        Constants.setCustomAnvilCostPerLevel(
            this.getConfig().getInt("CustomAnvilCostPerLevel")
        );

        loadEnchantmentsFromConfig();

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

    }

    @Override
    public void onDisable() {

    }

    private void setDefaultEnchantmentsConfig() {
        FileConfiguration config = this.getConfig();
        boolean changesMade = false;
        List<CustomEnchantment> customEnchantmentRegistry = Database.getCustomEnchantmentRegistry();

        // Check and set other attributes
        if (!config.isSet("BookshelfMultiplier")) {
            config.set("BookshelfMultiplier", 5);
            changesMade = true;
        }

        if (!config.isSet("RepairAnvilCostPerResource")) {
            config.set("RepairAnvilCostPerResource", 1.5);
            changesMade = true;
        }

        if (!config.isSet("VanillaAnvilCostPerLevel")) {
            config.set("VanillaAnvilCostPerLevel", 3);
            changesMade = true;
        }   

        if (!config.isSet("CustomAnvilCostPerLevel")) {
            config.set("CustomAnvilCostPerLevel", 5);
            changesMade = true;
        }

        // Check and set the default enchantments
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            NamespacedKey key = enchantment.getKey();
            String path = "Enchantments." + key.getKey();
            if (!config.isSet(path)) {
                config.set(path, enchantment.getMaxLevel());
                changesMade = true; // Mark that changes have been made
            }
        }

        // Check and set the custom enchantments
        for (CustomEnchantment enchantment : customEnchantmentRegistry) {
            String name = enchantment.getName();
            String path = "CustomEnchantments." + name;
            if (!config.isSet(path)) {
                config.set(path, enchantment.getMaxLevel());
                changesMade = true; // Mark that changes have been made
            }
        }

        // Save the config if any changes have been made
        if (changesMade) {
            this.saveConfig();
        }
    }

    private void loadEnchantmentsFromConfig() {
        HashMap<NamespacedKey, Integer> enchantments = new HashMap<>();
        HashMap<String, Integer> customEnchantments = new HashMap<>();
        FileConfiguration config = this.getConfig();

        ConfigurationSection enchantmentsSection = config.getConfigurationSection("Enchantments");
        if (enchantmentsSection != null) {
            for (String key : enchantmentsSection.getKeys(false)) {
                int level = enchantmentsSection.getInt(key);
                enchantments.put(new NamespacedKey("minecraft", key), level);
            }
        }

        ConfigurationSection customEnchantmentsSection = config.getConfigurationSection("CustomEnchantments");
        if (customEnchantmentsSection != null) {
            for (String key : customEnchantmentsSection.getKeys(false)) {
                int level = customEnchantmentsSection.getInt(key);
                customEnchantments.put(key, level);
            }
        }

        Constants.setEnchantments(enchantments);
        Constants.setCustomEnchantments(customEnchantments);
    }
}

