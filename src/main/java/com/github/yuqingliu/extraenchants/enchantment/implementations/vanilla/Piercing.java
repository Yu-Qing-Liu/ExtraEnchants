package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Piercing extends VanillaEnchantment {
    public Piercing(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.PIERCING,
            Component.text("Piercing", nameColor),
            Enchantment.PIERCING.getMaxLevel(),
            Component.text("Arrows pass through multiple entities.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
