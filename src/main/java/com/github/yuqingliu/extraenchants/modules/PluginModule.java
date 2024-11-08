package com.github.yuqingliu.extraenchants.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.*;
import com.github.yuqingliu.extraenchants.api.persistence.Database;
import com.github.yuqingliu.extraenchants.managers.*;
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
    
    // Provide plugin
    @Provides
    @Singleton
    public JavaPlugin provideJavaPlugin() {
        return plugin;
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

    // Managers
    @Override
    protected void configure() {
        bind(ColorManager.class).to(ColorManagerImpl.class).in(Singleton.class);
        bind(CommandManager.class).to(CommandManagerImpl.class).in(Singleton.class);
        bind(EventManager.class).to(EventManagerImpl.class).in(Singleton.class);
        bind(InventoryManager.class).to(InventoryManagerImpl.class).in(Singleton.class);
        bind(LoreManager.class).to(LoreManagerImpl.class).in(Singleton.class);
        bind(MathManager.class).to(MathManagerImpl.class).in(Singleton.class);
        bind(NameSpacedKeyManager.class).to(NameSpacedKeyManagerImpl.class).in(Singleton.class);
        bind(SoundManager.class).to(SoundManagerImpl.class).in(Singleton.class);
        bind(TextManager.class).to(TextManagerImpl.class).in(Singleton.class);
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
    public ManagerRepository provideManagerRepository(Logger logger, EventManager eventManager, CommandManager commandManager, InventoryManager inventoryManager, NameSpacedKeyManager keyManager, SoundManager soundManager, TextManager textManager, ColorManager colorManager, LoreManager loreManager, MathManager mathManager) {
        return new ManagerRepositoryImpl(plugin, logger, eventManager, commandManager, inventoryManager, keyManager, soundManager, textManager, colorManager, loreManager, mathManager);
    }

    // Persistence
    @Provides
    @Singleton
    @Named("EnchantmentDatabase")
    public Database provideEnchantmentDatabase(EnchantmentRepository enchantmentRepository) {
        return new EnchantmentDatabase(plugin.getDataFolder(), enchantmentRepository);
    }

    @Provides
    @Singleton
    @Named("AnvilDatabase")
    public Database provideAnvilDatabase(AnvilRepository anvilRepository) {
        return new AnvilDatabase(plugin.getDataFolder(), anvilRepository);
    }
}

