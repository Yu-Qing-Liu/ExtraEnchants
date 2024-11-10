package com.github.yuqingliu.extraenchants.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.persistence.Database;
import com.github.yuqingliu.extraenchants.api.managers.*;
import com.github.yuqingliu.extraenchants.managers.*;
import com.github.yuqingliu.extraenchants.persistence.anvil.AnvilDatabase;
import com.github.yuqingliu.extraenchants.persistence.blocks.CustomBlockDatabase;
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
    
    // Provide plugin
    @Provides
    @Singleton
    public JavaPlugin provideJavaPlugin() {
        return plugin;
    }

    // Provide data folder
    @Provides
    @Singleton
    public File providePluginDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    protected void configure() {
        // Logger
        bind(Logger.class).to(LoggerImpl.class).in(Singleton.class);
        // Managers
        bind(ColorManager.class).to(ColorManagerImpl.class).in(Singleton.class);
        bind(CommandManager.class).to(CommandManagerImpl.class).in(Singleton.class);
        bind(EventManager.class).to(EventManagerImpl.class).in(Singleton.class);
        bind(InventoryManager.class).to(InventoryManagerImpl.class).in(Singleton.class);
        bind(LoreManager.class).to(LoreManagerImpl.class).in(Singleton.class);
        bind(MathManager.class).to(MathManagerImpl.class).in(Singleton.class);
        bind(NameSpacedKeyManager.class).to(NameSpacedKeyManagerImpl.class).in(Singleton.class);
        bind(SoundManager.class).to(SoundManagerImpl.class).in(Singleton.class);
        bind(TextManager.class).to(TextManagerImpl.class).in(Singleton.class);
        bind(ConfigurationManager.class).to(ConfigurationManagerImpl.class).in(Singleton.class);
        // Repositories
        bind(EnchantmentRepository.class).to(EnchantmentRepositoryImpl.class).in(Singleton.class);
        bind(ItemRepository.class).to(ItemRepositoryImpl.class).in(Singleton.class);
        bind(AnvilRepository.class).to(AnvilRepositoryImpl.class).in(Singleton.class);
        bind(CustomBlockRepository.class).to(CustomBlockRepositoryImpl.class).in(Singleton.class);
        bind(ManagerRepository.class).to(ManagerRepositoryImpl.class).in(Singleton.class);
        // Persistence
        bind(Database.class).annotatedWith(Names.named(EnchantmentDatabase.class.getSimpleName())).to(EnchantmentDatabase.class).in(Singleton.class);
        bind(Database.class).annotatedWith(Names.named(AnvilDatabase.class.getSimpleName())).to(AnvilDatabase.class).in(Singleton.class);
        bind(Database.class).annotatedWith(Names.named(CustomBlockDatabase.class.getSimpleName())).to(CustomBlockDatabase.class).in(Singleton.class);
    }
}

