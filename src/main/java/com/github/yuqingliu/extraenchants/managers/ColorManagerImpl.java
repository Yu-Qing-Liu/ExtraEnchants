package com.github.yuqingliu.extraenchants.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.yuqingliu.extraenchants.api.managers.ColorManager;
import com.google.inject.Singleton;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;

@Singleton
public class ColorManagerImpl implements ColorManager {
    @Override
    public List<TextColor> generateMonochromaticGradient(TextColor color, int numColors) {
        List<TextColor> gradientColors = new ArrayList<>();
        HSVLike initialHsv = color.asHSV();
        // Final saturation and value to reach in the gradient (lightest, least saturated)
        float minS = 0.2f;  // Minimum saturation (least vibrant)
        float finalS = (float) Math.max(minS, initialHsv.s() - ((numColors - 1) * 0.15));
        float maxV = 1.0f;  // Maximum value (brightest)
        float finalV = (float) Math.min(maxV, initialHsv.v() + (numColors - 1) * 0.15);
        // Calculate the step sizes for saturation and brightness (value)
        float stepSizeS = (initialHsv.s() - finalS) / (float) numColors;  // Decrease saturation
        float stepSizeV = (finalV - initialHsv.v()) / (float) numColors;  // Increase value (brightness)
        // Generate the gradient by adjusting saturation and value
        for (int i = 0; i < numColors; i++) {
            // Calculate new saturation and value
            float newS = initialHsv.s() - stepSizeS * i;
            float newV = initialHsv.v() + stepSizeV * i;
            // Ensure that we reach the final saturation and value at the last step
            if (i == numColors - 1) {
                newS = finalS;
                newV = finalV;
            }
            // Create the new color with the same hue, but adjusted saturation and value
            HSVLike newHsv = HSVLike.hsvLike(initialHsv.h(), newS, newV);
            TextColor newColor = TextColor.color(newHsv);
            gradientColors.add(newColor);
        }
        Collections.reverse(gradientColors);
        return gradientColors;
    }
}
