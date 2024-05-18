package com.github.yuqingliu.extraenchants.enchantment;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;
import java.util.regex.Pattern;
import java.util.ArrayList;

import org.bukkit.inventory.meta.ItemMeta;

import com.github.yuqingliu.extraenchants.enchantment.AbstractEnchantment;
import com.github.yuqingliu.extraenchants.utils.TextUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public abstract class AbstractEnchantment {
    protected final Component name;
    protected final int maxLevel;
    protected final Component description;
    protected final List<Material> applicable;
    protected final List<Component> applicableDisplayNames;
    protected final String requiredLevelFormula;
    protected final String costFormula;

    public Component getDescription() {
        Pattern replace = Pattern.compile("(\\d+%?)");
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
        .match(replace)
        .replacement((matchResult, builder) -> {
            String numberStr = matchResult.group(1); // Get the captured number as a string
            String percentage = matchResult.groupCount() > 1 ? matchResult.group(2) : ""; // Get the percentage symbol if it exists
            return Component.text(numberStr + percentage, NamedTextColor.YELLOW); // Replace with multiplied value, preserving style
        })
        .build();

        Component finalComponent = description.replaceText(replacementConfig);
        return finalComponent;
    }
    
    public Component getLeveledDescription(int level) {
        Pattern remove = Pattern.compile("per level");
        Component leveledDescription = description.replaceText(builder -> builder.match(remove).replacement(Component.empty()));

        Pattern replace = Pattern.compile("(\\d+%?)");
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
            .match(replace)
            .replacement((matchResult, builder) -> {
                String numberStr = matchResult.group(1); // Get the captured number as a string
                boolean hasPercent = numberStr.endsWith("%");
                if (hasPercent) {
                    numberStr = numberStr.substring(0, numberStr.length() - 1); // Remove the "%" sign
                }
                int number = Integer.parseInt(numberStr); // Convert the string to an integer
                int multipliedValue = number * level; // Multiply by the level
                String result = String.valueOf(multipliedValue);
                if (hasPercent) {
                    result += "%"; // Append "%" sign back if it was present
                }
                return Component.text(result, NamedTextColor.YELLOW); // Replace with multiplied value, preserving style
            })
            .build();

        Component finalComponent = leveledDescription.replaceText(replacementConfig);

        return finalComponent;
    }

    public abstract int getEnchantmentLevel(ItemStack item);

    public abstract boolean canEnchant(ItemStack item);

    public abstract ItemStack applyEnchantment(ItemStack item, int level);

    public abstract ItemStack removeEnchantment(ItemStack item);

    public TextColor getNameColor() {
        return this.name.color();
    }

    public TextColor getDescriptionColor() {
        return this.description.color();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEnchantment that = (AbstractEnchantment) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
