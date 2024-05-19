package com.github.yuqingliu.extraenchants.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.enchantments.Enchantment;

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

    public static int fromRoman(String roman) {
        int result = 0;
        int prevValue = 0;
        for (int i = roman.length() - 1; i >= 0; i--) {
            char ch = roman.charAt(i);
            Integer value = romanNumerals.get(ch);
            if (value != null) {
                if (value < prevValue) {
                    // Subtract this value for subtractive notation
                    result -= value;
                } else {
                    result += value;
                }
                prevValue = value;
            } else {
                // Handle the case where the map returns null due to an unexpected character
                return 0;
            }
        }
        return result;
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

    public static String componentToJson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }

    public static Component jsonToComponent(String json) {
        return GsonComponentSerializer.gson().deserialize(json);
    }

    public static Enchantment getVanillaEnchantment(String key) {
        return switch(key) {
            case "AquaInfinity" -> Enchantment.WATER_WORKER;
            case "BaneOfArthropods" -> Enchantment.DAMAGE_ARTHROPODS;
            case "BindingCurse" -> Enchantment.BINDING_CURSE;
            case "BlastProtection" -> Enchantment.PROTECTION_EXPLOSIONS;
            case "Channeling" -> Enchantment.CHANNELING;
            case "DepthStrider" -> Enchantment.DEPTH_STRIDER;
            case "Efficiency" -> Enchantment.DIG_SPEED;
            case "FeatherFalling" -> Enchantment.PROTECTION_FALL;
            case "FireAspect" -> Enchantment.FIRE_ASPECT;
            case "FireProtection" -> Enchantment.PROTECTION_FIRE;
            case "Flame" -> Enchantment.ARROW_FIRE;
            case "Fortune" -> Enchantment.LOOT_BONUS_BLOCKS;
            case "FrostWalker" -> Enchantment.FROST_WALKER;
            case "Impaling" -> Enchantment.IMPALING;
            case "Infinity" -> Enchantment.ARROW_INFINITE;
            case "Knockback" -> Enchantment.KNOCKBACK;
            case "Looting" -> Enchantment.LOOT_BONUS_MOBS;
            case "Loyalty" -> Enchantment.LOYALTY;
            case "LuckOfTheSea" -> Enchantment.LUCK;
            case "Lure" -> Enchantment.LURE;
            case "Mending" -> Enchantment.MENDING;
            case "Multishot" -> Enchantment.MULTISHOT;
            case "Piercing" -> Enchantment.PIERCING;
            case "Power" -> Enchantment.ARROW_DAMAGE;
            case "ProjectileProtection" -> Enchantment.PROTECTION_PROJECTILE;
            case "Protection" -> Enchantment.PROTECTION_ENVIRONMENTAL;
            case "Punch" -> Enchantment.ARROW_KNOCKBACK;
            case "QuickCharge" -> Enchantment.QUICK_CHARGE;
            case "Respiration" -> Enchantment.OXYGEN;
            case "Riptide" -> Enchantment.RIPTIDE;
            case "Sharpness" -> Enchantment.DAMAGE_ALL;
            case "SilkTouch" -> Enchantment.SILK_TOUCH;
            case "Smite" -> Enchantment.DAMAGE_UNDEAD;
            case "SoulSpeed" -> Enchantment.SOUL_SPEED;
            case "SweepingEdge" -> Enchantment.SWEEPING_EDGE;
            case "SwiftSneak" -> Enchantment.SWIFT_SNEAK;
            case "Thorns" -> Enchantment.THORNS;
            case "Unbreaking" -> Enchantment.DURABILITY;
            case "VanishingCurse" -> Enchantment.VANISHING_CURSE;
            default -> null;
        };
    }

    public static boolean isAbilityEnchantment(String key) {
        return switch(key) {
            case "Focus" -> true;
            case "RapidFire" -> true;
            case "SonicBoom" -> true;
            default -> false;
        };
    }
}
