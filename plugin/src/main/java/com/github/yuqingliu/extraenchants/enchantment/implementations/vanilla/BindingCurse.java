package com.github.yuqingliu.extraenchants.enchantment.implementations.vanilla;

import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;

import com.github.yuqingliu.extraenchants.enchantment.implementations.VanillaEnchantment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class BindingCurse extends VanillaEnchantment {
    public BindingCurse(TextColor nameColor) {
        super(
            Enchantment.BINDING_CURSE,
            Component.text("Curse of Binding", nameColor),
            Enchantment.BINDING_CURSE.getMaxLevel(),
            Component.text(Enchantment.BINDING_CURSE.getKey().getKey(), nameColor),
            new ArrayList<>(),
            new ArrayList<>(),
            "x^2",
            "x"
        );
    }
}
