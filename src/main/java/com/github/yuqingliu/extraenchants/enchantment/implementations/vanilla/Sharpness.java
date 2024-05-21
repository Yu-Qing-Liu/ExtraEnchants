package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Sharpness extends VanillaEnchantment {
    public Sharpness(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.SHARPNESS,
            Component.text("Sharpness", nameColor),
            Enchantment.SHARPNESS.getMaxLevel(),
            Component.text("Increases weapon damage.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
