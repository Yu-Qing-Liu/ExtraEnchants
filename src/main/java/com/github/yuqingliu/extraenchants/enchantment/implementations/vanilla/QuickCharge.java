package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class QuickCharge extends VanillaEnchantment {
    public QuickCharge(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.QUICK_CHARGE,
            Component.text("QuickCharge", nameColor),
            Enchantment.QUICK_CHARGE.getMaxLevel(),
            Component.text("Decreases crossbow charging time.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
