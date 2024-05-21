package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Protection extends VanillaEnchantment {
    public Protection(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.PROTECTION,
            Component.text("Protection", nameColor),
            Enchantment.PROTECTION.getMaxLevel(),
            Component.text("Reduces most types of damage by 4% per level.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
