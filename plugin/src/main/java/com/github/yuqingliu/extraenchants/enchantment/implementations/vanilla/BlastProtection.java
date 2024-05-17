package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class BlastProtection extends VanillaEnchantment {
    public BlastProtection(TextColor nameColor) {
        super(
            Enchantment.PROTECTION_EXPLOSIONS,
            Component.text("Blast Protection", nameColor),
            Enchantment.PROTECTION_EXPLOSIONS.getMaxLevel(),
            Component.text(Enchantment.PROTECTION_EXPLOSIONS.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
