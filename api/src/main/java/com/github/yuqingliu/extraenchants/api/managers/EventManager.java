package com.github.yuqingliu.extraenchants.api.managers;

import org.bukkit.event.Listener;

import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;

public interface EventManager {
    void registerEvent(Listener listener);
    void unregisterEvent(String className);
    Listener getEvent(String className);
    void postConstruct(ManagerRepository managerRepository);
}
