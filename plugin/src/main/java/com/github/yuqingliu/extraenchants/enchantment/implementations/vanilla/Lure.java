package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Lure extends VanillaEnchantment {
    public Lure(TextColor nameColor) {
        super(
            Enchantment.LURE,
            Component.text("Lure", nameColor),
            Enchantment.LURE.getMaxLevel(),
            Component.text(Enchantment.LURE.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
