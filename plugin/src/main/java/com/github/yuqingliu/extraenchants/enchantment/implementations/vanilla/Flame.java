package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Flame extends VanillaEnchantment {
    public Flame(TextColor nameColor) {
        super(
            Enchantment.ARROW_FIRE,
            Component.text("Flame", nameColor),
            Enchantment.ARROW_FIRE.getMaxLevel(),
            Component.text(Enchantment.ARROW_FIRE.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
