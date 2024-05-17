package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Thorns extends VanillaEnchantment {
    public Thorns(TextColor nameColor) {
        super(
            Enchantment.THORNS,
            Component.text("Thorns", nameColor),
            Enchantment.THORNS.getMaxLevel(),
            Component.text(Enchantment.THORNS.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
