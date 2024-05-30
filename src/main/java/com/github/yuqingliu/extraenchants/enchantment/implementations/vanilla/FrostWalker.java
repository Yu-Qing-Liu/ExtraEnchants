package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class FrostWalker extends VanillaEnchantment {
    public FrostWalker(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.FROST_WALKER,
            Component.text("Frost Walker", nameColor),
            Enchantment.FROST_WALKER.getMaxLevel(),
            Component.text("Changes the water source blocks beneath the player into frosted ice.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
