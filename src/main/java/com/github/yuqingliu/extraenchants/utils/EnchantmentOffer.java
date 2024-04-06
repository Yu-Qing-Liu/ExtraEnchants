package com.github.yuqingliu.extraenchants.utils;

public class EnchantmentOffer {
    private final Enchantment enchantment;
    private final int level;
    private final int experienceCost;

    public EnchantmentOffer(Enchantment enchantment, int level, int experienceCost) {
        this.enchantment = enchantment;
        this.level = level;
        this.experienceCost = experienceCost;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getEnchantmentLevel() {
        return level;
    }

    public int getExperienceCost() {
        return experienceCost;
    }
}

