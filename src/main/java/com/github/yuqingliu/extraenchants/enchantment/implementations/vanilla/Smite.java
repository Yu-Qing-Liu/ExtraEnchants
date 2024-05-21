package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Smite extends VanillaEnchantment {
    public Smite(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.SMITE,
            Component.text("Smite", nameColor),
            Enchantment.SMITE.getMaxLevel(),
            Component.text("Increases damage to undead mobs.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
