package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class BaneOfArthropods extends VanillaEnchantment {
    public BaneOfArthropods(TextColor nameColor) {
        super(
            Enchantment.DAMAGE_ARTHROPODS,
            Component.text("Bane of Arthropods", nameColor),
            Enchantment.DAMAGE_ARTHROPODS.getMaxLevel(),
            Component.text(Enchantment.DAMAGE_ARTHROPODS.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
