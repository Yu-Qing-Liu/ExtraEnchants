package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class LuckOfTheSea extends VanillaEnchantment {
    public LuckOfTheSea(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.LUCK_OF_THE_SEA,
            Component.text("Luck of the Sea", nameColor),
            Enchantment.LUCK_OF_THE_SEA.getMaxLevel(),
            Component.text("Increases rate of fishing rare loot.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
