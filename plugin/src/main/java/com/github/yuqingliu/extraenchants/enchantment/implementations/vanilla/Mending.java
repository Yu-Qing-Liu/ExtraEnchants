package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Mending extends VanillaEnchantment {
    public Mending(TextColor nameColor) {
        super(
            Enchantment.MENDING,
            Component.text("Mending", nameColor),
            Enchantment.MENDING.getMaxLevel(),
            Component.text(Enchantment.MENDING.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
