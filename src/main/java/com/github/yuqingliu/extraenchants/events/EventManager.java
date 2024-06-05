package com.github.yuqingliu.extraenchants.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Listener;

import com.github.yuqingliu.extraenchants.ExtraEnchantsImpl;

public class EventManager {
    private ExtraEnchantsImpl plugin;
    private List<Listener> listeners = new ArrayList<>();

    public EventManager(ExtraEnchantsImpl plugin) {
        this.plugin = plugin;
        initializeListeners();
        registerListeners();
    }

    private void initializeListeners() {
        listeners.add(new PlayerInteractsWithAnvil(plugin));
        listeners.add(new PlayerInteractsWithEnchantmentTable(plugin));
        listeners.add(new PlayerInteractsWithGrindstone(plugin));
        listeners.add(new PlayerPlacesDestroysAnvil());
        listeners.add(new PlayerPlacesDestroysEtable());
        listeners.add(new PlayerPlacesDestroysGrindstone());
        listeners.add(new PlayerReceivesItem(plugin));
    }

    private void registerListeners() {
        for(Listener listener : listeners) {
            plugin.getPluginManager().registerEvents(listener, plugin);
        }
    }
}
