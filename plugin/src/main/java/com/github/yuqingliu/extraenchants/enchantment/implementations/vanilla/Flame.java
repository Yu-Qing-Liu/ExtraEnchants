package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Flame extends VanillaEnchantment {
    public Flame(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.ARROW_FIRE,
            Component.text("Flame", nameColor),
            Enchantment.ARROW_FIRE.getMaxLevel(),
            Component.text("Arrows set targets on fire.", descriptionColor),
            Arrays.asList(Material.CROSSBOW),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
