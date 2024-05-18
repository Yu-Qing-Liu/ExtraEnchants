package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Efficiency extends VanillaEnchantment {
    public Efficiency(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.DIG_SPEED,
            Component.text("Efficiency", nameColor),
            Enchantment.DIG_SPEED.getMaxLevel(),
            Component.text("When applied to an axe it increases the chance that the axe may stun a shield.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
