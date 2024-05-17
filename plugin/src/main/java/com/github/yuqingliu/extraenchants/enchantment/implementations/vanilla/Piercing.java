package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Piercing extends VanillaEnchantment {
    public Piercing(TextColor nameColor) {
        super(
            Enchantment.PIERCING,
            Component.text("Piercing", nameColor),
            Enchantment.PIERCING.getMaxLevel(),
            Component.text(Enchantment.PIERCING.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
