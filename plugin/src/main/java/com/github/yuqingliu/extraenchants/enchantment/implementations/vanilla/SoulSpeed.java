package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class SoulSpeed extends VanillaEnchantment {
    public SoulSpeed(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.SOUL_SPEED,
            Component.text("Soul Speed", nameColor),
            Enchantment.SOUL_SPEED.getMaxLevel(),
            Component.text("Increases walking speed on soul sand and soul soil.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
