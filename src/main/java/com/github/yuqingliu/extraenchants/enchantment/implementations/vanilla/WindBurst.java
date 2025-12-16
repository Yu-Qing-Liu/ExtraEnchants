package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.HashSet;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class WindBurst extends VanillaEnchantment {
    public WindBurst(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, TextColor nameColor, TextColor descriptionColor) { 
        super(
            managerRepository, enchantmentRepository,
            EnchantID.WIND_BURST,
            Component.text("Wind Burst", nameColor),
            Component.text("Allows the player to bounce up into the air following a successful hit; the height of this bounce increases with higher enchantment levels.", descriptionColor),
            Enchantment.WIND_BURST.getMaxLevel(),
            new HashSet<>(),
            new HashSet<>(),
            "x^2",
            "x",
            Enchantment.WIND_BURST
        );
    }

    @Override
    public void postConstruct() {}
}
