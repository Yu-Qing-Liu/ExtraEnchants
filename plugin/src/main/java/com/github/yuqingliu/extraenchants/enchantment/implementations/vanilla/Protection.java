package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Protection extends VanillaEnchantment {
    public Protection(TextColor nameColor) {
        super(
            Enchantment.PROTECTION_ENVIRONMENTAL,
            Component.text("Protection", nameColor),
            Enchantment.PROTECTION_ENVIRONMENTAL.getMaxLevel(),
            Component.text(Enchantment.PROTECTION_ENVIRONMENTAL.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
