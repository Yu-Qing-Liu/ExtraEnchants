package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class VanishingCurse extends VanillaEnchantment {
    public VanishingCurse(TextColor nameColor) {
        super(
            Enchantment.VANISHING_CURSE,
            Component.text("Curse of Vanishing", nameColor),
            Enchantment.VANISHING_CURSE.getMaxLevel(),
            Component.text("Item destroyed upon death.", nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
