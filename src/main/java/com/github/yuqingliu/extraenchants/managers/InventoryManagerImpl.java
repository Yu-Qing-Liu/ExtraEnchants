package com.github.yuqingliu.extraenchants.managers;

import java.util.HashMap;
import java.util.Map;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.EventManager;
import com.github.yuqingliu.extraenchants.api.managers.InventoryManager;
import com.github.yuqingliu.extraenchants.api.managers.SoundManager;
import com.github.yuqingliu.extraenchants.api.view.PlayerInventory;
import com.github.yuqingliu.extraenchants.view.AbstractPlayerInventory;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class InventoryManagerImpl implements InventoryManager {
    private final EventManager eventManager;
    private final SoundManager soundManager;
    private final Logger logger;
    private Map<String, AbstractPlayerInventory> inventories = new HashMap<>();
    
    @Inject
    public InventoryManagerImpl(EventManager eventManager, SoundManager soundManager, Logger logger) {
        this.eventManager = eventManager;
        this.soundManager = soundManager;
        this.logger = logger;
        initializeInventories();
    }

    private void initializeInventories() {
        
    }

    @Override
    public PlayerInventory getInventory(String className) {
        return inventories.get(className);
    }
}
