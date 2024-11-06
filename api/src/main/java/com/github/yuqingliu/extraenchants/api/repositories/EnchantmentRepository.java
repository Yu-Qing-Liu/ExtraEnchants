package com.github.yuqingliu.extraenchants.api.repositories;

import java.util.Map;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;

public interface EnchantmentRepository {
    Map<String, Enchantment> getEnchantments();   
}
