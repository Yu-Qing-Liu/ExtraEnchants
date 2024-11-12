package com.github.yuqingliu.extraenchants.enchantment;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.yuqingliu.extraenchants.api.enchantment.Enchantment;
import com.github.yuqingliu.extraenchants.api.item.Item;
import com.github.yuqingliu.extraenchants.api.managers.ColorManager;
import com.github.yuqingliu.extraenchants.api.managers.CooldownManager;
import com.github.yuqingliu.extraenchants.api.managers.EventManager;
import com.github.yuqingliu.extraenchants.api.managers.LoreManager;
import com.github.yuqingliu.extraenchants.api.managers.MathManager;
import com.github.yuqingliu.extraenchants.api.managers.NameSpacedKeyManager;
import com.github.yuqingliu.extraenchants.api.managers.SoundManager;
import com.github.yuqingliu.extraenchants.api.managers.TextManager;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository;
import com.github.yuqingliu.extraenchants.api.repositories.ManagerRepository;
import com.github.yuqingliu.extraenchants.api.repositories.EnchantmentRepository.EnchantID;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractEnchantment implements Enchantment {
    protected final CooldownManager cooldownManager;
    protected final TextManager textManager;
    protected final MathManager mathManager;
    protected final LoreManager loreManager;
    protected final ColorManager colorManager;
    protected final SoundManager soundManager;
    protected final NameSpacedKeyManager keyManager;
    protected final EnchantmentRepository enchantmentRepository;
    protected final EventManager eventManager;
    protected final JavaPlugin plugin;

    @EqualsAndHashCode.Include
    protected final EnchantID id;

    protected Component name;
    protected Component description;
    protected Duration cooldown;
    protected int maxLevel;
    protected Set<Item> applicable;
    protected Set<EnchantID> conflicting;
    protected String requiredLevelFormula;
    protected String costFormula;
    protected List<TextColor> leveledColors;
    
    @Inject
    public AbstractEnchantment(ManagerRepository managerRepository, EnchantmentRepository enchantmentRepository, EnchantID id, Component name, Component description, int maxLevel, Set<Item> applicable, Set<EnchantID> conflicting, String requiredLevelFormula, String costFormula) {
        this.cooldownManager = managerRepository.getCooldownManager();
        this.textManager = managerRepository.getTextManager();
        this.mathManager = managerRepository.getMathManager();
        this.loreManager = managerRepository.getLoreManager();
        this.colorManager = managerRepository.getColorManager();
        this.soundManager = managerRepository.getSoundManager();
        this.keyManager = managerRepository.getKeyManager();
        this.eventManager = managerRepository.getEventManager();
        this.plugin = managerRepository.getPlugin();
        this.enchantmentRepository = enchantmentRepository;
        this.name = name;
        this.id = id;
        this.description = description;
        this.maxLevel = maxLevel;
        this.applicable = applicable;
        this.conflicting = conflicting;
        this.requiredLevelFormula = requiredLevelFormula;
        this.costFormula = costFormula;
        this.leveledColors = colorManager.generateMonochromaticGradient(this.name.color(), this.maxLevel);
    }

    @Override
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        this.leveledColors = colorManager.generateMonochromaticGradient(this.name.color(), maxLevel);
    }

    @Override
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

    @Override
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
    
    @Override
    public Component getLeveledName(int level) {
        Component name = getName(level);
        Component eLevel = getLevel(level);
        return name.append(eLevel);
    }
    
    @Override
    public TextColor getDescriptionColor() {
        return this.description.color();
    }
    
    @Override
    public TextColor getLevelColor(int level) {
        if (level > leveledColors.size()) {
            return null;
        }
        return this.leveledColors.get(level - 1);
    }
    
    @Override
    public Component getName(int colorLevel) {
        return this.name.color(getLevelColor(colorLevel));
    }
    
    @Override
    public Component getLevel(int colorLevel) {
        return Component.text(" " + textManager.toRoman(colorLevel), getLevelColor(colorLevel));
    }
    
    @Override
    public Component getComma(int colorLevel) {
        return Component.text(", ", getLevelColor(colorLevel));
    }
    
    @Override
    public abstract int getEnchantmentLevel(ItemStack item);
    
    @Override
    public abstract boolean canEnchant(ItemStack item);
    
    @Override
    public abstract ItemStack applyEnchantment(ItemStack item, int level);
    
    @Override
    public abstract ItemStack removeEnchantment(ItemStack item);

    @Override
    public abstract void postConstruct();

    @Override
    public boolean conflictsWith(EnchantID id) {
        return conflicting.contains(id);
    }

    protected ItemStack addOrUpdateEnchantmentLore(ItemStack item, Map<Enchantment, Integer> newEnchants) {
        EnchantmentSection enchantmentSection = (EnchantmentSection) loreManager.getLoreSection(EnchantmentSection.class.getSimpleName(), item);
        enchantmentSection.addOrUpdateEnchantmentFromSection(newEnchants);
        return loreManager.applyLore(item, enchantmentSection);
    }
}
