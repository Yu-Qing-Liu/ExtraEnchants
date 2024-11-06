package com.github.yuqingliu.extraenchants.managers;

import java.util.Map;
import java.util.regex.Pattern;

import com.github.yuqingliu.extraenchants.api.managers.TextManager;
import com.google.inject.Singleton;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

@Singleton
public class TextManagerImpl implements TextManager {
    private final Map<Character, Integer> romanNumerals = Map.of(
        'I', 1,
        'V', 5,
        'X', 10,
        'L', 50,
        'C', 100,
        'D', 500,
        'M', 1000
    );

    @Override
    public String toRoman(int number) {
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

    @Override
    public int fromRoman(String roman) {
        int result = 0;
        int prevValue = 0;
        for (int i = roman.length() - 1; i >= 0; i--) {
            char ch = roman.charAt(i);
            Integer value = romanNumerals.get(ch);
            if (value != null) {
                if (value < prevValue) {
                    result -= value;
                } else {
                    result += value;
                }
                prevValue = value;
            } else {
                return 0;
            }
        }
        return result;
    }

    @Override
    public Component replaceComponent(Component component, String match, Component replaceComponent) {
        Pattern pattern = Pattern.compile(match);
        return component.replaceText(builder -> builder.match(pattern).replacement(replaceComponent));
    }

    @Override
    public boolean componentContains(Component component, String match) {
        String plainText = PlainTextComponentSerializer.plainText().serialize(component);
        return plainText.contains(match);
    }

    @Override
    public boolean componentStringEquals(Component expected, Component result) {
        String exp = componentToString(expected);
        String res = componentToString(result);
        return exp.equals(res);
    }
    
    @Override
    public String componentToString(Component component) {
        if(component == null) {
            return "";
        }
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
    
    @Override
    public String componentToJson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }
    
    @Override
    public Component jsonToComponent(String json) {
        return GsonComponentSerializer.gson().deserialize(json);
    }
    
    @Override
    public String formatName(String name) {
        String result = name.replace("_", " ").toLowerCase();
        String[] words = result.split(" ");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                formattedName.append(Character.toUpperCase(word.charAt(0)))
                             .append(word.substring(1))
                             .append(" ");
            }
        }
        return formattedName.toString().trim();
    }
}
