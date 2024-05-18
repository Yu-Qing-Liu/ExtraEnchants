package com.github.yuqingliu.extraenchants.item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.ExtraEnchants;
import com.github.yuqingliu.extraenchants.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.enchantment.EnchantmentManager;

public class ItemUtils {
    private EnchantmentManager enchantmentManager;

    public ItemUtils(ExtraEnchants plugin) {
        this.enchantmentManager = plugin.getEnchantmentManager();
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
