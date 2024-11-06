package com.github.yuqingliu.extraenchants.managers;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.google.inject.Singleton;

import lombok.Getter;

@Singleton
@Getter
public class NameSpacedKeyManagerImpl implements NameSpacedKeyManager {
    private final NamespacedKey shopKey;
    private final NamespacedKey vendorKey;
    private final NamespacedKey bankKey;

    public NameSpacedKeyManagerImpl(JavaPlugin plugin) {
        shopKey = new NamespacedKey(plugin, "shopKey");
        vendorKey = new NamespacedKey(plugin, "vendorKey");
        bankKey = new NamespacedKey(plugin, "bankKey");
    }
}
