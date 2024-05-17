package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Infinity extends VanillaEnchantment {
    public Infinity(TextColor nameColor) {
        super(
            Enchantment.ARROW_INFINITE,
            Component.text("Infinity", nameColor),
            Enchantment.ARROW_INFINITE.getMaxLevel(),
            Component.text(Enchantment.ARROW_INFINITE.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
