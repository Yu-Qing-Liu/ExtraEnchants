package com.github.yuqingliu.extraenchants.api.managers;

import java.util.List;

import net.kyori.adventure.text.format.TextColor;

public interface ColorManager {
    List<TextColor> generateMonochromaticGradient(TextColor baseColor, int maxLevel);
}
