package com.github.yuqingliu.extraenchants;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.enchants.bow.*;
import com.github.yuqingliu.extraenchants.enchants.crossbow.*;
import com.github.yuqingliu.extraenchants.enchants.armor.*;
import com.github.yuqingliu.extraenchants.enchants.weapons.*;
import com.github.yuqingliu.extraenchants.enchants.tools.*;
import com.github.yuqingliu.extraenchants.events.*;
import com.github.yuqingliu.extraenchants.database.Constants;
import com.github.yuqingliu.extraenchants.commands.*;

public class ExtraEnchants extends JavaPlugin {
    @Override
    public void onEnable() {
        /*Configuration*/
        this.saveDefaultConfig();

        // Set a default config.yml if not present
        if(!this.getConfig().contains("AllowReEnchanting")) this.getConfig().set("AllowReEnchanting", false);
        if(!this.getConfig().contains("HiddenEnchantsCount")) this.getConfig().set("HiddenEnchantsCount", 3);
        if(!this.getConfig().contains("MaxEnchantLevel")) this.getConfig().set("MaxEnchantLevel", 10);
        if(!this.getConfig().contains("BookshelfMultiplier")) this.getConfig().set("BookshelfMultiplier", 5);
        if(!this.getConfig().contains("MaxBookshelves")) this.getConfig().set("MaxBookshelves", 15);
        if(!this.getConfig().contains("MaxCustomEnchantLevel")) this.getConfig().set("MaxCustomEnchantLevel", 5);
        if(!this.getConfig().contains("BookshelfMultiplierCustom")) this.getConfig().set("BookshelfMultiplierCustom", 10);

        Constants.setAllowReEnchanting(
            this.getConfig().getBoolean("AllowReEnchanting")
        );
        Constants.setHiddenEnchantsCount(
            this.getConfig().getInt("HiddenEnchantsCount")
        );
        Constants.setMaxEnchantLevel(
            this.getConfig().getInt("MaxEnchantLevel")
        );
        Constants.setBookshelfMultiplier(
            this.getConfig().getInt("BookshelfMultiplier")
        );
        Constants.setMaxBookshelves(
            this.getConfig().getInt("MaxBookshelves")
        );
        Constants.setMaxCustomEnchantLevel(
            this.getConfig().getInt("MaxCustomEnchantLevel")
        );
        Constants.setBookshelfMultiplierCustom (
            this.getConfig().getInt("BookshelfMultiplierCustom")
        );

        /*Commands*/
        this.getCommand("ee").setExecutor(new EECommand(this));

        /*Events*/
        getServer().getPluginManager().registerEvents(new PlayerInteractsWithEnchantmentTable(this), this);
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
}

