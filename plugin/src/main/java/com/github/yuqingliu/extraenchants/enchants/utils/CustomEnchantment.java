package com.github.yuqingliu.extraenchants.enchants.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import net.kyori.adventure.text.format.TextColor;

import java.util.List;
import java.util.Objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomEnchantment {
    private String name;
    private String addCommand;
    private String removeCommand;
    private int maxLevel;
    private TextColor color;
    private List<Material> applicable;
    private String description;

    public CustomEnchantment(String name, String addCommand, String removeCommand, int maxLevel, TextColor color, List<Material> applicable) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.applicable = applicable;
        this.color = color;
        this.addCommand = addCommand;
        this.removeCommand = removeCommand;
    }

    public void setDescription(String description) {
        String formattedDescription = description.replaceAll("(\\d+%?)", "§e$1§r");
        this.description = formattedDescription;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLeveledDescription(int level) {
        String temp = this.description.replace("per level", "");

        Pattern pattern = Pattern.compile("(\\d+)(%?)");
        Matcher matcher = pattern.matcher(temp);

        // StringBuffer to hold the new description
        StringBuffer newDescription = new StringBuffer();

        while (matcher.find()) {
            // Extract the number and the percent symbol if it exists
            String number = matcher.group(1);
            String percent = matcher.group(2);

            // Convert the captured number to an integer, multiply by level, and convert back to string
            int num = Integer.parseInt(number);
            num *= level; // Multiply the number by the level

            // Append the modified number (and percent if applicable) with color codes
            String replacement = "§e" + num + percent + "§r";
            
            // Replace the matched sequence with our formatted replacement in the StringBuffer
            matcher.appendReplacement(newDescription, replacement);
        }

        // Append the tail of the description (part of the string after the last match)
        matcher.appendTail(newDescription);

        return newDescription.toString();
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public String getName() {
        return name;
    }

    public String getAddCmd() {
        return addCommand;
    }

    public String getRmCmd() {
        return removeCommand;
    }

    public TextColor getColor() {
        return color;
    }

    public boolean canEnchant(ItemStack item) {
        if(item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) return true;
        return applicable.contains(item.getType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomEnchantment that = (CustomEnchantment) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

