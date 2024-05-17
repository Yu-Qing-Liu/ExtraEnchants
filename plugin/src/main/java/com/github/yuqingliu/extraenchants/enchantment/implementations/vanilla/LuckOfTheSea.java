package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class LuckOfTheSea extends VanillaEnchantment {
    public LuckOfTheSea(TextColor nameColor) {
        super(
            Enchantment.LUCK,
            Component.text("Luck of the Sea", nameColor),
            Enchantment.LUCK.getMaxLevel(),
            Component.text(Enchantment.LUCK.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
