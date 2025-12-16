package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.HashSet;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Density extends VanillaEnchantment {
    public Density(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, TextColor nameColor, TextColor descriptionColor) { 
        super(
            managerRepository, enchantmentRepository,
            EnchantID.DENSITY,
            Component.text("Density", nameColor),
            Component.text("Boosts the rate at which mace multiplies damage while falling.", descriptionColor),
            Enchantment.DENSITY.getMaxLevel(),
            new HashSet<>(),
            new HashSet<>(),
            "x^2",
            "x",
            Enchantment.DENSITY
        );
    }

    @Override
    public void postConstruct() {}
}
