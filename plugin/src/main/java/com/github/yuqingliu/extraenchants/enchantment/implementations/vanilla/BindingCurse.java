package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class BindingCurse extends VanillaEnchantment {
    public BindingCurse(TextColor nameColor, TextColor descriptionColor) {
        super(
            Enchantment.BINDING_CURSE,
            Component.text("Curse of Binding", nameColor),
            Enchantment.BINDING_CURSE.getMaxLevel(),
            Component.text("Items cannot be removed from armor slots unless the cause is death or breaking.", descriptionColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
