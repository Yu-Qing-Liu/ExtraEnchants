package com.github.yuqingliu.extraenchants.api.managers;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.api.enums.CustomEnchant;

public interface NameSpacedKeyManager {
    NamespacedKey getEnchantKey(Enchantment enchant);
    NamespacedKey getEnchantKey(CustomEnchant enchant);
    NamespacedKey getLoreKey();
}
