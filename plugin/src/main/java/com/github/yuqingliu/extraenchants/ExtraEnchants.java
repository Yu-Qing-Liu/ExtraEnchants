package com.github.yuqingliu.extraenchants;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.item.ApplicableItemsRegistry;
import com.github.yuqingliu.extraenchants.item.ItemUtils;
import com.github.yuqingliu.extraenchants.anvil.AnvilManager;
import com.github.yuqingliu.extraenchants.configuration.ConfigurationManager;
import com.github.yuqingliu.extraenchants.enchantment.EnchantmentManager;
import com.github.yuqingliu.extraenchants.enchants.EnchantsManager;
import com.github.yuqingliu.extraenchants.events.EventManager;
import com.github.yuqingliu.extraenchants.commands.*;
import com.github.yuqingliu.extraenchants.persistence.blocks.CustomBlockDatabase;

import lombok.Getter;

import java.io.File;

@Getter
public class ExtraEnchants extends JavaPlugin {
    private ConfigurationManager configurationManager;
    private EnchantmentManager enchantmentManager;
    private PluginManager pluginManager;
    private EventManager eventManager;
    private AnvilManager anvilManager;
    private EnchantsManager enchantsManager;
    private ItemUtils itemUtils;
    private ApplicableItemsRegistry applicableItemsRegistry;
    
    @Override
    public void onLoad() {
        Scheduler.setPlugin(this);
        Keys.load(this);
    }

    @Override
    public void onEnable() {
        /*Persistence*/
        File blocksFile = new File(getDataFolder(), "blocks.csv");
        CustomBlockDatabase.start(blocksFile);
        
        /*Plugin Manager*/
        pluginManager = getServer().getPluginManager();
        
        /*Registries*/
        applicableItemsRegistry = new ApplicableItemsRegistry();

        /*Configuration*/
        this.saveDefaultConfig();
        
        /*Constants*/
        configurationManager = new ConfigurationManager(this);
        configurationManager.registerConstants();
        
        /*Enchantments*/
        enchantmentManager = new EnchantmentManager(this);
        enchantmentManager.registerEnchants();
        
        /*Utils*/
        itemUtils = new ItemUtils(this);

        /*Anvil*/
        anvilManager = new AnvilManager(this);
        
        /*Events*/
        eventManager = new EventManager(this);
        eventManager.registerListeners();

        /*Enchants*/
        enchantsManager = new EnchantsManager(this);
        enchantsManager.registerListeners();

        /*Commands*/
        this.getCommand("ee").setExecutor(new EECommand(this));
        this.getCommand("eelist").setExecutor(new EEListCommand(this));
        this.getCommand("eeget").setExecutor(new EEGetCommand(this));
    }

    @Override
    public void onDisable() {
        CustomBlockDatabase.stop();
    }
}

