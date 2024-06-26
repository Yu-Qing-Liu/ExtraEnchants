package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class FireProtection extends VanillaEnchantment {
    public FireProtection(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.FIRE_PROTECTION,
            Component.text("Fire Protection", nameColor),
            Enchantment.FIRE_PROTECTION.getMaxLevel(),
            Component.text("Reduces fire damage and burn time.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
