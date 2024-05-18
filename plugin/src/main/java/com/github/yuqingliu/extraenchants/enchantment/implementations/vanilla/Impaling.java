package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Impaling extends VanillaEnchantment {
    public Impaling(TextColor nameColor) {
        super(
            Enchantment.IMPALING,
            Component.text("Impaling", nameColor),
            Enchantment.IMPALING.getMaxLevel(),
            Component.text("Trident deals additional damage to mobs that spawn naturally in the ocean.", nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
