package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class SwiftSneak extends VanillaEnchantment {
    public SwiftSneak(TextColor nameColor) {
        super(
            Enchantment.SWIFT_SNEAK,
            Component.text("Swift Sneak", nameColor),
            3,
            Component.text(Enchantment.SWIFT_SNEAK.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
