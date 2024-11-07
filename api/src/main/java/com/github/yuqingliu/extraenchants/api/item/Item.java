package com.github.yuqingliu.extraenchants.api.item;

import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;

public interface Item {
    ItemStack getItemStack();
    Map<Enchantment, Integer> getEnchantments(EnchantmentRepository enchantmentRepository);
}

