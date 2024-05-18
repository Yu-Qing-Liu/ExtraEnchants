package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Multishot extends VanillaEnchantment {
    public Multishot(TextColor nameColor) {
        super(
            Enchantment.MULTISHOT,
            Component.text("Multishot", nameColor),
            Enchantment.MULTISHOT.getMaxLevel(),
            Component.text("Shoot 3 arrows at the cost of one; only one arrow can be recovered.", nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
