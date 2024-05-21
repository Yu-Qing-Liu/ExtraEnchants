package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class FeatherFalling extends VanillaEnchantment {
    public FeatherFalling(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.FEATHER_FALLING,
            Component.text("Feather Falling", nameColor),
            Enchantment.FEATHER_FALLING.getMaxLevel(),
            Component.text("Reduces fall damage.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
