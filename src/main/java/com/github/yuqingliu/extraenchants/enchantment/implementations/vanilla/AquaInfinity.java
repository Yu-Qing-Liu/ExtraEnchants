package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class AquaInfinity extends VanillaEnchantment {
    public AquaInfinity(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.AQUA_AFFINITY,
            Component.text("Aqua Infinity", nameColor),
            Enchantment.AQUA_AFFINITY.getMaxLevel(),
            Component.text("Increase the rate of underwater mining speed.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
