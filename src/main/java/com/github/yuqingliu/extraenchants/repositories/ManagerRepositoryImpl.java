package com.github.yuqingliu.extraenchants.repositories;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.*;
import com.github.yuqingliu.extraenchants.api.repositories.*;
import com.github.yuqingliu.extraenchants.managers.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import lombok.Getter;

@Getter
@Singleton
public class ManagerRepositoryImpl implements ManagerRepository {
    private final JavaPlugin plugin;
    private final EventManager eventManager;
    private final CommandManager commandManager;
    private final InventoryManager inventoryManager;
    private final NameSpacedKeyManager keyManager;
    private final SoundManager soundManager;
    private final TextManager textManager;
    private final ColorManager colorManager;
    private final LoreManager loreManager;
    private final MathManager mathManager;
    
    @Inject
    public ManagerRepositoryImpl(JavaPlugin plugin, Logger logger) {
        this.plugin = plugin;
        this.mathManager = new MathManagerImpl();
        this.soundManager = new SoundManagerImpl();
        this.commandManager = new CommandManagerImpl(plugin, logger);
        this.keyManager = new NameSpacedKeyManagerImpl(plugin);
        this.textManager = new TextManagerImpl();
        this.colorManager = new ColorManagerImpl();
        this.loreManager = new LoreManagerImpl(this, keyManager);
        this.inventoryManager = new InventoryManagerImpl(this, logger);
        this.eventManager = new EventManagerImpl(plugin, this);
    }
}
