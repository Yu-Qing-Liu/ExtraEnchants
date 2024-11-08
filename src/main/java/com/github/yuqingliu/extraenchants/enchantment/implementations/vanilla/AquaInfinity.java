package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class AquaInfinity extends VanillaEnchantment {
    public AquaInfinity(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, TextColor nameColor, TextColor descriptionColor) { 
        super(
            managerRepository, enchantmentRepository,
            EnchantID.AQUA_INFINITY,
            Component.text("Aqua Infinity", nameColor),
            Component.text("Increases underwater mining speed."),
            Enchantment.AQUA_AFFINITY.getMaxLevel(),
            new HashSet<>(),
            new HashSet<>(),
            "x^2",
            "x",
            Enchantment.AQUA_AFFINITY
        );
    }

    @Override
    public void postConstruct() {}
}
