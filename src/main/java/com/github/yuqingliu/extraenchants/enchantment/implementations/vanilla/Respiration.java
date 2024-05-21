package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Respiration extends VanillaEnchantment {
    public Respiration(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.RESPIRATION,
            Component.text("Respiration", nameColor),
            Enchantment.RESPIRATION.getMaxLevel(),
            Component.text("Extends underwater breathing time.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
