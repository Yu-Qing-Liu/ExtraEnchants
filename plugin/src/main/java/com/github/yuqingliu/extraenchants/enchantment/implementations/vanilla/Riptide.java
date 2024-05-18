package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Riptide extends VanillaEnchantment {
    public Riptide(TextColor nameColor) {
        super(
            Enchantment.RIPTIDE,
            Component.text("Riptide", nameColor),
            Enchantment.RIPTIDE.getMaxLevel(),
            Component.text("Trident launches player with itself when thrown. Functions only in water or rain.", nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
