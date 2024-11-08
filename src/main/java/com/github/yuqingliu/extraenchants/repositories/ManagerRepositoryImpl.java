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
    public ManagerRepositoryImpl(
        JavaPlugin plugin,
        Logger logger,
        EventManager eventManager,
        CommandManager commandManager,
        InventoryManager inventoryManager,
        NameSpacedKeyManager keyManager,
        SoundManager soundManager,
        TextManager textManager,
        ColorManager colorManager,
        LoreManager loreManager,
        MathManager mathManager) {
        this.plugin = plugin;
        this.mathManager = mathManager;
        this.soundManager = soundManager;
        this.commandManager = commandManager;
        this.keyManager = keyManager;
        this.textManager = textManager;
        this.colorManager = colorManager;
        this.loreManager = loreManager;
        this.inventoryManager = inventoryManager;
        this.eventManager = eventManager;
    }
}

