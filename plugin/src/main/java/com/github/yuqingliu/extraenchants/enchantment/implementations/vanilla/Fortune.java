package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Fortune extends VanillaEnchantment {
    public Fortune(TextColor nameColor) {
        super(
            Enchantment.LOOT_BONUS_BLOCKS,
            Component.text("Fortune", nameColor),
            Enchantment.LOOT_BONUS_BLOCKS.getMaxLevel(),
            Component.text("Increases certain item drop chances from blocks.", nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
