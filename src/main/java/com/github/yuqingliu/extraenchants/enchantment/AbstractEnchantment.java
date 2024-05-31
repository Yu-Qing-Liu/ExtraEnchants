package com.github.yuqingliu.extraenchants.enchantment;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;
import java.util.regex.Pattern;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchant;
import com.github.yuqingliu.extraenchants.api.utils.ColorUtils;
import com.github.yuqingliu.extraenchants.api.utils.TextUtils;
import com.github.yuqingliu.extraenchants.item.lore.Lore;
import com.github.yuqingliu.extraenchants.item.lore.implementations.EnchantmentSection;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public abstract class AbstractEnchantment implements Enchant {
    protected Component comma = Component.text(", ", NamedTextColor.DARK_BLUE);
    @Setter protected Component name;
    @Setter protected int maxLevel;
    @Setter protected Component description;
    @Setter protected List<Material> applicable;
    @Setter protected List<Component> applicableDisplayNames;
    @Setter protected String requiredLevelFormula;
    @Setter protected String costFormula;
    @Setter protected List<TextColor> leveledColors;

    public AbstractEnchantment(Component name, int maxLevel, Component description, List<Material> applicable, List<Component> applicableDisplayNames, String requiredLevelFormula, String costFormula) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.description = description;
        this.applicable = applicable;
        this.applicableDisplayNames = applicableDisplayNames;
        this.requiredLevelFormula = requiredLevelFormula;
        this.costFormula = costFormula;
        this.leveledColors = ColorUtils.generateMonochromaticGradient(name.color(), maxLevel);
    }
    
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
    
    public Component getLeveledName(int level) {
        Component name = this.name.color(getLevelColor(level));
        Component eLevel = Component.text(" " + TextUtils.toRoman(level), getLevelColor(level));
        return name.append(eLevel);
    }
    
    public TextColor getDescriptionColor() {
        return this.description.color();
    }
    
    public TextColor getLevelColor(int level) {
        if(level != leveledColors.size()) {
            leveledColors = ColorUtils.generateMonochromaticGradient(this.name.color(), this.maxLevel);
        } 
        return this.leveledColors.get(level - 1);
    }
    
    public abstract int getEnchantmentLevel(ItemStack item);
    
    public abstract boolean canEnchant(ItemStack item);
    
    public abstract ItemStack applyEnchantment(ItemStack item, int level);
    
    public abstract ItemStack removeEnchantment(ItemStack item);

    protected ItemStack addOrUpdateEnchantmentLore(ItemStack item, Component enchant, Component eLevel) {
        Lore lore = new Lore(item);
        EnchantmentSection enchantmentSection = (EnchantmentSection) lore.getLoreSection("EnchantmentSection");
        enchantmentSection.addOrUpdateEnchantmentFromSection(enchant, eLevel);
        return lore.applyLore();
    }

    protected ItemStack removeEnchantmentLore(ItemStack item, Component enchant) {
        Lore lore = new Lore(item);
        EnchantmentSection enchantmentSection = (EnchantmentSection) lore.getLoreSection("EnchantmentSection");
        enchantmentSection.removeEnchantmentFromSection(enchant);
        return lore.applyLore();
    }

    protected Component getName(int colorLevel) {
        return this.name.color(getLevelColor(colorLevel));
    }

    protected Component getLevel(int colorLevel) {
        return Component.text(" " + TextUtils.toRoman(colorLevel), getLevelColor(colorLevel));
    }

    protected Component getLevel(int level, int colorLevel) {
        return Component.text(" " + TextUtils.toRoman(level), getLevelColor(colorLevel));
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
