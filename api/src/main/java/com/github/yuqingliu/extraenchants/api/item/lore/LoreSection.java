package com.github.yuqingliu.extraenchants.api.item.lore;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

public interface LoreSection {
    @NotNull int getPosition();

    @NotNull int getSize();

    @NotNull List<Component> getLore();
}
