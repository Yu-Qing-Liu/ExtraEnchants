package com.github.yuqingliu.extraenchants.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.persistence.Database;
import com.github.yuqingliu.extraenchants.persistence.anvil.AnvilDatabase;
import com.github.yuqingliu.extraenchants.persistence.enchantments.EnchantmentDatabase;
import com.github.yuqingliu.extraenchants.logger.LoggerImpl;
import com.github.yuqingliu.extraenchants.api.repositories.*;
import com.github.yuqingliu.extraenchants.repositories.*;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginModule extends AbstractModule {
    private final JavaPlugin plugin;

    public PluginModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // Provide Logger
    @Provides
    @Singleton
    public Logger provideExtraEnchantsLogger() {
        return new LoggerImpl();
    }

    // Provide data folder
    @Provides
    @Singleton
    public File providePluginDataFolder() {
        return plugin.getDataFolder();
    }

    // Repositories
    @Provides
    @Singleton
    public EnchantmentRepository provideEnchantmentRepository(ManagerRepository managerRepository, ItemRepository itemRepository) {
        return new EnchantmentRepositoryImpl(managerRepository, itemRepository);
    }

    @Provides
    @Singleton
    public ItemRepository provideItemRepository() {
        return new ItemRepositoryImpl();
    }

    @Provides
    @Singleton
    public AnvilRepository provideAnvilRepository() {
        return new AnvilRepositoryImpl();
    }

    @Provides
    @Singleton
    public ManagerRepository provideManagerRepository(Logger logger) {
        return new ManagerRepositoryImpl(plugin, logger);
    }

    // Persistence
    @Provides
    @Singleton
    public Database provideEnchantmentDatabase(EnchantmentRepository enchantmentRepository) {
        return new EnchantmentDatabase(plugin.getDataFolder(), enchantmentRepository);
    }

    @Provides
    @Singleton
    public Database provideAnvilDatabase(AnvilRepository anvilRepository) {
        return new AnvilDatabase(plugin.getDataFolder(), anvilRepository);
    }
}

