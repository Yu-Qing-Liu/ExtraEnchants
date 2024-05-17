package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class SilkTouch extends VanillaEnchantment {
    public SilkTouch(TextColor nameColor) {
        super(
            Enchantment.SILK_TOUCH,
            Component.text("Silk Touch", nameColor),
            Enchantment.SILK_TOUCH.getMaxLevel(),
            Component.text(Enchantment.SILK_TOUCH.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
