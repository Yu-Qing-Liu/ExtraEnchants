package com.github.yuqingliu.extraenchants.api.item;

import org.bukkit.Material;

import net.kyori.adventure.text.Component;

public interface Item {
    Material getMaterial();
    Component getDisplayName();
}

