package com.github.yuqingliu.extraenchants.enchantment.implementations.custom;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;

import com.github.yuqingliu.extraenchants.enchantment.implementations.CustomEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Homing extends CustomEnchantment {
    public Homing(TextColor nameColor, TextColor descriptionColor) {
        super(
            Component.text("Homing", nameColor),
            1,
            Component.text("Guided arrows", descriptionColor),
            Arrays.asList(Material.BOW),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
