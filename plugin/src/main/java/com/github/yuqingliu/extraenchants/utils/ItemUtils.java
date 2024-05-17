package com.github.yuqingliu.extraenchants.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.enchantment.EnchantmentManager;

public class ItemUtils {
    private EnchantmentManager enchantmentManager;

    public ItemUtils(EnchantmentManager enchantmentManager) {
        this.enchantmentManager = enchantmentManager;
    }

    public Map<Enchantment, Integer> getEnchantments(ItemStack item) {
        Collection<Enchantment> enchantsRegistry = enchantmentManager.getEnchantments().values();
        Map<Enchantment, Integer> itemEnchants = new HashMap<>();

        for (Enchantment enchantment : enchantsRegistry) {
            int level = enchantment.getEnchantmentLevel(item);
            if(level > 0) {
                itemEnchants.put(enchantment, level);
            }
        }
        return itemEnchants;
    }
}
