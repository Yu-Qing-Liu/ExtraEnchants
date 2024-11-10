package com.github.yuqingliu.extraenchants.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.managers.ConfigurationManager;
import com.github.yuqingliu.extraenchants.api.managers.EventManager;
import com.github.yuqingliu.extraenchants.api.managers.InventoryManager;
import com.github.yuqingliu.extraenchants.api.repositories.CustomBlockRepository;
import com.github.yuqingliu.extraenchants.events.PlayerInteractsWithAnvil;
import com.github.yuqingliu.extraenchants.events.PlayerInteractsWithEnchantingTable;
import com.github.yuqingliu.extraenchants.events.PlayerInteractsWithGrindstone;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EventManagerImpl implements EventManager {
    private Map<String, Listener> listeners = new HashMap<>();
    private final JavaPlugin plugin;
    private final InventoryManager inventoryManager;
    private final CustomBlockRepository blockRepository;
    private final ConfigurationManager configurationManager;
    
    @Inject
    public EventManagerImpl(JavaPlugin plugin, InventoryManager inventoryManager, CustomBlockRepository blockRepository, ConfigurationManager configurationManager) {
        this.plugin = plugin;
        this.inventoryManager = inventoryManager;
        this.blockRepository = blockRepository;
        this.configurationManager = configurationManager;
    }
    
    @Inject
    public void postConstruct() {
        listeners.put(PlayerInteractsWithEnchantingTable.class.getSimpleName(), new PlayerInteractsWithEnchantingTable(inventoryManager, blockRepository, configurationManager));
        listeners.put(PlayerInteractsWithAnvil.class.getSimpleName(), new PlayerInteractsWithAnvil(inventoryManager, blockRepository, configurationManager));
        listeners.put(PlayerInteractsWithGrindstone.class.getSimpleName(), new PlayerInteractsWithGrindstone(inventoryManager, blockRepository, configurationManager));
        registerEvents();
    }

    private void registerEvents() {
        for(Listener listener : listeners.values()) {
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
        HandlerList.unregisterAll(listeners.get(className));;
    }
}
