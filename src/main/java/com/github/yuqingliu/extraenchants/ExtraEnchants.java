package com.github.yuqingliu.extraenchants;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.enchants.bow.*;
import com.github.yuqingliu.extraenchants.enchants.armor.*;
import com.github.yuqingliu.extraenchants.events.*;
import com.github.yuqingliu.extraenchants.database.Constants;

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

        /*Commands*/

        /*Events*/
        getServer().getPluginManager().registerEvents(new PlayerInteractsWithEnchantmentTable(this), this);
        getServer().getPluginManager().registerEvents(new Homing(this), this);
        getServer().getPluginManager().registerEvents(new Mitigation(this), this);

    }

    @Override
    public void onDisable() {

    }
}

