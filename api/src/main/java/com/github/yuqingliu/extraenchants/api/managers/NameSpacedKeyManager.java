package com.github.yuqingliu.extraenchants.api.managers;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;

import org.bukkit.NamespacedKey;

public interface NameSpacedKeyManager {
    NamespacedKey getEnchantKey(EnchantID enchant);
    NamespacedKey getLoreKey();
}
