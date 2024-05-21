package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Unbreaking extends VanillaEnchantment {
    public Unbreaking(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.UNBREAKING,
            Component.text("Unbreaking", nameColor),
            Enchantment.UNBREAKING.getMaxLevel(),
            Component.text("Increases item durability.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
