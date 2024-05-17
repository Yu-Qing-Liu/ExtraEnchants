package com.github.yuqingliu.extraenchants.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.Map;
import java.util.regex.Pattern;

public class TextUtils {
    private static final Map<Character, Integer> romanNumerals = Map.of(
        'I', 1,
        'V', 5,
        'X', 10,
        'L', 50,
        'C', 100,
        'D', 500,
        'M', 1000
    );

    public static String toRoman(int number) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanLiterals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                number -= values[i];
                roman.append(romanLiterals[i]);
            }
        }
        return roman.toString();
    }

    /**
    * Replaces parts of a component by string match, by a new component.
    *
    * @param component the component to be modified
    * @param match the string to perform match and replace with the replacement component
    * @param replaceComponent the component that will replace the strings matched by match
    * @return the modified component
    */
    public static Component replaceComponent(Component component, String match, Component replaceComponent) {
        Pattern pattern = Pattern.compile(match);
        return component.replaceText(builder -> builder.match(pattern).replacement(replaceComponent));
    }

    /**
    * Determines if a component contains a string
    *
    * @param component the component to be searched on
    * @param match the string to perform the search on
    * @return true if a match was found, false otherwise
    */
    public static boolean componentContains(Component component, String match) {
        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        return plainText.contains(match);
    }

    public static String componentToString(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
}
