package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Unbreaking extends VanillaEnchantment {
    public Unbreaking(TextColor nameColor) {
        super(
            Enchantment.DURABILITY,
            Component.text("Unbreaking", nameColor),
            Enchantment.DURABILITY.getMaxLevel(),
            Component.text(Enchantment.DURABILITY.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
