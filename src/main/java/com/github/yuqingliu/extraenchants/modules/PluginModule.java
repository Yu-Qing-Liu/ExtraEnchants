package com.github.yuqingliu.extraenchants.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.*;
import com.github.yuqingliu.extraenchants.api.persistence.Database;
import com.github.yuqingliu.extraenchants.api.repositories.*;
import com.github.yuqingliu.extraenchants.managers.*;
import com.github.yuqingliu.extraenchants.persistence.enchantments.EnchantmentDatabase;
import com.github.yuqingliu.extraenchants.logger.LoggerImpl;
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
    public EnchantmentRepository provideEnchantmentRepository(ItemRepository itemRepository, TextManager textManager, LoreManager loreManager, ColorManager colorManager, NameSpacedKeyManager keyManager) {
        return new EnchantmentRepositoryImpl(itemRepository, textManager, loreManager, colorManager, keyManager);
    }

    @Provides
    @Singleton
    public ItemRepository provideItemRepository() {
        return new ItemRepositoryImpl();
    }

    // Persistence
    @Provides
    @Singleton
    public Database provideEnchantmentDatabase(EnchantmentRepository enchantmentRepository) {
        return new EnchantmentDatabase(plugin.getDataFolder(), enchantmentRepository);
    }

    // Managers
    @Provides
    @Singleton
    public MathManager provideMathManager() {
        return new MathManagerImpl();
    }

    @Provides
    @Singleton
    public NameSpacedKeyManager provideNameSpacedKeyManager() {
        return new NameSpacedKeyManagerImpl(plugin);
    }

    @Provides
    @Singleton
    public SoundManager provideSoundManager() {
        return new SoundManagerImpl();
    }

    @Provides
    @Singleton
    public EventManager provideEventManager(InventoryManager inventoryManager) {
        return new EventManagerImpl(plugin, inventoryManager);
    }

    @Provides
    @Singleton
    public InventoryManager provideInventoryManager(MathManager mathManager, SoundManager soundManager, Logger logger, EnchantmentRepository enchantmentRepository) {
        return new InventoryManagerImpl(mathManager, soundManager, logger, enchantmentRepository);
    }

    @Provides
    @Singleton
    public CommandManager provideCommandManager(Logger logger) {
        return new CommandManagerImpl(plugin, logger);
    }

    @Provides
    @Singleton
    public ColorManager provideColorManager() {
        return new ColorManagerImpl();
    }

    @Provides 
    @Singleton
    public TextManager provideTextManager() {
        return new TextManagerImpl();
    }

    @Provides
    @Singleton
    public LoreManager provideLoreManager(TextManager textManager, NameSpacedKeyManager keyManager) {
        return new LoreManagerImpl(textManager, keyManager);
    }
}

