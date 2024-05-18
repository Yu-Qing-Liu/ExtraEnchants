package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Loyalty extends VanillaEnchantment {
    public Loyalty(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.LOYALTY,
            Component.text("Loyalty", nameColor),
            Enchantment.LOYALTY.getMaxLevel(),
            Component.text("Trident returns after being thrown.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
