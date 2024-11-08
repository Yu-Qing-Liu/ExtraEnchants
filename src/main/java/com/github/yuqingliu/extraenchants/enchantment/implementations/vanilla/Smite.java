package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.HashSet;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Smite extends VanillaEnchantment {
    public Smite(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.SMITE,
            Component.text("Smite", nameColor),
            Component.text("Increases damage to undead mobs.", descriptionColor),
            Enchantment.SMITE.getMaxLevel(),
            new HashSet<>(),
            new HashSet<>(),
            "x^2",
            "x",
            Enchantment.SMITE
        );
    }

    @Override
    public void postConstruct() {}
}