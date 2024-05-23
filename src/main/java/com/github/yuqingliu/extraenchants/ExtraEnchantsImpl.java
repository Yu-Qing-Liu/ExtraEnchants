package com.github.yuqingliu.extraenchants;

import org.bukkit.plugin.PluginManager;

import com.github.yuqingliu.extraenchants.api.ExtraEnchants;
import com.github.yuqingliu.extraenchants.api.item.ApplicableItemsRegistry;

import com.github.yuqingliu.extraenchants.item.ItemUtilsImpl;
import com.github.yuqingliu.extraenchants.item.items.ItemManager;
import com.github.yuqingliu.extraenchants.anvil.AnvilManager;
import com.github.yuqingliu.extraenchants.configuration.ConfigurationManager;
import com.github.yuqingliu.extraenchants.enchantment.EnchantmentManagerImpl;
import com.github.yuqingliu.extraenchants.enchants.EnchantsManager;
import com.github.yuqingliu.extraenchants.events.EventManager;
import com.github.yuqingliu.extraenchants.commands.*;
import com.github.yuqingliu.extraenchants.persistence.blocks.CustomBlockDatabase;

import lombok.Getter;

import java.io.File;

@Getter
public final class ExtraEnchantsImpl extends ExtraEnchants {
    private PluginManager pluginManager;
    private ConfigurationManager configurationManager;
    private EnchantmentManagerImpl enchantmentManager;
    private EventManager eventManager;
    private AnvilManager anvilManager;
    private EnchantsManager enchantsManager;
    private ItemUtilsImpl itemUtils;
    private ApplicableItemsRegistry applicableItemsRegistry;
    private ItemManager itemManager;
    
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
        enchantmentManager = new EnchantmentManagerImpl(this);
        enchantmentManager.registerEnchants();
        
        /*Items*/
        itemUtils = new ItemUtilsImpl(this);
        itemManager = new ItemManager(this);
        itemManager.registerItems();

        /*Anvil*/
        anvilManager = new AnvilManager(this);
        anvilManager.registerCombinations();
        
        /*Events*/
        eventManager = new EventManager(this);
        eventManager.registerListeners();

        /*Enchants*/
        enchantsManager = new EnchantsManager(this);
        enchantsManager.registerListeners();

        /*Commands*/
        this.getCommand("ee").setExecutor(new EECommand(this));
        this.getCommand("eelist").setExecutor(new EEListCommand(this));
        this.getCommand("eeget").setExecutor(new EEGetCommand());
    }

    @Override
    public void onDisable() {
        CustomBlockDatabase.stop();
    }
}

