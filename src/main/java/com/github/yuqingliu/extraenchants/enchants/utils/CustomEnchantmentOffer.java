package com.github.yuqingliu.extraenchants.enchants.utils;

public class CustomEnchantmentOffer {
    private int cost;
    private CustomEnchantment enchant;
    private int enchantmentLevel;
    
    public CustomEnchantmentOffer(CustomEnchantment enchant, int enchantmentLevel, int cost) {
        this.cost = cost;
        this.enchant = enchant;
        this.enchantmentLevel = enchantmentLevel;
    }
    
    public int getCost() {
        return this.cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public CustomEnchantment getEnchant() {
        return this.enchant;
    }

    public void setEnchant(CustomEnchantment enchant) {
        this.enchant = enchant;
    }

    public int getEnchantmentLevel() {
        return this.enchantmentLevel;
    }

    public void setEnchantmentLevel(int enchantmentLevel) {
        this.enchantmentLevel = enchantmentLevel;
    }
}

