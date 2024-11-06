package com.github.yuqingliu.extraenchants.enchantment;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.inventory.ItemStack;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.managers.ColorManager;
import com.github.yuqingliu.extraenchants.api.managers.LoreManager;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.api.managers.TextManager;
import com.github.yuqingliu.extraenchants.lore.implementations.EnchantmentSection;
import com.google.inject.Inject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

@Getter
@Setter
@EqualsAndHashCode
public abstract class AbstractEnchantment implements Enchantment {
    protected Component comma = Component.text(", ", NamedTextColor.DARK_BLUE);
    protected final TextManager textManager;
    protected final LoreManager loreManager;
    protected final ColorManager colorManager;
    protected final NameSpacedKeyManager keyManager;
    protected Component name;
    protected Component description;
    protected int maxLevel;
    protected Set<Item> applicable;
    protected String requiredLevelFormula;
    protected String costFormula;
    protected List<TextColor> leveledColors;
    
    @Inject
    public AbstractEnchantment(TextManager textManager, LoreManager loreManager, ColorManager colorManager, NameSpacedKeyManager keyManager, Component name, Component description, int maxLevel, Set<Item> applicable, String requiredLevelFormula, String costFormula) {
        this.textManager = textManager;
        this.loreManager = loreManager;
        this.colorManager = colorManager;
        this.keyManager = keyManager;
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
        this.applicable = applicable;
        this.requiredLevelFormula = requiredLevelFormula;
        this.costFormula = costFormula;
    }

    public Component getDescription() {
        Pattern replace = Pattern.compile("(\\d+%?)");
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                .match(replace)
                .replacement((matchResult, builder) -> {
                    String numberStr = matchResult.group(1);
                    String percentage = matchResult.groupCount() > 1 ? matchResult.group(2) : "";
                    return Component.text(numberStr + percentage, NamedTextColor.YELLOW);
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
                    String numberStr = matchResult.group(1);
                    boolean hasPercent = numberStr.endsWith("%");
                    if (hasPercent) {
                        numberStr = numberStr.substring(0, numberStr.length() - 1);
                    }
                    int number = Integer.parseInt(numberStr);
                    int multipliedValue = number * level;
                    String result = String.valueOf(multipliedValue);
                    if (hasPercent) {
                        result += "%";
                    }
                    return Component.text(result, NamedTextColor.YELLOW);
                })
                .build();
        Component finalComponent = leveledDescription.replaceText(replacementConfig);
        return finalComponent;
    }

    public Component getLeveledName(int level) {
        Component name = this.name.color(getLevelColor(level));
        Component eLevel = Component.text(" " + textManager.toRoman(level), getLevelColor(level));
        return name.append(eLevel);
    }

    public TextColor getDescriptionColor() {
        return this.description.color();
    }

    public TextColor getLevelColor(int level) {
        if (level != leveledColors.size()) {
            leveledColors = colorManager.generateMonochromaticGradient(this.name.color(), this.maxLevel);
        }
        return this.leveledColors.get(level - 1);
    }

    public abstract int getEnchantmentLevel(ItemStack item);

    public abstract boolean canEnchant(ItemStack item);

    public abstract ItemStack applyEnchantment(ItemStack item, int level);

    public abstract ItemStack removeEnchantment(ItemStack item);

    protected ItemStack addOrUpdateEnchantmentLore(ItemStack item, Component enchant, Component eLevel) {
        EnchantmentSection enchantmentSection = (EnchantmentSection) loreManager.getLoreSection(EnchantmentSection.class.getSimpleName(), item);
        enchantmentSection.addOrUpdateEnchantmentFromSection(enchant, eLevel);
        return loreManager.applyLore(item, enchantmentSection);
    }

    protected ItemStack removeEnchantmentLore(ItemStack item, Component enchant) {
        EnchantmentSection enchantmentSection = (EnchantmentSection) loreManager.getLoreSection(EnchantmentSection.class.getSimpleName(), item);
        enchantmentSection.removeEnchantmentFromSection(enchant);
        return loreManager.applyLore(item, enchantmentSection);
    }

    protected Component getName(int colorLevel) {
        return this.name.color(getLevelColor(colorLevel));
    }

    protected Component getLevel(int colorLevel) {
        return Component.text(" " + textManager.toRoman(colorLevel), getLevelColor(colorLevel));
    }

    protected Component getLevel(int level, int colorLevel) {
        return Component.text(" " + textManager.toRoman(level), getLevelColor(colorLevel));
    }
}
