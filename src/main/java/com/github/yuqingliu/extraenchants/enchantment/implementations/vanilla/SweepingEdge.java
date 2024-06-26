package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class SweepingEdge extends VanillaEnchantment {
    public SweepingEdge(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.SWEEPING_EDGE,
            Component.text("Sweeping Edge", nameColor),
            Enchantment.SWEEPING_EDGE.getMaxLevel(),
            Component.text("Increases sweeping attack damage.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
