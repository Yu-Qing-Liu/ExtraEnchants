package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Looting extends VanillaEnchantment {
    public Looting(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.LOOT_BONUS_MOBS,
            Component.text("Looting", nameColor),
            Enchantment.LOOT_BONUS_MOBS.getMaxLevel(),
            Component.text("Increases amount of loot earned from mobs.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
