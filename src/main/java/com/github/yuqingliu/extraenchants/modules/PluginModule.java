package com.github.yuqingliu.extraenchants.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.CommandManager;
import com.github.yuqingliu.extraenchants.api.managers.EventManager;
import com.github.yuqingliu.extraenchants.api.managers.InventoryManager;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.api.managers.SoundManager;
import com.github.yuqingliu.extraenchants.logger.LoggerImpl;
import com.github.yuqingliu.extraenchants.managers.CommandManagerImpl;
import com.github.yuqingliu.extraenchants.managers.EventManagerImpl;
import com.github.yuqingliu.extraenchants.managers.InventoryManagerImpl;
import com.github.yuqingliu.extraenchants.managers.NameSpacedKeyManagerImpl;
import com.github.yuqingliu.extraenchants.managers.SoundManagerImpl;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginModule extends AbstractModule {
    private final JavaPlugin plugin;

    public PluginModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Provides
    @Singleton
    public Logger provideExtraEnchantsLogger() {
        return new LoggerImpl();
    }

    // Repositories

    // Managers
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
    public EventManager provideEventManager() {
        return new EventManagerImpl(plugin);
    }

    @Provides
    @Singleton
    public InventoryManager provideInventoryManager(EventManager eventManager, SoundManager soundManager, Logger logger) {
        return new InventoryManagerImpl(eventManager, soundManager, logger);
    }

    @Provides
    @Singleton
    public CommandManager provideCommandManager(Logger logger) {
        return new CommandManagerImpl(plugin, logger);
    }

}
