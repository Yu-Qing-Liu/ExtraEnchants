package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Knockback extends VanillaEnchantment {
    public Knockback(TextColor nameColor) {
        super(
            Enchantment.KNOCKBACK,
            Component.text("Knockback", nameColor),
            Enchantment.KNOCKBACK.getMaxLevel(),
            Component.text(Enchantment.KNOCKBACK.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
