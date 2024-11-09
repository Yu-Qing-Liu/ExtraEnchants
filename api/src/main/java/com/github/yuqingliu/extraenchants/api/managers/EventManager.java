package com.github.yuqingliu.extraenchants.api.managers;

import org.bukkit.event.Listener;

public interface EventManager {
    void registerEvent(Listener listener);
    void unregisterEvent(String className);
    Listener getEvent(String className);
}
