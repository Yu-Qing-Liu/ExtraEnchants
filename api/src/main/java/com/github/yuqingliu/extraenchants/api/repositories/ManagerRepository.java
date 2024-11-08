package com.github.yuqingliu.extraenchants.api.repositories;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.managers.*;

public interface ManagerRepository {
    JavaPlugin getPlugin();
    EventManager getEventManager();
    CommandManager getCommandManager();
    InventoryManager getInventoryManager();
    NameSpacedKeyManager getKeyManager();
    SoundManager getSoundManager();
    TextManager getTextManager();
    ColorManager getColorManager();
    LoreManager getLoreManager();
    MathManager getMathManager();
}
