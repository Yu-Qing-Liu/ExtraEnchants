package com.github.yuqingliu.extraenchants;

import org.bukkit.plugin.PluginManager;

import com.github.yuqingliu.extraenchants.api.ExtraEnchants;
import com.github.yuqingliu.extraenchants.api.command.CommandManager;
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
    private CommandManager commandManager;
    
    @Override
    public void onEnable() {
        /*Persistence*/
        File blocksFile = new File(getDataFolder(), "blocks.csv");
        CustomBlockDatabase.start(blocksFile);
        
        /*Managers*/
        pluginManager = getServer().getPluginManager();
        applicableItemsRegistry = new ApplicableItemsRegistry();
        configurationManager = new ConfigurationManager(this);
        enchantmentManager = new EnchantmentManagerImpl(this);
        itemUtils = new ItemUtilsImpl(this);
        itemManager = new ItemManager(this);
        anvilManager = new AnvilManager(this);
        eventManager = new EventManager(this);
        enchantsManager = new EnchantsManager(this);
        commandManager = new CommandManagerImpl(this);
    }

    @Override
    public void onDisable() {
        CustomBlockDatabase.stop();
    }
}

