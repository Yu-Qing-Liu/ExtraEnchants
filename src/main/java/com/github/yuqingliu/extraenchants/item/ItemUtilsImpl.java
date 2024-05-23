package com.github.yuqingliu.extraenchants.item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.item.ItemUtils;

import com.github.yuqingliu.extraenchants.ExtraEnchantsImpl;
import com.github.yuqingliu.extraenchants.enchantment.EnchantmentManagerImpl;

public class ItemUtilsImpl implements ItemUtils {
    private EnchantmentManagerImpl enchantmentManager;

    public ItemUtilsImpl(ExtraEnchantsImpl plugin) {
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
