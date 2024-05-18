package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Punch extends VanillaEnchantment {
    public Punch(TextColor nameColor) {
        super(
            Enchantment.ARROW_KNOCKBACK,
            Component.text("Punch", nameColor),
            Enchantment.ARROW_KNOCKBACK.getMaxLevel(),
            Component.text("Increases arrow knockback.", nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
