package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Power extends VanillaEnchantment {
    public Power(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.POWER,
            Component.text("Power", nameColor),
            Enchantment.POWER.getMaxLevel(),
            Component.text("Increases arrow damage.", descriptionColor),
            Arrays.asList(Material.CROSSBOW),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
