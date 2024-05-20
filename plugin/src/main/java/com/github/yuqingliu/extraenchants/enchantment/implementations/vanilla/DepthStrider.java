package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class DepthStrider extends VanillaEnchantment {
    public DepthStrider(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.DEPTH_STRIDER,
            Component.text("Depth Strider", nameColor),
            Enchantment.DEPTH_STRIDER.getMaxLevel(),
            Component.text("Increases underwater movement speed.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
