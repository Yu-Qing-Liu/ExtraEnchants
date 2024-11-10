package com.github.yuqingliu.extraenchants.managers;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import lombok.Getter;

@Singleton
@Getter
public class NameSpacedKeyManagerImpl implements NameSpacedKeyManager {
    private final JavaPlugin plugin;
    private final NamespacedKey loreKey;
    private final NamespacedKey customUIKey;
    
    @Inject
    public NameSpacedKeyManagerImpl(JavaPlugin plugin) {
        this.plugin = plugin;
        loreKey = new NamespacedKey(plugin , "loreKey");
        customUIKey = new NamespacedKey(plugin, "customUIKey");
    }

    public NamespacedKey getEnchantKey(EnchantID enchant) {
        return new NamespacedKey(plugin, String.format("enchantKey-%s", enchant.name()));
    }
}
