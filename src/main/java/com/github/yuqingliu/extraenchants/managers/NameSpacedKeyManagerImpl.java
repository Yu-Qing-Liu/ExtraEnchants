package com.github.yuqingliu.extraenchants.managers;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.enums.CustomEnchant;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.google.inject.Singleton;

import lombok.Getter;

@Singleton
@Getter
public class NameSpacedKeyManagerImpl implements NameSpacedKeyManager {
    private final JavaPlugin plugin;
    private final NamespacedKey loreKey;

    public NameSpacedKeyManagerImpl(JavaPlugin plugin) {
        this.plugin = plugin;
        loreKey = new NamespacedKey(plugin , "loreKey");
    }

    public NamespacedKey getEnchantKey(Enchantment enchant) {
        return new NamespacedKey(plugin, String.format("enchantKey-%s", enchant.getKey().getKey()));
    }

    public NamespacedKey getEnchantKey(CustomEnchant enchant) {
        return new NamespacedKey(plugin, String.format("enchantKey-%s", enchant.name()));
    }
}
