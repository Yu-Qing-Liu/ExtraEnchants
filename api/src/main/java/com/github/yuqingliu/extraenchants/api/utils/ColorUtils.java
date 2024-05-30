package com.github.yuqingliu.extraenchants.api.utils;

import java.util.ArrayList;
import java.util.List;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;

public class ColorUtils {
    public static List<TextColor> generateMonochromaticGradient(TextColor color, int numColors) {
        List<TextColor> gradientColors = new ArrayList<>();

        HSVLike initialHsv = color.asHSV();
        
        float finalH = initialHsv.h() + 0.06f;
        float finalS = initialHsv.h() + 0.03f;
        float finalV = initialHsv.h() + 0.03f;
        float stepSizeH = (finalH - initialHsv.h()) / (float) numColors;
        float stepSizeS = (finalS - initialHsv.s()) / (float) numColors;
        float stepSizeV = (finalV - initialHsv.v()) / (float) numColors;

        for (int i = 0; i < numColors; i++) {
            float newH = initialHsv.h() + stepSizeH * i;
            float newS = initialHsv.s() + stepSizeS * i;
            float newV = initialHsv.v() + stepSizeV * i;
            if(i != 0 && i == numColors - 1) {
                newH = finalH;
            } 
            HSVLike darkerHSV = HSVLike.hsvLike(newH, newS, newV);
            TextColor darkerColor = TextColor.color(darkerHSV);
            gradientColors.add(darkerColor);
        }

        return gradientColors;
    }
}
