package com.github.yuqingliu.extraenchants.api.managers;

import net.kyori.adventure.text.Component;

public interface TextManager {
    String toRoman(int number);
    int fromRoman(String roman);
    Component replaceComponent(Component component, String match, Component replaceComponent);
    boolean componentContains(Component component, String match);
    boolean componentStringEquals(Component expected, Component result);
    String componentToString(Component component);
    String componentToJson(Component component);
    Component jsonToComponent(String json);
    String formatName(String name);
}
