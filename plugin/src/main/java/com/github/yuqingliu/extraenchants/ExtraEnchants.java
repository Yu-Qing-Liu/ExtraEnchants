package com.github.yuqingliu.extraenchants;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.configuration.ConfigurationManager;
import com.github.yuqingliu.extraenchants.enchantment.EnchantmentManager;
import com.github.yuqingliu.extraenchants.enchants.bow.*;
import com.github.yuqingliu.extraenchants.enchants.crossbow.*;
import com.github.yuqingliu.extraenchants.enchants.armor.*;
import com.github.yuqingliu.extraenchants.enchants.weapons.*;
import com.github.yuqingliu.extraenchants.enchants.melee.*;
import com.github.yuqingliu.extraenchants.enchants.tools.*;
import com.github.yuqingliu.extraenchants.enchants.universal.*;
import com.github.yuqingliu.extraenchants.enchants.ranged.*;
import com.github.yuqingliu.extraenchants.events.*;
import com.github.yuqingliu.extraenchants.commands.*;
import com.github.yuqingliu.extraenchants.persistence.blocks.*;
import com.github.yuqingliu.extraenchants.utils.ItemUtils;

import java.io.File;

public class ExtraEnchants extends JavaPlugin {
    @Override
    public void onEnable() {
        /*Database*/
        File blocksFile = new File(getDataFolder(), "blocks.csv");
        CustomBlockDatabase.start(blocksFile);

        /*Configuration*/
        this.saveDefaultConfig();

        ConfigurationManager configurationManager = new ConfigurationManager(this);
        configurationManager.registerConstants();
        
        /*Enchantments*/
        EnchantmentManager enchantmentManager = new EnchantmentManager(this);
        enchantmentManager.registerEnchants();

        /*Items*/
        ItemUtils itemUtils = new ItemUtils(enchantmentManager);

        /*Commands*/
        this.getCommand("ee").setExecutor(new EECommand(this));
        this.getCommand("eelist").setExecutor(new EEListCommand(this));
        this.getCommand("eeget").setExecutor(new EEGetCommand(this));

        /*Events*/
        getServer().getPluginManager().registerEvents(new PlayerInteractsWithEnchantmentTable(this, configurationManager, enchantmentManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractsWithGrindstone(this, itemUtils, configurationManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractsWithAnvil(this), this);
        getServer().getPluginManager().registerEvents(new PlayerPlacesDestroysAnvil(this), this);
        getServer().getPluginManager().registerEvents(new PlayerPlacesDestroysEtable(this), this);
        getServer().getPluginManager().registerEvents(new PlayerPlacesDestroysGrindstone(this), this);

        getServer().getPluginManager().registerEvents(new Homing(this), this);
        getServer().getPluginManager().registerEvents(new Mitigation(this), this);
        getServer().getPluginManager().registerEvents(new Growth(this), this);
        getServer().getPluginManager().registerEvents(new Snipe(this), this);
        getServer().getPluginManager().registerEvents(new Flame(this), this);
        getServer().getPluginManager().registerEvents(new Power(this), this);
        getServer().getPluginManager().registerEvents(new Wither(this), this);
        getServer().getPluginManager().registerEvents(new Venom(this), this);
        getServer().getPluginManager().registerEvents(new SonicBoom(this), this);
        getServer().getPluginManager().registerEvents(new Replant(this), this);
        getServer().getPluginManager().registerEvents(new Delicate(this), this);
        getServer().getPluginManager().registerEvents(new Smelting(this), this);
        getServer().getPluginManager().registerEvents(new AutoLooting(this), this);
        getServer().getPluginManager().registerEvents(new SilkTouch(this), this);
        getServer().getPluginManager().registerEvents(new PowerStrike(this), this);
        getServer().getPluginManager().registerEvents(new Focus(this), this);
        getServer().getPluginManager().registerEvents(new Warped(this), this);
        getServer().getPluginManager().registerEvents(new LifeSteal(this), this);
        getServer().getPluginManager().registerEvents(new RapidFire(this), this);

    }

    @Override
    public void onDisable() {
        CustomBlockDatabase.stop();
    }
}

