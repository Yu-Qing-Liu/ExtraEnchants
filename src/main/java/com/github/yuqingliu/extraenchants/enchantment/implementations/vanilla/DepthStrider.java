package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.HashSet;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class DepthStrider extends VanillaEnchantment {
    public DepthStrider(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.DEPTH_STRIDER,
            Component.text("Depth Strider", nameColor),
            Component.text("Increases underwater movement speed.", descriptionColor),
            Enchantment.DEPTH_STRIDER.getMaxLevel(),
            new HashSet<>(),
            new HashSet<>(),
            "x^2",
            "x",
            Enchantment.DEPTH_STRIDER
        );
    }

    @Override
    public void postConstruct() {}
}
