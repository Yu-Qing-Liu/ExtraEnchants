package com.github.yuqingliu.extraenchants.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.logger.Logger;
import com.github.yuqingliu.extraenchants.api.managers.ConfigurationManager;
import com.github.yuqingliu.extraenchants.api.managers.EventManager;
import com.github.yuqingliu.extraenchants.api.managers.InventoryManager;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.api.repositories.CustomBlockRepository;
import com.github.yuqingliu.extraenchants.events.InventoryEvents;
import com.github.yuqingliu.extraenchants.events.WandEvents;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EventManagerImpl implements EventManager {
    private Map<String, Listener> listeners = new HashMap<>();
    private final JavaPlugin plugin;
    private final Logger logger;
    private final InventoryManager inventoryManager;
    private final CustomBlockRepository blockRepository;
    private final ConfigurationManager configurationManager;
    private final NameSpacedKeyManager nameSpacedKeyManager;

    @Inject
    public EventManagerImpl(
        JavaPlugin plugin,
        Logger logger,
        InventoryManager inventoryManager,
        CustomBlockRepository blockRepository,
        ConfigurationManager configurationManager,
        NameSpacedKeyManager nameSpacedKeyManager) {
        this.plugin = plugin;
        this.logger = logger;
        this.inventoryManager = inventoryManager;
        this.blockRepository = blockRepository;
        this.configurationManager = configurationManager;
        this.nameSpacedKeyManager = nameSpacedKeyManager;
    }

    @Inject
    public void postConstruct() {
        listeners.put(InventoryEvents.class.getSimpleName(), new InventoryEvents(inventoryManager, blockRepository, configurationManager));
        listeners.put(WandEvents.class.getSimpleName(), new WandEvents(logger, blockRepository, nameSpacedKeyManager, inventoryManager));
        registerEvents();
    }

    private void registerEvents() {
        for (Listener listener : listeners.values()) {
            registerEvent(listener);
        }
    }

    @Override
    public Listener getEvent(String className) {
        return listeners.get(className);
    }

    @Override
    public void registerEvent(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public void unregisterEvent(String className) {
        HandlerList.unregisterAll(listeners.get(className));
    }
}
