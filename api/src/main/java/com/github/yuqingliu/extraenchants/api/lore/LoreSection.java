package com.github.yuqingliu.extraenchants.api.lore;

import java.util.List;

import net.kyori.adventure.text.Component;

public interface LoreSection {
    String getName();
    int getPosition();
    int getSize();
    List<Component> getLore();
}
