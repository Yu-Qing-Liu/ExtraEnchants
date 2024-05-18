package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Channeling extends VanillaEnchantment {
    public Channeling(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.CHANNELING,
            Component.text("Channeling", nameColor),
            Enchantment.CHANNELING.getMaxLevel(),
            Component.text("Trident channels a bolt of lightning toward a hit entity. Functions only during thunderstorms.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
