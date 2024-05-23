package com.github.yuqingliu.extraenchants.api.enchantment;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public interface EnchantmentManager {
    /**
     * Returns all default enchantments registered
     * @return scheduled task.
     */
    @NotNull Map<String, Enchantment> getEnchantments();   
}
