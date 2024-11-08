package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.enchantments.Enchantment;
import org.checkerframework.checker.units.qual.m;

import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class BindingCurse extends VanillaEnchantment {
    public BindingCurse(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, TextColor nameColor, TextColor descriptionColor) {
        super(
            managerRepository, enchantmentRepository,
            EnchantID.BINDING_CURSE,
            Component.text("Curse of Binding", nameColor),
            Component.text("Items cannot be removed from armor slots unless the cause is death or breaking.", descriptionColor),
            Enchantment.BINDING_CURSE.getMaxLevel(),
            new HashSet<>(),
            new HashSet<>(),
            "x^2",
            "x",
            Enchantment.BINDING_CURSE
        );
    }

    @Override
    public void postConstruct() {}
}
