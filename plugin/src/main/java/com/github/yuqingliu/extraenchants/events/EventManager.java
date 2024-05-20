package com.github.yuqingliu.extraenchants.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Listener;

import com.github.yuqingliu.extraenchants.ExtraEnchants;

public class EventManager {
    private ExtraEnchants plugin;
    private List<Listener> listeners = new ArrayList<>();

    public EventManager(ExtraEnchants plugin) {
        this.plugin = plugin;
        listeners.add(new PlayerInteractsWithAnvil(plugin));
        listeners.add(new PlayerInteractsWithEnchantmentTable(plugin));
        listeners.add(new PlayerInteractsWithGrindstone(plugin));
        listeners.add(new PlayerPlacesDestroysAnvil());
        listeners.add(new PlayerPlacesDestroysEtable());
        listeners.add(new PlayerPlacesDestroysGrindstone());
    }

    public void registerListeners() {
        for(Listener listener : listeners) {
            plugin.getPluginManager().registerEvents(listener, plugin);
        }
    }
}
