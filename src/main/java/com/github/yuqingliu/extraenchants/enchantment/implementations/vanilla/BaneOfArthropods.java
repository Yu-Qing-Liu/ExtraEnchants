package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.HashSet;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class BaneOfArthropods extends VanillaEnchantment {
    public BaneOfArthropods(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.BANE_OF_ARTHROPODS,
            Component.text("Bane of Arthropods", nameColor),
            Component.text("Increases damage and applies Slowness IV to arthropod mobs", descriptionColor),
            Enchantment.BANE_OF_ARTHROPODS.getMaxLevel(),
            new HashSet<>(),
            new HashSet<>(),
            "x^2",
            "x",
            Enchantment.BANE_OF_ARTHROPODS
        );
    }

    @Override
    public void postConstruct() {};
}
