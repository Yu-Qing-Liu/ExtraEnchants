package com.github.yuqingliu.extraenchants.api.repositories;

import com.github.yuqingliu.extraenchants.api.managers.*;

public interface ManagerRepository {
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
