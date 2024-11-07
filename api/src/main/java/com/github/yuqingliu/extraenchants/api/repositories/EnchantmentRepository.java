package com.github.yuqingliu.extraenchants.api.repositories;

import java.util.Set;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;

public interface EnchantmentRepository {
    Set<Enchantment> getEnchantments();
    Enchantment[] getApplicableEnchantments(ItemStack item);
    Enchantment getEnchantment(String enchantmentName);
}
