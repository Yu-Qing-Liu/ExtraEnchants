package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class FrostWalker extends VanillaEnchantment {
    public FrostWalker(TextColor nameColor) {
        super(
            Enchantment.FROST_WALKER,
            Component.text("Frost Walker", nameColor),
            Enchantment.FROST_WALKER.getMaxLevel(),
            Component.text(Enchantment.FROST_WALKER.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
